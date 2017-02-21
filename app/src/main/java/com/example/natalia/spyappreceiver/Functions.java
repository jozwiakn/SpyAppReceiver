package com.example.natalia.spyappreceiver;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Natalia on 29.12.2016.
 */
public class Functions {
    public ArrayList<String> listItems;
    public ArrayList<String> listItemsTemp; //czas i numer na liscie
    public ArrayList<String> listMessageDetails;
    public ArrayList<String> listMessageDetailsTemp; //tresc wiadomosci (szczegoly)
    public ArrayList<String> listTitleDetails;
    public ArrayList<String> listTitleDetailsTemp; //czas i numer (szczegoly)

    public ArrayList<Integer> mSelectedItems_nr; //zaznaczanie Dialog numery
    public ArrayList<Integer> mSelectedItems_date; //zaznaczanie Dialog daty

    public ArrayList<String> number;
    public ArrayList<String> number_temp; //lista wszystkich numerow
    public ArrayList<String> listNumberTemp;

    public ArrayList<String> date;
    public ArrayList<String> date_temp; //lista dat
    public ArrayList<String> listDateTemp;

//    public ArrayList<String> state; //sms type
    public ArrayList<String> positions; //sms positions

    public ArrayList<String> type; //polaczenie i wiadomosci typ
    public ArrayList<String> typeTemp; //polaczenie i wiadomosci typ

//    boolean filtr_number;
//    boolean filtr_date;

    int typeClass; //1 - messages, 2 - call

    public Functions(int type) {
        this.typeClass = type;
//        filtr_number = false;
//        filtr_date = false;
    }

    public void init() {
        listItems = new ArrayList<>();
        listItemsTemp = new ArrayList<>();

        listMessageDetails = new ArrayList<>();
        listMessageDetailsTemp = new ArrayList<>();

        listTitleDetails = new ArrayList<>();
        listTitleDetailsTemp = new ArrayList<>();

        number = new ArrayList<>();
        number_temp = new ArrayList<>();
        listNumberTemp = new ArrayList<>();

        date = new ArrayList<>();
        date_temp = new ArrayList<>();
        listDateTemp = new ArrayList<>();

        mSelectedItems_nr = new ArrayList<>();
        mSelectedItems_date = new ArrayList<>();

        if (typeClass == 1) {
//            state = new ArrayList<>(); //for messages - sent or receiver
            positions = new ArrayList<>(); //for messages - position of cursor
        }

//        if (typeClass == 2) {
            type = new ArrayList<>();
            typeTemp = new ArrayList<>();
            filtr_type = false;
            start = true;
//        }

    }

    boolean start;

    public void AddToArray(ArrayList<String> array, ArrayList<String> arrayTemp) {
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

    public void sweep() {
        if (typeClass == 2) {
            int index = 0;
            for (int i = listItemsTemp.size() - 1; i >= 0; i--) {
                addFromTempList(index, i);
//                listItems.add(index, listItemsTemp.get(i));
//                listTitleDetails.add(index, listTitleDetailsTemp.get(i));
//                listMessageDetails.add(index, listMessageDetailsTemp.get(i));
//                type.add(index, typeTemp.get(i));
                index = index + 1;
            }
        } else if (typeClass == 1 && positions.size()!=0) {
            int index = 0;
            int maximum = Integer.parseInt(Collections.max(positions));
            int minimum = Integer.parseInt(Collections.min(positions));

            for (int j = maximum; j >= minimum; j--) {
                for (int i = positions.size() - 1; i >= 0; i--) {
                    if (positions.get(i).equals(Integer.toString(j))) {
                        addFromTempList(index, i);
//                        listItems.add(index, listItemsTemp.get(i));
//                        listTitleDetails.add(index, listTitleDetailsTemp.get(i));
//                        listMessageDetails.add(index, listMessageDetailsTemp.get(i));
////                        number.add(index, listNumberTemp.get(i));
////                        date.add(index, listDateTemp.get(i));
//                        type.add(index, typeTemp.get(i));
                        index = index + 1;
                    }
                }
            }
        }
    }

    public void onListItemDialog(int pos, Context context) {
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

    public void setFilter(ArrayList<Integer> mSelectedItems, ArrayList<String> array) {
        listItems.clear();
        listMessageDetails.clear();
        listTitleDetails.clear();
//        number.clear();
//        date.clear();
        if (typeClass == 2) type.clear();
        int index = 0;

        for (int k = 0; k < mSelectedItems.size(); k++) {
            for (int i = listItemsTemp.size() - 1; i >= 0; i--) {
                if (listItemsTemp.get(i).contains(array.get(mSelectedItems.get(k)))) {
                    addFromTempList(index, i);
//                    listItems.add(index, listItemsTemp.get(i));
//                    listMessageDetails.add(index, listMessageDetailsTemp.get(i));
//                    listTitleDetails.add(index, listTitleDetailsTemp.get(i));
////                    number.add(index, listNumberTemp.get(i));
////                    date.add(index, listDateTemp.get(i));
////                    if (typeClass == 2)
//                        type.add(index, typeTemp.get(i));
                    index = index + 1;
                }
            }
        }
        filtr_type = false;
        Log.i("SET FILTER", "FILTR TYPE = FALSE");
//        if ((what.equals("number") && filtr_date) || (what.equals("date") && filtr_number)) {
//            if (filtr_date) filtr_date = false;
//            if (filtr_number) filtr_number = false;
//            listItemsTemp.clear();
//            listMessageDetailsTemp.clear();
//            listTitleDetailsTemp.clear();
//            listNumberTemp.clear();
//            listDateTemp.clear();
//            if (typeClass == 2) typeTemp.clear();
//            for (int i = 0; i < listItems.size(); i++) {
//                listItemsTemp.add(i, listItems.get(i));
//                listMessageDetailsTemp.add(i, listMessageDetails.get(i));
//                listTitleDetailsTemp.add(i, listTitleDetails.get(i));
//                listNumberTemp.add(i, number.get(i));
//                listDateTemp.add(i, date.get(i));
//                if (typeClass == 2) typeTemp.add(i, type.get(i));
//            }
//            Log.i("LIST NUMBER TEMP SIZE", Integer.toString(listNumberTemp.size()));
//            Log.i("LIST DATE TEMP SIZE", Integer.toString(listDateTemp.size()));
//        }
    }

    public void multiChoiceItems(AlertDialog.Builder builder, String[] string, final ArrayList<Integer> mSelectedItems) {
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

    public void setTitle(AlertDialog.Builder builder, String string, Context context) {
        TextView title = new TextView(context);
        title.setText(string);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.rgb(0, 153, 204));
        title.setTextSize(23);
        builder.setCustomTitle(title);
    }

    public void clearLists() {
        listItems.clear();
        listItemsTemp.clear();
        listMessageDetails.clear();
        listMessageDetailsTemp.clear();
        listTitleDetails.clear();
        listTitleDetailsTemp.clear();
//        listNumberTemp.clear();
//        listDateTemp.clear();
//        number.clear();
//        date.clear();
        if (typeClass == 1) {
            positions.clear();
//            state.clear();
        }
//        if (typeClass == 2) {
            type.clear();
            typeTemp.clear();
//        }
    }

    public View adapter(View view) {
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setTextColor(Color.WHITE);
        return view;
    }

    public void setPositiveB(AlertDialog.Builder builder, final ArrayList<Integer> mSelectedItems, final ArrayList<String> temp, final ArrayAdapter<String> adapter) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                adapter.clear();
                Log.i("SET POS BUT", "1");
                setFilter(mSelectedItems, temp);
                Log.i("SET POS BUT", "2");
                adapter.notifyDataSetChanged();
                Log.i("SET POS BUT", "3");
            }
        });
    }

    public void filtr_number(Context context, String[] numbers, AlertDialog.Builder builder, final ArrayAdapter<String> adapter) {
        for (int j = 0; j < number_temp.size(); j++) {
            numbers[j] = number_temp.get(j);
        }
        setTitle(builder, "Numer:", context);
        multiChoiceItems(builder, numbers, mSelectedItems_nr);
        setPositiveB(builder, mSelectedItems_nr, number_temp, adapter);
        AlertDialog dialog = builder.create();
        dialog.show();

        filtr_type = false;
        Log.i("FILTR NUMBER", "FILTR TYPE = FALSE");
    }

    public void filtr_date(Context context, String[] dates, AlertDialog.Builder builder, final ArrayAdapter<String> adapter) {
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

    boolean filtr_type;
    ArrayList<String> listItemsForType = new ArrayList<>();
    ArrayList<String> listMessageDetailsForType = new ArrayList<>();
    ArrayList<String> listTitleDetailsForType = new ArrayList<>();
    ArrayList<String> listTypeForType = new ArrayList<>();

    public void filtr_type(String type, ArrayAdapter<String> dataAdapter) {
        int index;

        if(filtr_type) Log.i("FILTR TYPE", "FILTR TYPE");
        else Log.i("nie filtr type", "nie filtr type");

        if(start) Log.i("START", "START");
        else Log.i("nie START", "nie START");

        if (!filtr_type && !start) {
            listItemsForType.clear();
            listMessageDetailsForType.clear();
            listTitleDetailsForType.clear();
            listTypeForType.clear();
            Log.i("! filtr and ! start", "nie filtr i nie start");
            Log.i("LIST ITEMS SIZE", Integer.toString(listItems.size()));
            Log.i("LIST TYPE SIZE", Integer.toString(this.type.size()));
            index = 0;
            for (int i = listItems.size() - 1; i >=0; i--) {
                listItemsForType.add(index, listItems.get(i));
                listMessageDetailsForType.add(index, listMessageDetails.get(i));
                listTitleDetailsForType.add(index, listTitleDetails.get(i));
                listTypeForType.add(index, this.type.get(i));
                index = index + 1;
            }
            filtr_type = true;
            Log.i("FILTR TYPE", "FILTR TYPE = TRUE");
        }

        dataAdapter.clear();
        listItems.clear();
        listMessageDetails.clear();
        listTitleDetails.clear();
//        number.clear();
//        date.clear();
        this.type.clear();

        index = 0;
        switch(typeClass){
            case 1: //WIADOMOSCI
                switch(type) {
                    case "wszystkie wiadomosci":
                        if (!start) {
                            for (int i = listTypeForType.size() - 1; i >= 0; i--) {
                                addFromListForType(index, i);
                                index = index + 1;
                            }
                        } else {
                            Log.i("ALL", "2");
                            sweep();
                            if (listItems.size() != 0) start = false;
                        }
                        break;
                    case "wiadomosci przychodzace": //przychodzace wiadomosci
                        for (int i = listTypeForType.size() - 1; i >= 0; i--) {
                            if (listTypeForType.get(i).equals("1")) {
                                addFromListForType(index, i);
                                index = index + 1;
                            }
                        }
                        break;
                    case "wiadomosci wychodzace":
                        for (int i = listTypeForType.size() - 1; i >= 0; i--) {
                            if (listTypeForType.get(i).equals("2") || listTypeForType.get(i).equals("6")) {
                                addFromListForType(index, i);
                                index = index + 1;
                            }
                        }
                        break;
                }
            case 2: //POLACZENIA
                switch (type){
                    case "wszystkie polaczenia":
                        Log.i("ALL", "ALL");
                        if(!start) {
                            Log.i("ALL", "1");
                            for (int i = listTypeForType.size() - 1; i >= 0; i--) {
                                addFromListForType(index, i);
                                index = index + 1;
                            }
                        }
                        else{
                            Log.i("ALL", "2");
                            sweep();
                            if(listItems.size()!=0) start = false;
                        }
                        break;
                    default:
                        for (int i = listTypeForType.size() - 1; i >= 0; i--) {
                            if (listTypeForType.get(i).equals(type.substring(11, type.length()))) {
                                addFromListForType(index, i);
                                index = index + 1;
                            }
                        }
                        break;
                }
//                if (!type.equals("wszystkie polaczenia")) {
//                    Log.i("nie wszystkie", "nie wszystkie");
//                    for (int i = listTypeForType.size() - 1; i >= 0; i--) {
//                        if (listTypeForType.get(i).equals(type.substring(11, type.length()))) {
//                            addFromListForType(index, i);
//                            index = index + 1;
//                        }
//                    }
//                } else if (type.equals("wszystkie polaczenia")) {
//                    Log.i("ALL", "ALL");
//                    if(!start) {
//                        Log.i("ALL", "1");
//                        for (int i = listTypeForType.size() - 1; i >= 0; i--) {
//                            addFromListForType(index, i);
//                            index = index + 1;
//                        }
//                    }
//                    else{
//                        Log.i("ALL", "2");
//                        sweep();
//                        if(listItems.size()!=0) start = false;
//                    }
//                }
                break;
        }
        dataAdapter.notifyDataSetChanged();
    }

    public void addFromListForType(int index, int i){
        listItems.add(index, listItemsForType.get(i));
        listMessageDetails.add(index, listMessageDetailsForType.get(i));
        listTitleDetails.add(index, listTitleDetailsForType.get(i));
        this.type.add(index, typeTemp.get(i));
    }

    public void addFromTempList(int index, int i){
        listItems.add(index, listItemsTemp.get(i));
        listTitleDetails.add(index, listTitleDetailsTemp.get(i));
        listMessageDetails.add(index, listMessageDetailsTemp.get(i));
        type.add(index, typeTemp.get(i));
    }
}
