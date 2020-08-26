package com.modirniya.rideshareaid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.modirniya.rideshareaid.adapter.LogAdapter;
import com.modirniya.rideshareaid.modules.FuelLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class LogsActivity extends Activity {
    private String currentUser;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        database = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RecyclerView logView = findViewById(R.id.log_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        logView.setLayoutManager(linearLayoutManager);
        logView.setHasFixedSize(true);
        final ArrayList<FuelLog> allLogs = new ArrayList<>();
        final LogAdapter mAdapter = new LogAdapter(this, allLogs);
        logView.setAdapter(mAdapter);
        DatabaseReference myRef = database.getReference("users")
                .child(currentUser).child("logs");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FuelLog log;
                for (DataSnapshot childNode : snapshot.getChildren()) {
                    allLogs.add((FuelLog) childNode.getValue(FuelLog.class));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLogDialog("", new FuelLog());
            }
        });
    }

    public void editLogDialog(final String error, final FuelLog fuelLog) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_log_layout, null);
        final EditText etMileage = subView.findViewById(R.id.etMileage);
        final EditText etCost = subView.findViewById(R.id.etCost);
        final EditText etMemo = subView.findViewById(R.id.etMemo);
        final TextInputLayout etCostLayout = subView.findViewById(R.id.etCostLayout);
        final MaterialButton tgFuel = subView.findViewById(R.id.tgFuel);
        final MaterialButton tgMileage = subView.findViewById(R.id.tgMileage);
        final MaterialButton tgService = subView.findViewById(R.id.tgService);
        if (error.equals("empty"))
            etMileage.setError("Enter a value for mileage");
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        if (fuelLog.getId() == null) {
            builder.setTitle("New log");
        } else {
            builder.setTitle("Edit log");

        }
        if (fuelLog.getMileage() > 0)
            etMileage.setText(String.valueOf(fuelLog.getMileage()));
        etCost.setText(fuelLog.getCost());
        etMemo.setText(fuelLog.getMemo());
        if (fuelLog.getTag() != null) {
            switch (fuelLog.getTag()) {
                case "fuel":
                    tgFuel.setChecked(true);
                    break;
                case "mileage":
                    tgMileage.setChecked(true);
                    etCostLayout.setVisibility(View.GONE);
                    break;
                case "service":
                    tgService.setChecked(true);
            }
        }
        builder.setView(subView);
        builder.setCancelable(true);
        builder.create();
        tgFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCostLayout.setVisibility(View.VISIBLE);
            }
        });
        tgMileage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCostLayout.setVisibility(View.GONE);
            }
        });
        tgService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCostLayout.setVisibility(View.VISIBLE);
            }
        });
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sMileage = etMileage.getText().toString();
                if (TextUtils.isEmpty(sMileage))
                    sMileage = "0";
                fuelLog.setMileage(Integer.parseInt(sMileage));
                fuelLog.setCost(etCost.getText().toString());
                fuelLog.setMemo(etMemo.getText().toString());
                fuelLog.setTime(get_time());
                if (tgFuel.isChecked())
                    fuelLog.setTag("fuel");
                else if (tgMileage.isChecked())
                    fuelLog.setTag("mileage");
                else
                    fuelLog.setTag("service");

                if (fuelLog.getMileage() <= 0)
                    editLogDialog("empty", fuelLog);
                else {
                    String res = loadSetting("last_mileage", "0");
                    if (Integer.parseInt(res) > fuelLog.getMileage())
                        Toast.makeText(getApplicationContext(), "Mileage has decreased from the last time. please edit it if necessary.", Toast.LENGTH_LONG).show();
                    saveSetting("last_mileage", sMileage);
                    DatabaseReference myRef = database.getReference("users").child(currentUser).child("logs");
                    if (fuelLog.getId() == null) {
                        myRef = myRef.push();
                        fuelLog.setId(myRef.getKey());
                    } else {
                        myRef = myRef.child(fuelLog.getId());
                    }
                    myRef.setValue(fuelLog);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        if (fuelLog.getId() != null)
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (fuelLog.getId() != null) {
                        // Delete data node
                        DatabaseReference myRef = database.getReference("users")
                                .child(currentUser).child("logs").child(fuelLog.getId());
                        myRef.setValue(null);
                        finish();
                        startActivity(getIntent());
                    }
                }
            });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private String get_time() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdFormat = new SimpleDateFormat("EEEE\nMM-dd\nh:mm a");
        return mdFormat.format(calendar.getTime());
    }

    public String loadSetting(String keyword, String def) {
        String result;
        if (def.equals("")) {
            result = "N/A";
        } else {
            result = def;
        }
        SharedPreferences settings = getSharedPreferences("Rideshare-Aid", 0);
        result = settings.getString(keyword, result).toString();
        return result;
    }


    public void saveSetting(String keyword, String value) {
        if (!keyword.equals("")) {
            SharedPreferences settings = getSharedPreferences("Rideshare-Aid", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(keyword, value);
            editor.commit();
        }
    }
}

