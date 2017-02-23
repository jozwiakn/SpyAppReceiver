package com.example.natalia.spyappreceiver;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Natalia on 29.12.2016.
 */
class Functions {
    ArrayList<String> listItems;
    ArrayList<String> listItemsTemp; //czas i numer na liscie
    private ArrayList<String> listMessageDetails;
    ArrayList<String> listMessageDetailsTemp; //tresc wiadomosci (szczegoly)
    private ArrayList<String> listTitleDetails;
    ArrayList<String> listTitleDetailsTemp; //czas i numer (szczegoly)

    private ArrayList<Integer> mSelectedItems_nr; //zaznaczanie Dialog numery
    private ArrayList<Integer> mSelectedItems_date; //zaznaczanie Dialog daty

    ArrayList<String> number_temp; //lista wszystkich numerow
    ArrayList<String> listNumberTemp;

    ArrayList<String> date_temp; //lista dat
    ArrayList<String> listDateTemp;

    ArrayList<String> positionsTemp;
    private ArrayList<String> positions; //sms pozycja

    private ArrayList<String> type;
    ArrayList<String> typeTemp; //polaczenie i wiadomosci typ

    boolean filtr_type;
    private ArrayList<String> listItemsForType = new ArrayList<>();
    private ArrayList<String> listMessageDetailsForType = new ArrayList<>();
    private ArrayList<String> listTitleDetailsForType = new ArrayList<>();
    private ArrayList<String> listTypeForType = new ArrayList<>();
    private ArrayList<String> listPositionsForType;

    private int typeClass; //1 - messages, 2 - call
    private boolean start;
    private String response = "";

    Functions(int type) {
        this.typeClass = type;
        init();
    }

    private void init() {
        listItems = new ArrayList<>();
        listItemsTemp = new ArrayList<>();

        listMessageDetails = new ArrayList<>();
        listMessageDetailsTemp = new ArrayList<>();

        listTitleDetails = new ArrayList<>();
        listTitleDetailsTemp = new ArrayList<>();

        number_temp = new ArrayList<>();
        listNumberTemp = new ArrayList<>();

        date_temp = new ArrayList<>();
        listDateTemp = new ArrayList<>();

        mSelectedItems_nr = new ArrayList<>();
        mSelectedItems_date = new ArrayList<>();

        if (typeClass == 1) {
            positionsTemp = new ArrayList<>(); //for messages - position of cursor
            positions = new ArrayList<>(); //for messages - position of cursor
            listPositionsForType = new ArrayList<>();
        }
        type = new ArrayList<>();
        typeTemp = new ArrayList<>();
        filtr_type = false;
        start = true;

    }


    void AddToArray(ArrayList<String> array, ArrayList<String> arrayTemp) {
        arrayTemp.clear();
        int index = 0;
        int yes;
        for (int i = 0; i < array.size(); i++) {
            yes = 0;
            for (int j = 0; j < arrayTemp.size(); j++) {
                if (array.get(i).contains(arrayTemp.get(j)) || arrayTemp.get(j).contains(array.get(i))) {
                    yes = 1;
                }
            }
            if (yes == 0) {
                arrayTemp.add(index++, array.get(i));
            }
        }
    }

    void sweep() {
        switch (typeClass) {
            case 2:
                int index = 0;
                for (int i = listItemsTemp.size() - 1; i >= 0; i--) {
                    addFromTempList(index, i);
                    index = index + 1;
                }
                break;
            case 1:
                if (positionsTemp.size() != 0) {
                    index = 0;
                    int maximum = Integer.parseInt(Collections.max(positionsTemp));
                    int minimum = Integer.parseInt(Collections.min(positionsTemp));
                    for (int j = maximum; j >= minimum; j--) {
                        for (int i = positionsTemp.size() - 1; i >= 0; i--) {
                            if (positionsTemp.get(i).equals(Integer.toString(j))) {
                                addFromTempList(index, i);
                                index = index + 1;
                            }
                        }
                    }
                }
                break;
        }
    }

    void onListItemDialog(int pos, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(listMessageDetails.get(pos))
                .setTitle(listTitleDetails.get(pos));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setFilter(ArrayList<Integer> mSelectedItems, ArrayList<String> array) {
        listItems.clear();
        listMessageDetails.clear();
        listTitleDetails.clear();
        if (typeClass == 1) positions.clear();
        type.clear();
        int index = 0;

        switch (typeClass) {
            case 1:
                int maximum = Integer.parseInt(Collections.max(positionsTemp));
                int minimum = Integer.parseInt(Collections.min(positionsTemp));
                for (int k = 0; k < mSelectedItems.size(); k++) {
                    for (int j = maximum; j >= minimum; j--) {
                        for (int i = listItemsTemp.size() - 1; i >= 0; i--) {
                            if (listItemsTemp.get(i).contains(array.get(mSelectedItems.get(k)))) {
                                if (positionsTemp.get(i).equals(Integer.toString(j))) {
                                    addFromTempList(index, i);
                                    index = index + 1;
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                for (int k = 0; k < mSelectedItems.size(); k++) {
                    for (int i = listItemsTemp.size() - 1; i >= 0; i--) {
                        if (listItemsTemp.get(i).contains(array.get(mSelectedItems.get(k)))) {
                            addFromTempList(index, i);
                            index = index + 1;
                        }
                    }
                }
                break;
        }
        filtr_type = false;
    }

    private void multiChoiceItems(AlertDialog.Builder builder, String[] string, final ArrayList<Integer> mSelectedItems) {
        mSelectedItems.clear();
        builder.setMultiChoiceItems(string, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }
                });
    }

    private void setTitle(AlertDialog.Builder builder, String string, Context context) {
        TextView title = new TextView(context);
        title.setText(string);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.rgb(0, 0, 0));
        title.setTextSize(23);
        builder.setCustomTitle(title);
    }

    void clearLists() {
        listItems.clear();
        listItemsTemp.clear();
        listMessageDetails.clear();
        listMessageDetailsTemp.clear();
        listTitleDetails.clear();
        listTitleDetailsTemp.clear();
        if (typeClass == 1) {
            positionsTemp.clear();
            positions.clear();
        }
        type.clear();
        typeTemp.clear();
    }

    View adapter(View view) {
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setTextColor(Color.WHITE);
        return view;
    }

    private void setPositiveB(AlertDialog.Builder builder, final ArrayList<Integer> mSelectedItems, final ArrayList<String> temp, final ArrayAdapter<String> adapter) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                adapter.clear();
                setFilter(mSelectedItems, temp);
                adapter.notifyDataSetChanged();
            }
        });
    }

    void filtr_number(Context context, String[] numbers, AlertDialog.Builder builder, final ArrayAdapter<String> adapter) {
        for (int j = 0; j < number_temp.size(); j++) {
            numbers[j] = number_temp.get(j);
        }
        setTitle(builder, "Numer:", context);
        multiChoiceItems(builder, numbers, mSelectedItems_nr);
        setPositiveB(builder, mSelectedItems_nr, number_temp, adapter);
        AlertDialog dialog = builder.create();
        dialog.show();
        filtr_type = false;
    }

    void filtr_date(Context context, String[] dates, AlertDialog.Builder builder, final ArrayAdapter<String> adapter) {
        for (int j = 0; j < date_temp.size(); j++) {
            dates[j] = date_temp.get(j);
        }
        setTitle(builder, "Data:", context);
        multiChoiceItems(builder, dates, mSelectedItems_date);
        setPositiveB(builder, mSelectedItems_date, date_temp, adapter);
        AlertDialog dialog = builder.create();
        dialog.show();
        filtr_type = false;
    }


    void filtr_type(String type, ArrayAdapter<String> dataAdapter) {
        int index;
        if (!filtr_type && !start) {
            listItemsForType.clear();
            listMessageDetailsForType.clear();
            listTitleDetailsForType.clear();
            listTypeForType.clear();
            if (typeClass == 1) listPositionsForType.clear();
            index = 0;
            for (int i = listItems.size() - 1; i >= 0; i--) {
                listItemsForType.add(index, listItems.get(i));
                listMessageDetailsForType.add(index, listMessageDetails.get(i));
                listTitleDetailsForType.add(index, listTitleDetails.get(i));
                listTypeForType.add(index, this.type.get(i));
                if (typeClass == 1) listPositionsForType.add(index, positions.get(i));
                index = index + 1;
            }
            filtr_type = true;
        }

        dataAdapter.clear();
        listItems.clear();
        listMessageDetails.clear();
        listTitleDetails.clear();
        if (typeClass == 1) positions.clear();
        this.type.clear();

        switch (typeClass) {
            case 1: //MESSAGES
                selectedMessages(type);
                break;
            case 2: //CONNECT
                selectedConnect(type);
                break;
        }
        dataAdapter.notifyDataSetChanged();
    }

    private void addFromListForType(int index, int i) {
        listItems.add(index, listItemsForType.get(i));
        listMessageDetails.add(index, listMessageDetailsForType.get(i));
        listTitleDetails.add(index, listTitleDetailsForType.get(i));
        if (typeClass == 1) positions.add(index, listPositionsForType.get(i));
        this.type.add(index, typeTemp.get(i));
    }

    private void addFromTempList(int index, int i) {
        listItems.add(index, listItemsTemp.get(i));
        listTitleDetails.add(index, listTitleDetailsTemp.get(i));
        listMessageDetails.add(index, listMessageDetailsTemp.get(i));
        if (typeClass == 1) positions.add(index, positionsTemp.get(i));
        type.add(index, typeTemp.get(i));
    }


    String getRequest(String url) {
        SpyAppRestClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody == null) {
                    return;
                }
                response = new String(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("onFailure");
                if (responseBody == null) {
                    return;
                }
                response = new String(responseBody);

            }
        });
        return response;
    }

    private void selectedMessages(String type) {
        switch (type) {
            case "wszystkie wiadomosci":
                selectedAll();
                break;
            case "wiadomosci przychodzace": //przychodzace wiadomosci
                selectedIncomingSms();
                break;
            case "wiadomosci wychodzace":
                selectedOutgoingSms();
                break;
        }
    }

    private void selectedConnect(String type) {
        switch (type) {
            case "wszystkie polaczenia":
                selectedAll();
                break;
            default:
                selectedRestCall(type);
                break;
        }
    }

    private void selectedAll() {
        int index = 0;
        if (!start) {
            for (int i = listTypeForType.size() - 1; i >= 0; i--) {
                addFromListForType(index, i);
                index = index + 1;
            }
        } else {
            sweep();
            if (listItems.size() != 0) start = false;
        }
    }

    private void selectedIncomingSms() {
        int index = 0;
        for (int i = listTypeForType.size() - 1; i >= 0; i--) {
            if (listTypeForType.get(i).equals("1")) {
                addFromListForType(index, i);
                index = index + 1;
            }
        }
    }

    private void selectedOutgoingSms() {
        int index = 0;
        for (int i = listTypeForType.size() - 1; i >= 0; i--) {
            if (listTypeForType.get(i).equals("2") || listTypeForType.get(i).equals("6")) {
                addFromListForType(index, i);
                index = index + 1;
            }
        }
    }

    private void selectedRestCall(String type) {
        int index = 0;
        for (int i = listTypeForType.size() - 1; i >= 0; i--) {
            if (listTypeForType.get(i).equals(type.substring(11, type.length()))) {
                addFromListForType(index, i);
                index = index + 1;
            }
        }
    }
}
