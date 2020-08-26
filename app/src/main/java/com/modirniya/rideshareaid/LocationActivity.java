package com.modirniya.rideshareaid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

public class LocationActivity extends Activity implements AdapterView.OnItemSelectedListener {
    TextInputLayout textInputLayout;
    Spinner spRegion;
    AutoCompleteTextView autoCompleteTextView;
    Button btSave;
    List<String> myResArrayList;
    int indexDefault = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initialize();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] myResArray;
        //Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        if (position == 0) {
            myResArray = getResources().getStringArray(R.array.NorthAmerica);
            indexDefault = 100;
        } else if (position == 1) {
            myResArray = getResources().getStringArray(R.array.CentralSouthAmerica);
            indexDefault = 430;
        } else if (position == 2) {
            myResArray = getResources().getStringArray(R.array.Europe);
            indexDefault = 617;
        } else if (position == 3) {
            myResArray = getResources().getStringArray(R.array.AustraliaNewZealand);
            indexDefault = 850;
        } else if (position == 4) {
            myResArray = getResources().getStringArray(R.array.SouthAsia);
            indexDefault = 804;
        } else if (position == 5) {
            myResArray = getResources().getStringArray(R.array.EastAsia);
            indexDefault = 781;
        } else {
            myResArray = getResources().getStringArray(R.array.MiddleEast);
            indexDefault = 746;
        }
        myResArrayList = Arrays.asList(myResArray);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myResArrayList);
        // Showing selected spinner item
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initialize() {
        spRegion = findViewById(R.id.spRegion);
        autoCompleteTextView = findViewById(R.id.tvCity);
        textInputLayout = findViewById(R.id.textInputLayout);
        btSave = findViewById(R.id.btSave);


        spRegion.setOnItemSelectedListener(this);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sCity = autoCompleteTextView.getText().toString();
                if (myResArrayList.contains(sCity)) {
                    saveSetting("city_name", sCity);
                    int i = myResArrayList.indexOf(sCity);
                    sCity = String.valueOf(i + indexDefault);
                    saveSetting("city_code", sCity);
                    Intent intent = new Intent(LocationActivity.this, CoopActivity.class);
                    // Toast.makeText(v.getContext(), sCity, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    // next activity (startActivity(intent)
                } else {
                    textInputLayout.setError(sCity + " is not listed as a supported city yet");
                }
            }
        });
    }

    private void saveSetting(String keyword, String value) {
        if (!keyword.equals("") || !value.equals("")) {
            SharedPreferences settings = getSharedPreferences("Rideshare-Aid", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(keyword, value);
            editor.apply();
        }
    }
}
