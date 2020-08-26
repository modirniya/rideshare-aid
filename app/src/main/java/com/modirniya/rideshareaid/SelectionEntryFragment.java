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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectionEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectionEntryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String CITY_CODE = "city_code";

    private FirebaseFunctions mFunctions;


    private String sResult = "N/A";

    private Button btSubmit;

    // TODO: Rename and change types of parameters
    private String cityCode;

    public SelectionEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectionEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectionEntryFragment newInstance(String param1, String param2) {
        SelectionEntryFragment fragment = new SelectionEntryFragment();
        Bundle args = new Bundle();
        args.putString(CITY_CODE, param1);
        fragment.setArguments(args);
        return fragment;
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
        btSubmit = view.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("city_code", cityCode);
                data.put("selection", sResult);
                callFunctions("stat", data);
                ((CoopActivity)getActivity()).feedbackReceived();
            }
        });
        return view;
    }

    private void callFunctions(String funcName, Map<String, Object> data) {
        mFunctions
                .getHttpsCallable(funcName)
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return (String) Objects.requireNonNull(task.getResult()).getData();
                    }
                });
    }


}