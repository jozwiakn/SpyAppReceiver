package com.example.natalia.spyappreceiver;

import android.app.ListActivity;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConnectActivity extends ListActivity {

    private Functions functions;
    private String response_connect = "";

    private ArrayAdapter<String> adapterForList;
    private AlertDialog.Builder builder;

    private Spinner spinner;
    private ArrayAdapter<String> adapterForType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            response_connect = "";
        } else {
            response_connect = extras.getString("RESPONSE");
        }

        functions = new Functions(2);

        builder = new AlertDialog.Builder(this);

        adapterForList = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                functions.listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return functions.adapter(view);
            }
        };
        setListAdapter(adapterForList);
        addItemsOnSpinner();
        getAndUpdate();
        response_connect = functions.getRequest("list_connect/");
    }


    private void addItemsOnSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_connect);
        List<String> list = new ArrayList<>();
        list.add("wszystkie polaczenia");
        list.add("polaczenia przychodzace");
        list.add("polaczenia wychodzace");
        list.add("polaczenia nieodebrane");
        adapterForType = new ArrayAdapter<>(this,
                R.layout.spinner_item, list);
        adapterForType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorSpinner), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(adapterForType);
    }

    private void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.spinner_connect);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(functions, adapterForList));
    }

    private void getAndUpdate() {
        int clickCounter = 0;
        functions.clearLists();

        Gson gson = new Gson();
        if (!response_connect.equals("")) {
            Type type2 = new TypeToken<List<Connect>>() {
            }.getType();
            List<Connect> connectList = gson.fromJson(response_connect, type2);
            for (Connect connect : connectList) {
                if (connect.log.equals(Start.serialNumber)) {
                    String item = "                                    " + connect.start_time + "\r\n" + connect.number;
                    String item2 = "dnia: " + connect.start_time + "\r\n" + "numer: " + connect.number;
                    String message = "polaczenie: " + connect.type;
                    String time = connect.start_time.substring(0, 10);

                    functions.listItemsTemp.add(clickCounter, item);
                    functions.listTitleDetailsTemp.add(clickCounter, item2);
                    functions.listMessageDetailsTemp.add(clickCounter, message);
                    functions.listNumberTemp.add(clickCounter, connect.number);
                    functions.listDateTemp.add(clickCounter, time);
                    functions.typeTemp.add(clickCounter, connect.type);
                    clickCounter = clickCounter + 1;
                }
            }
            functions.sweep();
        }
        addListenerOnSpinnerItemSelection();
    }

    public void refresh(View view) {
        response_connect = functions.getRequest("list_connect/");
        getAndUpdate();
        adapterForList.notifyDataSetChanged();
        spinner.setAdapter(adapterForType);
        functions.filtr_type = false;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        functions.onListItemDialog(pos, this);
    }

    public void filtr_number(View view) {
        functions.AddToArray(functions.listNumberTemp, functions.number_temp);
        String[] numbers = new String[functions.number_temp.size()];
        functions.filtr_number(this, numbers, builder, adapterForList);
        spinner.setAdapter(adapterForType);
    }

    public void filtr_date(View view) {
        functions.AddToArray(functions.listDateTemp, functions.date_temp);
        String[] dates = new String[functions.date_temp.size()];
        functions.filtr_date(this, dates, builder, adapterForList);
        spinner.setAdapter(adapterForType);
    }
}
