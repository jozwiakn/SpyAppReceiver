package com.example.natalia.spyappreceiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Start extends AppCompatActivity {

    static String serialNumber;
    private static String myId;
    private AutoCompleteTextView autoCompleteTextView;

    private ArrayList<String> myIdList = new ArrayList<>();
    private ArrayList<String> serialNrList = new ArrayList<>();

    private int clickCounter = 0;
    private String response_login = "";
    private String response_message = "";
    private String response_connect = "";

    private int layout = 0;
    private int search = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        layout=0;

        System.out.println("ON CREATE START ACTIVITY");
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete);
        TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        myId = telemamanger.getSimSerialNumber();

        getRequest("list_connect/");
        getRequest("list_message/");
        getRequest("list_login/");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("RUN TIMER");
                saveLog();
            }
        }, 5 * 1000);

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("LISTENER AUTO COMPLETE LIST");
                if(hasFocus) {
                    autoComplete();
                }
            }
        });

    }

    public void message(View view) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("RESPONSE", response_message);
        startActivity(intent);

    }

    public void connect(View view) {
        Intent intent = new Intent(this, ConnectActivity.class);
        intent.putExtra("RESPONSE", response_connect);
        startActivity(intent);
    }


    public void zaloguj(View view) {
        if (autoCompleteTextView.length() >= 18) {
            serialNumber = autoCompleteTextView.getText().toString();
            search = 0;
            for (int i = 0; i < myIdList.size(); i++) {
                if (myId.equals(myIdList.get(i)) && (serialNumber.equals(serialNrList.get(i)))) {
                    search = 1;
                }
            }
            if (checkMessage()==0 && checkConnect()==0){
                Toast.makeText(getApplicationContext(), "NIE MA TAKIEGO LOGINU", Toast.LENGTH_LONG).show();
            }
            else {
                if (search == 0) {
                    postRequest(myId, serialNumber);
                }

                serialNumber = autoCompleteTextView.getText().toString();
                setContentView(R.layout.activity_start);
                layout = 1;
            }
        } else {
            Toast.makeText(getApplicationContext(), "NUMER IDENTYFIKACYJNY JEST ZBYT KROTKI", Toast.LENGTH_LONG).show();
        }
    }

    public int checkMessage(){
        int clickCounter = 0;
        Gson gson = new Gson();
        if (!response_message.equals("")) {
            Type type2 = new TypeToken<List<Messages>>() {
            }.getType();
            List<Messages> messagesList = gson.fromJson(response_message, type2);
            for (Messages messages : messagesList) {
                if (messages.log.equals(serialNumber)) {
                    clickCounter = clickCounter + 1;
                }
            }
        }
        return clickCounter;
    }

    public int checkConnect(){
        int clickCounter = 0;
        Gson gson = new Gson();
        if (!response_connect.equals("")) {
            Type type2 = new TypeToken<List<Connect>>() {
            }.getType();
            List<Connect> connectList = gson.fromJson(response_connect, type2);
            for (Connect connect : connectList) {
                if (connect.log.equals(serialNumber)) {
                    clickCounter = clickCounter + 1;
                }
            }
        }
        return clickCounter;
    }

    private void saveLog() {
        System.out.println("SAVE LOG");
        Gson gson = new Gson();
        if (!response_login.equals("")) {
            System.out.println("response_login");
            Type type2 = new TypeToken<List<Login>>() {
            }.getType();
            Log.i("get type", "get type");
            List<Login> loginList = gson.fromJson(response_login, type2);
            Log.i("login", "save from Json");
            for (Login login : loginList) {
                if(login.my_id.equals(myId)) {
                    System.out.println(login.login);
                    myIdList.add(clickCounter++, login.my_id);
                    serialNrList.add(clickCounter - 1, login.login);
                    System.out.println("add to list login");
                }
            }
        }

    }

    private void autoComplete() {
        System.out.println("AUTOCOMPLETE");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, serialNrList);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.auto_complete);
        System.out.println("AUTO COMPLETE LIST size " + serialNrList.size());
        textView.setAdapter(adapter);
    }

    private void postRequest(String my_id, String serialNumber) {
        System.out.println("POST REQUEST log");
        RequestParams params = new RequestParams();
        params.put("my_id", my_id);
        params.put("login", serialNumber);
//        System.out.println("OK POST REQUESR");
        SpyAppRestClient.post("create_list/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("ONSUCCESS LOGIN");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("ON FAILURE LOGIN");
            }
        });
    }


    private void getRequest(final String url) {
        SpyAppRestClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("onSuccess");
                if (responseBody == null) { /* empty response_login, alert something*/
                    return;
                }
                switch(url){
                    case "list_login/":
                        response_login = new String(responseBody);
                        break;
                    case "list_connect/":
                        response_connect = new String(responseBody);
                        break;
                    case "list_message/":
                        response_message = new String(responseBody);
                }
//                response_login = new String(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("onFailure");
                if (responseBody == null) { /* empty response_login, alert something*/
                    return;
                }
                switch(url){
                    case "list_login/":
                        response_login = new String(responseBody);
                        break;
                    case "list_connect/":
                        response_connect = new String(responseBody);
                        break;
                    case "list_message/":
                        response_message = new String(responseBody);
                }
//                response_login = new String(responseBody);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(layout == 1) {
            autoCompleteTextView.setText("");
            System.out.println("BAK TO LOGIN");
            setContentView(R.layout.login);

            autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete);
            autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    System.out.println("LISTENER AUTO COMPLETE LIST");
                    if(hasFocus) {
                        autoComplete();
                    }
                }
            });
            layout=0;
        }
        else{
            finish();
            System.exit(1);
        }
    }
}
