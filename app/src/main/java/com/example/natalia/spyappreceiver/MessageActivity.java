package com.example.natalia.spyappreceiver;

import android.app.*;
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
import java.util.*;

public class MessageActivity extends ListActivity {

    private Functions functions;
    private String response_message = "";
    private ArrayAdapter<String> adapterForList;
    private AlertDialog.Builder builder;

    private Spinner spinner;
    private ArrayAdapter<String> adapterForType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            response_message= "";
        } else {
            response_message= extras.getString("RESPONSE");
        }

        functions = new Functions(1);

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
    }



    public void addItemsOnSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_message);
        List<String> list = new ArrayList<>();
        list.add("wszystkie wiadomosci");
        list.add("wiadomosci przychodzace");
        list.add("wiadomosci wychodzace");
        adapterForType = new ArrayAdapter<>(this,
                R.layout.spinner_item, list);
        adapterForType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorSpinner), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(adapterForType);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.spinner_message);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(functions, adapterForList));
    }

    private void getAndUpdate() {
        int clickCounter = 0;
       functions.clearLists();

        Gson gson = new Gson();
        if (!response_message.equals("")) {
            Type type2 = new TypeToken<List<Messages>>() {
            }.getType();
            List<Messages> messagesList = gson.fromJson(response_message, type2);
            for (Messages messages : messagesList) {
                if (messages.log.equals(Start.serialNumber)) {
                    String item = "                                    " + messages.start_time + "\r\n" + messages.number;
                    String item2 = "dnia: " + messages.start_time + "\r\n" + "od: " + messages.number;
                    String time = messages.start_time.substring(0, 10);

                    functions.listItemsTemp.add(clickCounter, item);
                    functions.listTitleDetailsTemp.add(clickCounter, item2);
                    functions.listMessageDetailsTemp.add(clickCounter, messages.text);
                    functions.listNumberTemp.add(clickCounter, messages.number);
                    functions.listDateTemp.add(clickCounter, time);
                    functions.typeTemp.add(clickCounter, messages.type);
                    functions.positions.add(clickCounter, messages.position);
                    clickCounter = clickCounter + 1;
                }
            }
            functions.sweep();
        }
        addListenerOnSpinnerItemSelection();
    }

    public void refresh(View view) {
        response_message = functions.getRequest("list_message/");
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