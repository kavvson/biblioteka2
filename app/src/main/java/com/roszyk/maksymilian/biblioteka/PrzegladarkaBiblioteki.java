package com.roszyk.maksymilian.biblioteka;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kavvson on 13 sty 2018.
 */

public class PrzegladarkaBiblioteki extends AppCompatActivity {
    private int idBiblioteki; // zmienna do przechowania id wybranej biblioteki potrzebny do dalszej obslugi zdarzen
    private String nazwaBiblioteki; //  jw
    DBHandler Bazadanych;
    private ListView ListViewKsiazki;
    private ListView ListViewFilmy;
    Map<Integer, Integer> mapaKsiazek = new HashMap<>(); // przechowuje pozycje z listy oraz id ksiazki
    Map<Integer, Integer> mapaFilmow = new HashMap<>(); // przechowuje pozycje z listy oraz id filmu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bazadanych = new DBHandler(this);
        Intent intent = getIntent();
        idBiblioteki = intent.getIntExtra("id", 0); // odbieramy parametr po wybraniu biblioteki
        nazwaBiblioteki = intent.getStringExtra("nazwa"); // odbieramy parametr po wybraniu biblioteki
        setTitle("biblioteka - " + nazwaBiblioteki); // zmiana tytulu toolbara

        setContentView(R.layout.przegladarka_biblioteki); // renderuj widok

        TextView bh = (TextView) findViewById(R.id.podglad_biblioteki_header);
        bh.setText(nazwaBiblioteki);// zmiana naglowka na nazwe biblioteki


        TextView filmy_label = (TextView) findViewById(R.id.filmy_label);
        filmy_label.setText("Łącznie filmów " + String.valueOf(Bazadanych.liczbaFilmow(idBiblioteki))); // zmiana naglowka na nazwe biblioteki

        TextView ksiazki_label = (TextView) findViewById(R.id.ksiazki_label);
        ksiazki_label.setText("Łącznie książek " + String.valueOf(Bazadanych.liczbaKsiazek(idBiblioteki))); // zmiana naglowka na nazwe biblioteki


        ListViewKsiazki = (ListView) findViewById(R.id.bookList); // TODO: zmiana nazwy
        ListViewFilmy = (ListView) findViewById(R.id.filmList);// TODO: zmiana nazwy

        listaKsiazek(); // uzupelnia liste ksiazkami
        listaFilmow(); // uzupelnia liste filmow


        Button btn_dodajKsiazke = (Button) findViewById(R.id.dodajKsiazke);
        Button btn_dodajFilm = (Button) findViewById(R.id.dodajFilm);
        Button usunBiblioteke = (Button) findViewById(R.id.usunBiblioteke);
        btn_dodajKsiazke.setOnClickListener(new PrzegladarkaBiblioteki.PrzejdzDoDodawaniaKsiazki());
        btn_dodajFilm.setOnClickListener(new PrzegladarkaBiblioteki.PrzejdzDoDodawaniaFilmu());
        usunBiblioteke.setOnClickListener(new PrzegladarkaBiblioteki.UsunBiblioteke());


    }

    private void listaFilmow() {
        // przechowujemy liste filmow
        ArrayList<String> listafilmow = new ArrayList<String>();
        // pobieramy liste ksiazek biblioteki idBiblioteki, id pozyskany z  intent.getIntExtra, ktory to zostal przekazany z wybrania biblioteki
        Cursor data = Bazadanych.getFilmsFromLibrary(idBiblioteki); // sql result - iterator

        int list_item = 0;

        while (data.moveToNext()) {
            int czyObejrzany = 0;
            String obejrzany = "";
            czyObejrzany = Integer.valueOf(data.getString(8)); // trzeba zmienic na integer
            if (czyObejrzany == 1) {
                obejrzany = " - Obejrzany";
            }
            listafilmow.add(data.getString(2) + " - " + data.getString(1) + " "+obejrzany); // dodaj do listy ksiazek format autor - tytul

            // tworzyny mape -pozycja,film- pierwsza wartosc to pozycja w liscie ListView, a druga wartosc to id filmu
            // inaczej nie da sie pozyskac id filmu poniewaz elementy w liscie maja swoje numery
            mapaFilmow.put(list_item, Integer.valueOf(data.getString(0)));
            list_item++;
        }

        // tworzymy adapter, zrodlo ktore przechowuje liste filmow
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listafilmow);
        // ustawiamy adapter
        ListViewFilmy.setAdapter(adapter);
        // tworzymy event, po nacisnieciu list item,
        ListViewFilmy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // po nacisnieciu pobierz id naszej ksiazki
                // id jest pobierane z mapy
                // int i element listy
                // .get(i) zwraca id z mapy odpowiadajacemu elementowi i
                // << jak w tablicy php $array[$id] zwraca wartosc >>
                int id = mapaFilmow.get(i) != null ? mapaFilmow.get(i) : -1; // jak nie ma id naszej ksiazki w liscie zwracamy -1, nie odnaleziono

                Log.i("LISTA FILMOW", nazwaBiblioteki + " Wcisnieto pozycje film :: id :: " + String.valueOf(id) + " w bibliotece :: id :: " + idBiblioteki);
                if(id > -1){
                    Intent podgladFilmu = new Intent(PrzegladarkaBiblioteki.this, PodgladFilmu.class);
                    podgladFilmu.putExtra("id",id);
                    podgladFilmu.putExtra("idBiblioteki",idBiblioteki);
                    podgladFilmu.putExtra("nazwa",nazwaBiblioteki);
                    startActivity(podgladFilmu);
                }
            }
        });

    }

    private void listaKsiazek() {
        // przechowujemy liste ksiazek
        ArrayList<String> listaksiazek = new ArrayList<String>();
        // pobieramy liste ksiazek biblioteki idBiblioteki, id pozyskany z  intent.getIntExtra, ktory to zostal przekazany z wybrania biblioteki
        Cursor data = Bazadanych.getbookFromLibrary(idBiblioteki); // sql result - iterator


        int list_item = 0;
        while (data.moveToNext()) {
            int czyPrzeczytana = 0;
            String przeczytana = "";
            czyPrzeczytana = Integer.valueOf(data.getString(8)); // trzeba zmienic na integer
            if (czyPrzeczytana == 1) {
                przeczytana = " - Przeczytana";
            }
            listaksiazek.add(data.getString(2) + " - " + data.getString(1) + " "+przeczytana); // dodaj do listy ksiazek format autor - tytul
            // tworzyny mape -pozycja,ksiazka- pierwsza wartosc to pozycja w liscie ListView, a druga wartosc to id ksiazki
            // inaczej nie da sie pozyskac id ksiazki poniewaz elementy w liscie maja swoje numery
            mapaKsiazek.put(list_item, Integer.valueOf(data.getString(0)));
            list_item++;
        }

        // tworzymy adapter, zrodlo ktore przechowuje liste ksiazek
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaksiazek);
        // ustawiamy adapter
        ListViewKsiazki.setAdapter(adapter);
        // tworzymy event, po nacisnieciu list item,
        ListViewKsiazki.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // po nacisnieciu pobierz id naszej ksiazki
                // id jest pobierane z mapy
                // int i element listy
                // .get(i) zwraca id z mapy odpowiadajacemu elementowi i
                // << jak w tablicy php $array[$id] zwraca wartosc >>
                int id = mapaKsiazek.get(i) != null ? mapaKsiazek.get(i) : -1; // jak nie ma id naszej ksiazki w liscie zwracamy -1, nie odnaleziono

                Log.i("LISTA KSIAZEK", nazwaBiblioteki + " Wcisnieto pozycje ksiazki :: id :: " + String.valueOf(id) + " w bibliotece :: id :: " + idBiblioteki);
                if(id > -1){
                    Intent podgladKsiazki = new Intent(PrzegladarkaBiblioteki.this, PodgladKsiazki.class);
                    podgladKsiazki.putExtra("id",id);
                    podgladKsiazki.putExtra("idBiblioteki",idBiblioteki);
                    podgladKsiazki.putExtra("nazwa",nazwaBiblioteki);
                    startActivity(podgladKsiazki);
                }
            }
        });

    }


    // event po nacisnieciu przycisku, przejdz do widoku dodawania ksiazki
    private class PrzejdzDoDodawaniaKsiazki implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // activity handler przekazuje parametry
            // << jak np GET ?id=idBiblioteki w php>>
            //  DodajKsiazke.class odbiera ten parametr
            Intent intent = new Intent(PrzegladarkaBiblioteki.this, DodajKsiazke.class);
            intent.putExtra("id", idBiblioteki);
            intent.putExtra("nazwa", nazwaBiblioteki);
            startActivity(intent);
        }
    }

    private class UsunBiblioteke implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // usuwanie
            Bazadanych.usunBiblioteke(idBiblioteki);

            startActivity(new Intent(PrzegladarkaBiblioteki.this,biblioteka.class));

        }
    }

    private class PrzejdzDoDodawaniaFilmu implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // activity handler przekazuje parametry
            // << jak np GET ?id=idBiblioteki w php>>
            //  DodajKsiazke.class odbiera ten parametr
            Intent intent = new Intent(PrzegladarkaBiblioteki.this, DodajFilm.class);
            intent.putExtra("id", idBiblioteki);
            intent.putExtra("nazwa", nazwaBiblioteki);
            startActivity(intent);
            Log.i("LISTA KSIAZEK", "Przejscie do dodawania filmow - parametry " + idBiblioteki + " " + nazwaBiblioteki);
        }
    }
}
