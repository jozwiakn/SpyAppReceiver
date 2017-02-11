package com.example.natalia.spyappreceiver;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends ListActivity {

    private Class klasa;

    private String response_location = "";
    private String url = "list_location/";

    public static ArrayList<String> listDate;
    public static ArrayList<String> listDateTemp; //lista dat po filtrowaniu

    private ArrayList<String> listItems;
    private ArrayList<String> listItemsTemp; //data i miejscowosc na liscie
    private ArrayList<String> listMessageDetails;
    private ArrayList<String> listMessageDetailsTemp; //ulica i kraj (szczegoly)
    private ArrayList<String> listTitleDetails;
    private ArrayList<String> listTitleDetailsTemp; //data i miejscowosc (szczegoly)

    public static ArrayList<String> listLongitude;
    private ArrayList<String> listLongitudeTemp;
    public static ArrayList<String> listLatitude;
    private ArrayList<String> listLatitudeTemp; //wspolrzedne

    private ArrayAdapter<String> adapter;

    private ArrayList<Integer> mSelectedItems_date; //zaznaczanie Dialog daty

    private ArrayList<String> date;
    private ArrayList<String> date_temp; //lista dat

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        klasa = new Class(1);
        klasa.init();

        listDate = klasa.listDate;
        listDateTemp = klasa.listDateTemp;

        listItems = klasa.listItems;
        listItemsTemp = klasa.listItemsTemp;

        listMessageDetails = klasa.listMessageDetails;
        listMessageDetailsTemp = klasa.listMessageDetailsTemp;

        listTitleDetails = klasa.listTitleDetails;
        listTitleDetailsTemp = klasa.listTitleDetailsTemp;

        listLongitude = klasa.listLongitude;
        listLongitudeTemp = klasa.listLongitudeTemp;

        listLatitude = klasa.listLatitude;
        listLatitudeTemp = klasa.listLatitudeTemp;

        date = klasa.date;
        date_temp = klasa.date_temp;

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
        if (!response_location.equals("")) {
            System.out.println("if");
            Type type2 = new TypeToken<List<Location>>() {
            }.getType();
            List<Location> locationList = gson.fromJson(response_location, type2);
            System.out.println("type");
            for (Location location : locationList) {
                System.out.println("LOCATION SERIAL NR:" + location.log);
                if (Start.serialNumber.equals(location.log)) {
                    System.out.println("for");
                    String item = "                                    " + location.time + "\r\n" + location.city;
                    System.out.println("item 1");
                    String item2 = "dnia: " + location.time + "\r\n" + location.city;
                    System.out.println("item 2");

                    String message = "szczegoly: " + "\r\n" + location.street + "\r\n" + location.country;
                    System.out.println("szczegoly");
                    String time = location.time.substring(0, 10);
                    listDateTemp.add(clickCounter++, location.time);
                    listItemsTemp.add(clickCounter - 1, item);
                    listTitleDetailsTemp.add(clickCounter - 1, item2);
                    listMessageDetailsTemp.add(clickCounter - 1, message);
                    listLongitudeTemp.add(clickCounter - 1, location.longitude);
                    listLatitudeTemp.add(clickCounter - 1, location.latitude);
                    date.add(clickCounter - 1, time);

                }
            }
            klasa.sweep();
        }
    }

    public void getRequest(String url) {
        SpyAppRestClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("onSuccess");
                if (responseBody == null) { /* empty response, alert something*/
                    return;
                }
                //success response, do something with it!
                response_location = new String(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody == null) { /* empty response, alert something*/
                    return;
                }
                response_location = new String(responseBody);

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


    public void filtr_date(View view) {
        klasa.AddToArray(date, date_temp);

        String[] dates = new String[date_temp.size()];
        klasa.filtr_date(this, dates, builder, adapter);
    }

    public void map(View view) {
        if(listLatitude.size()>0) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(), "Brak lokalizacji na liscie, SPROBUJ ODSWIERZYC LISTE", Toast.LENGTH_LONG).show();

    }
}
