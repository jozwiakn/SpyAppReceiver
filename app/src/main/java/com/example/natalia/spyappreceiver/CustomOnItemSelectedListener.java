package com.example.natalia.spyappreceiver;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

/**
 * Created by Natalia on 15.02.2017.
 */
class CustomOnItemSelectedListener implements OnItemSelectedListener {
    private Functions functions;
    private ArrayAdapter<String> dataAdapter;

    CustomOnItemSelectedListener(Functions functions, ArrayAdapter<String> dataAdapter){
        this.functions = functions;
        this.dataAdapter = dataAdapter;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        functions.filtr_type(parent.getItemAtPosition(pos).toString(), dataAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
