package com.example.natalia.spyappreceiver;

import android.app.*;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.lang.reflect.Type;
import java.util.*;

public class MessageActivity extends ListActivity {

    private Class klasa;

    private String response_message = "";
    private String url = "list_message/";


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
    private ArrayList<String> number_temp; //lista wszystkich numerow
    private ArrayList<String> date;
    private ArrayList<String> date_temp; //lista dat

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        klasa = new Class(0);
        klasa.init();

        listItems = klasa.listItems;
        listItemsTemp = klasa.listItemsTemp;

        listMessageDetails = klasa.listMessageDetails;
        listMessageDetailsTemp = klasa.listMessageDetailsTemp;

        listTitleDetails = klasa.listTitleDetails;
        listTitleDetailsTemp = klasa.listTitleDetailsTemp;

        number = klasa.number;
        number_temp = klasa.number_temp;

        date = klasa.date;
        date_temp = klasa.date_temp;

        mSelectedItems_nr = klasa.mSelectedItems_nr;
        mSelectedItems_date = klasa.mSelectedItems_date;

        builder = new AlertDialog.Builder(this);

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listItems) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return klasa.adapter(view);
            }
        };
        setListAdapter(adapter);
        getAndUpdate();
    }

    public void getAndUpdate() {
        getRequest(url);
        int clickCounter = 0;

       klasa.clearLists();

        Gson gson = new Gson();
        if (!response_message.equals("")) {
            Type type2 = new TypeToken<List<Messages>>() {
            }.getType();
            List<Messages> messagesList = gson.fromJson(response_message, type2);
            for (Messages messages : messagesList) {
                if (messages.log.equals(Start.serialNumber)) {
                    System.out.println(messages.log);
                    String item = "                                    " + messages.start_time + "\r\n" + messages.number;
                    String item2 = "dnia: " + messages.start_time + "\r\n" + "od: " + messages.number;
                    String time = messages.start_time.substring(0, 10);
                    listItemsTemp.add(clickCounter++, item);
                    listTitleDetailsTemp.add(clickCounter - 1, item2);
                    listMessageDetailsTemp.add(clickCounter - 1, messages.text);
                    number.add(clickCounter - 1, messages.number);
                    date.add(clickCounter - 1, time);
                }
            }
            klasa.sweep();
        }
    }

    private void getRequest(String url) {
        SpyAppRestClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("onSuccess");
                if (responseBody == null) { /* empty response, alert something*/
                    return;
                }
                response_message = new String(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("onFailure");
                if (responseBody == null) { /* empty response, alert something*/
                    return;
                }
                response_message = new String(responseBody);

            }
        });
    }


    public void odswiez(View view) {
        getAndUpdate();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);

        klasa.onListItemDialog(pos, this);
    }



    public void filtr_number(View view) {
        klasa.AddToArray(number, number_temp);

        String[] numbers = new String[number_temp.size()];
        klasa.filtr_number(this, numbers, builder, adapter);
    }

    public void filtr_date(View view) {
        klasa.AddToArray(date, date_temp);

        String[] dates = new String[date_temp.size()];
        klasa.filtr_date(this, dates, builder, adapter);
    }
}