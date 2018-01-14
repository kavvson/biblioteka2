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

import com.roszyk.maksymilian.biblioteka.Model.Film;

/**
 * Created by Kavvson on 14 sty 2018.
 * podobna klasa do PodgladKsiazki
 */

public class PodgladFilmu extends AppCompatActivity {
    private int idFilmu; // zmienna do przechowania id filmu
    private int idBiblioteki; // zmienna do przechowania id wybranej biblioteki potrzebny do dalszej obslugi zdarzen
    private String nazwaBiblioteki; //  jw
    DBHandler Bazadanych;
    CheckBox mCheckBox;
    EditText tytul, rezyser, czas, rok, gatunek, opis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podglad_filmu);

        Bazadanych = new DBHandler(this);
        Intent intent = getIntent();

        idBiblioteki = intent.getIntExtra("idBiblioteki", 0); // odbieramy parametr po wybraniu biblioteki
        idFilmu = intent.getIntExtra("id", 0); // odbieramy parametr po wybraniu biblioteki
        nazwaBiblioteki = intent.getStringExtra("nazwa"); // odbieramy parametr po wybraniu biblioteki


        Button btn_confnijF = (Button) findViewById(R.id.btn_confnijF);
        btn_confnijF.setOnClickListener(new PodgladFilmu.CofnijDoBiblioteki());

        Button btn_usunFilm = (Button) findViewById(R.id.btn_usunFilm);
        btn_usunFilm.setOnClickListener(new PodgladFilmu.UsunFilm());


        Cursor data = Bazadanych.getFilm(idFilmu); // pobierz dane o ksiazce
        int czyObejrzany = 0;
        Film k = new Film();
        while (data.moveToNext()) {
            k.setTytul(data.getString(1));
            k.setRezyser(data.getString(2));
            k.setDlugosc(Integer.valueOf(data.getString(3)));
            k.setRok(data.getString(4));
            k.setGatunek(data.getString(5));
            k.setOpis(data.getString(6));
            k.setFK_Biblioteka(Integer.valueOf(data.getString(7)));
            k.setObejrzany(Boolean.valueOf(data.getString(8))); // boolean

            czyObejrzany = Integer.valueOf(data.getString(8)); // trzeba zmienic na integer
        }

        // Ustaw zaznaczenie z bazy
        mCheckBox = (CheckBox) findViewById(R.id.input_obejrzany);
        if (czyObejrzany == 0) {
            mCheckBox.setChecked(false);
        } else {
            mCheckBox.setChecked(true);
        }

        tytul = (EditText) findViewById(R.id.input_film_tytul);
        rezyser = (EditText) findViewById(R.id.input_film_rezyser);
        czas = (EditText) findViewById(R.id.input_film_dlugosc);
        rok = (EditText) findViewById(R.id.input_film_rok);
        gatunek = (EditText) findViewById(R.id.input_film_gatunek);
        opis = (EditText) findViewById(R.id.input_film_opis);
        tytul.setText(k.getTytul());
        rezyser.setText(k.getRezyser());
        czas.setText(String.valueOf(k.getDlugosc()));
        rok.setText(String.valueOf(k.getRok()));
        gatunek.setText(k.getGatunek());

        // jak nie ma opisu to jest null, nie ma potrzeby wyswietlania
        if (String.valueOf(k.getOpis()) != "null") {
            opis.setText(String.valueOf(k.getOpis()));
        }


        chceckboxZdarzenie();

    }

    // Po nacisnieciu wykonaj update w bazie
    private void chceckboxZdarzenie() {


        mCheckBox = (CheckBox) findViewById(R.id.input_obejrzany);
        mCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int obejrzany = 0;
                if (mCheckBox.isChecked()) {
                    // true
                    obejrzany = 1;
                }

                // db update
                Bazadanych.filmObejrzany(idFilmu, obejrzany);

                Log.i("Podglad filmu", "ustaw przeczytano idFilmu :: " + idFilmu + " na " + String.valueOf(mCheckBox.isChecked()));

            }
        });
    }


    private class CofnijDoBiblioteki implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // activity handler przekazuje parametry
            // << jak np GET ?id=idBiblioteki w php>>
            //  DodajKsiazke.class odbiera ten parametr
            Intent intent = new Intent(PodgladFilmu.this, PrzegladarkaBiblioteki.class);
            intent.putExtra("id", idBiblioteki);
            intent.putExtra("nazwa", nazwaBiblioteki);
            startActivity(intent);
        }
    }

    private class UsunFilm implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // usuwanie
            Bazadanych.usunFilm(idFilmu);

            // przenies do biblioteki
            Intent intent = new Intent(PodgladFilmu.this, PrzegladarkaBiblioteki.class);
            intent.putExtra("id", idBiblioteki);
            intent.putExtra("nazwa", nazwaBiblioteki);
            startActivity(intent);
        }
    }

}
