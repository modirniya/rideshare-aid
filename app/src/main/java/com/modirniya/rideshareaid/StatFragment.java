package com.modirniya.rideshareaid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.modirniya.rideshareaid.modules.Stat;

import java.util.ArrayList;
import java.util.Collections;

public class StatFragment extends Fragment {

    static final String CITY_CODE = "city_code";

    private String cityCode;

    public StatFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_stat, container, false);
        final BarChart barChart = (BarChart) view.findViewById(R.id.barChart);
        final LinearLayout layContainer = view.findViewById(R.id.layContainer);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        final TextView tvCondition = view.findViewById(R.id.tvCondition);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference stats = database.getReference("Statistics").child(cityCode);

        stats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Stat stat = snapshot.getValue(Stat.class);
                assert stat != null;
                int[] results = new int[]
                        {stat.getDead(), stat.getSlow(), stat.getGood(), stat.getExcellent()};
                progressBar.setVisibility(View.INVISIBLE);
                layContainer.setVisibility(View.VISIBLE);
                ArrayList<BarEntry> entries = new ArrayList<>();
                int max = 0, condition = 0;
                for (int i = 0; i < 4; i++) {
                    int res = results[i];
                    if (res > max) {
                        condition = i;
                        max = res;
                    }
                    entries.add(new BarEntry(results[i], i));
                }
                String sPrompt = "Business been ";
                if (max != 0) {
                    switch (condition) {
                        case 0:
                            sPrompt += "very slow today!";
                            break;
                        case 1:
                            sPrompt += "slow today!";
                            break;
                        case 2:
                            sPrompt += "good today!";
                            break;
                        case 3:
                            sPrompt += "excellent today!";
                            break;
                    }
                }
                tvCondition.setText(sPrompt);
                BarDataSet bardataset = new BarDataSet(entries, "");

                ArrayList<String> labels = new ArrayList<>();
                labels.add("Dead");
                labels.add("Slow");
                labels.add("Good");
                labels.add("Excellent");

                BarData data = new BarData(labels, bardataset);
                barChart.setData(data); // set the data and list of labels into chart
                barChart.setDescription("");  // set the description
                bardataset.setColors(Collections.singletonList(getResources().getColor(R.color.colorPrimary)));
                barChart.animateY(3000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}