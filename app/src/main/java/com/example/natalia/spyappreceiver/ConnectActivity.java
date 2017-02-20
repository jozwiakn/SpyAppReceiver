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
    private String url = "list_connect/";


    private ArrayList<String> listItems;
    private ArrayList<String> listItemsTemp; //czas i numer na liscie
    private ArrayList<String> listMessageDetails;
    private ArrayList<String> listMessageDetailsTemp; //tresc wiadomosci (szczegoly)
    private ArrayList<String> listTitleDetails;
    private ArrayList<String> listTitleDetailsTemp; //czas i numer (szczegoly)

    private ArrayAdapter<String> adapter;

    private ArrayList<Integer> mSelectedItems_nr; //zaznaczanie Dialog numery
    private ArrayList<Integer> mSelectedItems_date; //zaznaczanie Dialog daty

    private ArrayList<String> number;
    private ArrayList<String> number_temp; //lista unikalnych numerow
    private ArrayList<String> listNumberTemp;

    private ArrayList<String> date;
    private ArrayList<String> listDateTemp;
    private ArrayList<String> date_temp; //lista unikalnych dat

    private ArrayList<String> type; //typ polaczenia
    private ArrayList<String> typeTemp; //typ polaczenia

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Bundle extras = getIntent().getExtras();
        response_connect = extras.getString("RESPONSE");

        functions = new Functions(2);
        functions.init();

        listItems = functions.listItems;
        listItemsTemp = functions.listItemsTemp;

        listMessageDetails = functions.listMessageDetails;
        listMessageDetailsTemp = functions.listMessageDetailsTemp;

        listTitleDetails = functions.listTitleDetails;
        listTitleDetailsTemp = functions.listTitleDetailsTemp;

        number = functions.number;
        number_temp = functions.number_temp;
        listNumberTemp = functions.listNumberTemp;

        date = functions.date;
        date_temp = functions.date_temp;
        listDateTemp = functions.listDateTemp;

        mSelectedItems_nr = functions.mSelectedItems_nr;
        mSelectedItems_date = functions.mSelectedItems_date;

        type = functions.type;
        typeTemp= functions.typeTemp;

        builder = new AlertDialog.Builder(this);


        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listItems) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return functions.adapter(view);
            }
        };
        setListAdapter(adapter);

        addItemsOnSpinner2();

//        getRequest(url);
        getAndUpdate();
    }

    Spinner spinner;
    ArrayAdapter<String> dataAdapter;

    public void addItemsOnSpinner2() {

        spinner = (Spinner) findViewById(R.id.spinner_connect);
        List<String> list = new ArrayList<>();
        list.add("wszystkie polaczenia");
        list.add("polaczenia przychodzace");
        list.add("polaczenia wychodzace");
        list.add("polaczenia nieodebrane");
        dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorSpinner), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.spinner_connect);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(functions, adapter));
    }

    public void getAndUpdate() {
//        getRequest(url);
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
                    String message = "polaczenie: " + connect.time;
                    String time = connect.start_time.substring(0, 10);

                    listItemsTemp.add(clickCounter, item);
                    listTitleDetailsTemp.add(clickCounter, item2);
                    listMessageDetailsTemp.add(clickCounter, message);
                    listNumberTemp.add(clickCounter, connect.number);
                    listDateTemp.add(clickCounter, time);
                    typeTemp.add(clickCounter, connect.time);
                    clickCounter = clickCounter + 1;
                }
            }
            functions.sweep();
        }
        addListenerOnSpinnerItemSelection();
    }

    public void getRequest(String url) {
        SpyAppRestClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody == null) { /* empty response, alert something*/
                    return;
                }
                //success response, do something with it!
                response_connect = new String(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("onFailure");
                if (responseBody == null) { /* empty response, alert something*/
                    return;
                }
                response_connect = new String(responseBody);

            }
        });
    }

    public void refresh(View view) {
        getRequest(url);
        getAndUpdate();
        adapter.notifyDataSetChanged();
        spinner.setAdapter(dataAdapter);
        functions.filtr_type = false;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);

        functions.onListItemDialog(pos, this);
    }

    public void filtr_number(View view) {
        functions.AddToArray(functions.listNumberTemp, functions.number_temp);

        String[] numbers = new String[number_temp.size()];
        functions.filtr_number(this, numbers, builder, adapter);
        spinner.setAdapter(dataAdapter);
    }

    public void filtr_date(View view) {
        functions.AddToArray(functions.listDateTemp, functions.date_temp);

        String[] dates = new String[date_temp.size()];
        functions.filtr_date(this, dates, builder, adapter);
        spinner.setAdapter(dataAdapter);
    }
}
