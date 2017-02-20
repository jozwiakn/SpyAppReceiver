package com.example.natalia.spyappreceiver;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * Created by Natalia on 15.02.2017.
 */
public class CustomOnItemSelectedListener implements OnItemSelectedListener {
    Functions functions;
    ArrayAdapter<String> dataAdapter;

    public CustomOnItemSelectedListener(Functions functions, ArrayAdapter<String> dataAdapter){
        this.functions = functions;
        this.dataAdapter = dataAdapter;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        functions.filtr_type(parent.getItemAtPosition(pos).toString(), dataAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
