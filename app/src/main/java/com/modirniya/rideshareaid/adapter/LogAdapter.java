package com.modirniya.rideshareaid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modirniya.rideshareaid.LogsActivity;
import com.modirniya.rideshareaid.R;
import com.modirniya.rideshareaid.modules.FuelLog;

import java.util.ArrayList;
import java.util.Locale;

public class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
    private Context context;
    private ArrayList<FuelLog> logs;


    public LogAdapter(Context c, ArrayList<FuelLog> logs) {
        this.context = c;
        this.logs = logs;
        // this.mArrayList = logs;

    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_element_layout, viewGroup, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int i) {
        final FuelLog fuelLog = logs.get(i);
        String time, tag, cost;
        int mileage;
        time = fuelLog.getTime();
        cost = "$" + fuelLog.getCost();
        tag = fuelLog.getTag();
        if (cost.equals("$"))
            cost = "N/A";
        mileage = fuelLog.getMileage();
        holder.tvTime.setText(time);
        if (cost.equals("N/A"))
            holder.tvCost.setVisibility(View.INVISIBLE);
        holder.tvCost.setText(String.format("Cost: %s", cost));
        holder.tvMileage.setText(String.format(Locale.getDefault(), "Mileage: %d", mileage));
        if (tag.equals("fuel")) {
            holder.ivTag.setBackgroundResource(R.drawable.ic_fuel);
            holder.llTag.setBackgroundColor(Color.rgb(125, 200, 125));
        } else if (tag.equals("mileage")) {
            holder.ivTag.setBackgroundResource(R.drawable.ic_car);
            holder.llTag.setBackgroundColor(Color.rgb(125, 125, 200));
        } else {
            holder.ivTag.setBackgroundResource(R.drawable.ic_build);
            holder.llTag.setBackgroundColor(Color.rgb(200, 125, 125));
        }

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LogsActivity) context).editLogDialog("", fuelLog);
//                editTaskDialog(fuelLog, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

}
