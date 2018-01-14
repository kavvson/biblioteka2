package com.roszyk.maksymilian.biblioteka;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.roszyk.maksymilian.biblioteka.Model.Ksiazka;


/**
 * Created by Kavvson on 14 sty 2018.
 */

public class PodgladKsiazki extends AppCompatActivity {
    private int idKsiazki; // zmienna do przechowania id ksiazki
    private int idBiblioteki; // zmienna do przechowania id wybranej biblioteki potrzebny do dalszej obslugi zdarzen
    private String nazwaBiblioteki; //  jw
    DBHandler Bazadanych;
    CheckBox mCheckBox;
    EditText tytul, autor, strony, rok, gatunek, opis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podglad_ksiazki);

        Bazadanych = new DBHandler(this);
        Intent intent = getIntent();

        idBiblioteki = intent.getIntExtra("idBiblioteki", 0); // odbieramy parametr po wybraniu biblioteki
        idKsiazki = intent.getIntExtra("id", 0); // odbieramy parametr po wybraniu biblioteki
        nazwaBiblioteki = intent.getStringExtra("nazwa"); // odbieramy parametr po wybraniu biblioteki

        Button btn_cofnij = (Button) findViewById(R.id.btn_cofnij);
        btn_cofnij.setOnClickListener(new PodgladKsiazki.CofnijDoBiblioteki());

        Button btn_usun = (Button) findViewById(R.id.btn_usun);
        btn_usun.setOnClickListener(new PodgladKsiazki.UsunKsiazke());


        Cursor data = Bazadanych.getbook(idKsiazki); // pobierz dane o ksiazce
        int czyPrzeczytana = 0;
        Ksiazka k = new Ksiazka();
        while (data.moveToNext()) {
            k.setTytul(data.getString(1));
            k.setAutor(data.getString(2));
            k.setStrony(Integer.valueOf(data.getString(3)));
            k.setRok(data.getString(4));
            k.setGatunek(data.getString(5));
            k.setOpis(data.getString(6));
            k.setFK_Biblioteka(Integer.valueOf(data.getString(7)));
            k.setPrzeczytana(Boolean.valueOf(data.getString(8))); // boolean

            czyPrzeczytana = Integer.valueOf(data.getString(8)); // trzeba zmienic na integer

        }

        // ustawianie tekstu do pol

        tytul = (EditText) findViewById(R.id.input_ksiazka_tytul);
        autor = (EditText) findViewById(R.id.input_ksiazka_autor);
        strony = (EditText) findViewById(R.id.input_ksiazka_strony);
        rok = (EditText) findViewById(R.id.input_ksiazka_datawydania);
        gatunek = (EditText) findViewById(R.id.input_ksiazka_gatunek);
        opis = (EditText) findViewById(R.id.input_ksiazka_opis);
        tytul.setText(k.getTytul());
        autor.setText(k.getAutor());
        strony.setText(String.valueOf(k.getStrony()));
        rok.setText(String.valueOf(k.getRok()));
        gatunek.setText(k.getGatunek());

        // jak nie ma opisu to jest null, nie ma potrzeby wyswietlania
        if (String.valueOf(k.getOpis()) != "null") {
            opis.setText(String.valueOf(k.getOpis()));
        }


        // Ustaw zaznaczenie z bazy
        mCheckBox = (CheckBox) findViewById(R.id.input_przeczytana);
        if (czyPrzeczytana == 0) {
            mCheckBox.setChecked(false); //to uncheck
        } else {
            mCheckBox.setChecked(true); //to check
        }

        chceckboxZdarzenie();


    }

    // Po nacisnieciu wykonaj update w bazie
    private void chceckboxZdarzenie() {


        mCheckBox = (CheckBox) findViewById(R.id.input_przeczytana);
        mCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int przeczytana = 0;
                if (mCheckBox.isChecked()) {
                    // true
                    przeczytana = 1;
                }

                // db update
                Bazadanych.ksiazkaPrzeczytana(idKsiazki, przeczytana);

                Log.i("Podglad ksiazki", "ustaw przeczytano idKsiazki :: " + idKsiazki + " na " + String.valueOf(mCheckBox.isChecked()));

            }
        });
    }


    private class CofnijDoBiblioteki implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // activity handler przekazuje parametry
            // << jak np GET ?id=idBiblioteki w php>>
            //  DodajKsiazke.class odbiera ten parametr
            Intent intent = new Intent(PodgladKsiazki.this, PrzegladarkaBiblioteki.class);
            intent.putExtra("id", idBiblioteki);
            intent.putExtra("nazwa", nazwaBiblioteki);
            startActivity(intent);
        }
    }

    private class UsunKsiazke implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // usuwanie
            Bazadanych.usunKsiazke(idKsiazki);

            // przenies do biblioteki
            Intent intent = new Intent(PodgladKsiazki.this, PrzegladarkaBiblioteki.class);
            intent.putExtra("id", idBiblioteki);
            intent.putExtra("nazwa", nazwaBiblioteki);
            startActivity(intent);
        }
    }

}
