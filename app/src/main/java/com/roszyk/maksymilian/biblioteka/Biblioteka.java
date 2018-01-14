package com.roszyk.maksymilian.biblioteka;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.roszyk.maksymilian.biblioteka.Landing_Page.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

/**
 * ExpandableListView opracowane na podstawie
 * https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */
public class biblioteka extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> nazwaBiblioteki;
    HashMap<String, List<String>> listDataChild;
    private static final String TAG = "DatabaseHelper";

    DBHandler Bazadanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
        Bazadanych = new DBHandler(this);


        /*
            Obsługa przycisku dodawania
         */
        Button dodajBiblioteke = (Button) findViewById(R.id.dodajBiblioteke);
        dodajBiblioteke.setOnClickListener(new PrzejdzDoDodawania());

        /*
            Koniec obsługi przycisku dodawania
         */
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, nazwaBiblioteki, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        nazwaBiblioteki.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        nazwaBiblioteki.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        /*
            Child interaction
         */
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Cursor data = Bazadanych.pobierz_id_na_podstawie_nazwy_biblioteki(nazwaBiblioteki.get(groupPosition));

                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }

                if (itemID == -1) {
                    Toast.makeText(getApplicationContext(), "Wystąpił błąd z otworzeniem " + nazwaBiblioteki.get(groupPosition), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), PrzegladarkaBiblioteki.class);
                    intent.putExtra("id", itemID);
                    intent.putExtra("nazwa", nazwaBiblioteki.get(groupPosition));
                    startActivity(intent);
                }

                return false;
            }
        });
    }

    private class PrzejdzDoDodawania implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(biblioteka.this, DodawanieBiblioteki.class);
            startActivity(intent);
        }
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        nazwaBiblioteki = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        Cursor data = Bazadanych.pobierzWszystkieBiblioteki();
        ArrayList<String> listData = new ArrayList<>();



       // boolean insertData = Bazadanych.addDataLibrary("test");

        while (data.moveToNext()) {
            nazwaBiblioteki.add(data.getString(1));
            List<String> szczegoly = new ArrayList<String>();
            szczegoly.add("Łącznie książek " + String.valueOf(Bazadanych.liczbaKsiazek(data.getInt(0)))); // suma jest zwracana jako int parametr to id biblioteki
            szczegoly.add("Łącznie filmów " + String.valueOf(Bazadanych.liczbaFilmow(data.getInt(0)))); // suma jest zwracana jako int  parametr to id biblioteki
            listDataChild.put(data.getString(1), szczegoly); // Header, Child data
        }


    }
}