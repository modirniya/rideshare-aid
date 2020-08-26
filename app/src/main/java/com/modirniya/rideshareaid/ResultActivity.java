package com.modirniya.rideshareaid;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;

public class ResultActivity extends Activity {

    ImageView imgResult;
    TextView tvResult;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialize();
        Intent intent = getIntent();
        if (intent.hasExtra("condition")) {
            String sExtra = intent.getStringExtra("condition");
            if (sExtra.equals("Excellent")) {
                imgResult.setImageResource(R.drawable.ic_vs);
                tvResult.setText(R.string.b_booming);
            } else if (sExtra.equals("Good")) {
                imgResult.setImageResource(R.drawable.ic_s);
                tvResult.setText(R.string.b_good);
            } else if (sExtra.equals("Slow")) {
                imgResult.setImageResource(R.drawable.ic_us);
                tvResult.setText(R.string.b_slow);
            } else if (sExtra.equals("Dead")) {
                imgResult.setImageResource(R.drawable.ic_vus);
                tvResult.setText(R.string.b_dead);
            } else {
                tvResult.setText(getString(R.string.not_enough_data));
            }
        }
    }


    private void initialize() {
        imgResult = findViewById(R.id.imgResult);
        tvResult = findViewById(R.id.tvResult);
    }
}
