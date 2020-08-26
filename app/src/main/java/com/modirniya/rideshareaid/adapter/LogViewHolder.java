package com.modirniya.rideshareaid.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.modirniya.rideshareaid.R;


public class LogViewHolder extends RecyclerView.ViewHolder {
    TextView tvTime, tvMileage, tvCost;
    ImageButton btEdit;
    ImageView ivTag;
    LinearLayout llTag;

    public LogViewHolder(View itemView) {
        super(itemView);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvMileage = itemView.findViewById(R.id.tvMileage);
        tvCost = itemView.findViewById(R.id.tvCost);
        btEdit = itemView.findViewById(R.id.btEdit);
        ivTag = itemView.findViewById(R.id.ivTag);
        llTag = itemView.findViewById(R.id.linearLayoutTag);

    }
}
