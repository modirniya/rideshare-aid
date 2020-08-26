package com.modirniya.rideshareaid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SelectionEntryFragment extends Fragment {

    static final String CITY_CODE = "city_code";

    private FirebaseFunctions mFunctions;

    private String sResult = "N/A";

    private String cityCode;

    public SelectionEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityCode = getArguments().getString(CITY_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selection_entry, container, false);
        // Initiate Functions
        mFunctions = FirebaseFunctions.getInstance();
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                sResult = "";
                if (checkedId == R.id.rbExcellent) {
                    sResult += "excellent";
                } else if (checkedId == R.id.rbGood) {
                    sResult += "good";
                } else if (checkedId == R.id.rbSlow) {
                    sResult += "slow";
                } else {
                    sResult += "dead";
                }
            }
        });
        Button btSubmit = view.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("city_code", cityCode);
                data.put("selection", sResult);
                callFunctions(data);
                ((CoopActivity) Objects.requireNonNull(getActivity())).feedbackReceived();
            }
        });
        return view;
    }

    private void callFunctions(Map<String, Object> data) {
        mFunctions
                .getHttpsCallable("stat")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) {
                        return (String) Objects.requireNonNull(task.getResult()).getData();
                    }
                });
    }


}