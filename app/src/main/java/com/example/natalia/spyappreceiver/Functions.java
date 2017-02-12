package com.example.natalia.spyappreceiver;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
    public ArrayList<String> date;
    public ArrayList<String> date_temp; //lista dat


    public Functions(){
    }

    public void init(){
        listItems = new ArrayList<>();
        listItemsTemp = new ArrayList<>();

        listMessageDetails = new ArrayList<>();
        listMessageDetailsTemp = new ArrayList<>();

        listTitleDetails = new ArrayList<>();
        listTitleDetailsTemp = new ArrayList<>();

        number = new ArrayList<>();
        number_temp = new ArrayList<>();

        date = new ArrayList<>();
        date_temp = new ArrayList<>();

        mSelectedItems_nr = new ArrayList<>();
        mSelectedItems_date = new ArrayList<>();

    }

    public void AddToArray(ArrayList<String> array, ArrayList<String> arrayTemp) {
        int index = 0;
        int yes;
        for (int i = 0; i < array.size(); i++) {
            yes = 0;
            for (int j = 0; j < arrayTemp.size(); j++) {
                if (array.get(i).equals(arrayTemp.get(j))) {
                    yes = 1;
                }
            }
            if (yes == 0) {
                arrayTemp.add(index++, array.get(i));
            }
        }
    }

    public void sweep() {
        int index = 0;
        for (int i = listItemsTemp.size() - 1; i >= 0; i--) {
            listItems.add(index, listItemsTemp.get(i));
            listTitleDetails.add(index, listTitleDetailsTemp.get(i));
            listMessageDetails.add(index, listMessageDetailsTemp.get(i));
            index = index + 1;
        }
    }

    public void onListItemDialog(int pos, Context context){
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
        int index = 0;

        for (int k = 0; k < mSelectedItems.size(); k++) {
            for (int i = listItemsTemp.size() - 1; i >= 0; i--) {
                if (listItemsTemp.get(i).contains(array.get(mSelectedItems.get(k)))) {
                    listItems.add(index, listItemsTemp.get(i));
                    listMessageDetails.add(index, listMessageDetailsTemp.get(i));
                    listTitleDetails.add(index, listTitleDetailsTemp.get(i));
                    index = index + 1;
                }
            }
        }
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

    public void clearLists(){
        listItems.clear();
        listItemsTemp.clear();
        listMessageDetails.clear();
        listMessageDetailsTemp.clear();
        listTitleDetails.clear();
        listTitleDetailsTemp.clear();
    }

    public View adapter(View view){
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setTextColor(Color.WHITE);
        return view;
    }

    public void setPositiveB(AlertDialog.Builder builder, final ArrayList<Integer> mSelectedItems, final ArrayList<String> temp, final ArrayAdapter<String> adapter){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                adapter.clear();
                setFilter(mSelectedItems, temp);
                adapter.notifyDataSetChanged();
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
    }
}
