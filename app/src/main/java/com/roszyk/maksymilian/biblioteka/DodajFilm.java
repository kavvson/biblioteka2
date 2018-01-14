package com.roszyk.maksymilian.biblioteka;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.roszyk.maksymilian.biblioteka.Model.Film;


/**
 * Created by Kavvson on 13 sty 2018.
 */

public class DodajFilm extends AppCompatActivity {

    DBHandler Bazadanych;
    private static final int MAX_REZYSER_LENGTH = 50;
    private static final int MAX_TYTUL_LENGTH = 50;
    private static final int MAX_MINUT_LENGTH = 3; // ze wzgledu ze dane sa trzymane w TEXT :: limit to XXX minut
    private static final int MAX_ROK_LENGTH = 4; // ze wzgledu ze dane sa trzymane w TEXT :: limit to XXXX rok, dalej jest walidacja roku

    private static final int MIN_ROK = 1900;
    private static final int MAX_ROK = 2999;

    private static final int MAX_GATUNEK_LENGTH = 50;
    private static final int MAX_OPIS_LENGTH = 50;

    private EditText input_film_rezyser,
            input_film_tytul,
            input_film_dlugosc,
            input_film_rok,
            input_film_gatunek,
            input_film_opis;
    private TextInputLayout layout_input_film_rezyser,
            layout_input_film_tytul,
            layout_input_film_dlugosc,
            layout_input_film_rok,
            layout_input_film_gatunek,
            layout_input_film_opis;
    private Button dodaj;
    private static final String TAG = "Dodawanie Filmu";
    private int idBiblioteki;
    private String nazwaBiblioteki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        idBiblioteki = intent.getIntExtra("id", 0); // odbieramy parametr po wybraniu biblioteki
        nazwaBiblioteki = intent.getStringExtra("nazwa"); // odbieramy parametr po wybraniu biblioteki

        setTitle("Dodawanie filmu - " + nazwaBiblioteki); // zmiana tytulu toolbara
        setContentView(R.layout.dodaj_film);

        layout_input_film_rezyser = (TextInputLayout) findViewById(R.id.layout_input_film_rezyser);
        layout_input_film_tytul = (TextInputLayout) findViewById(R.id.layout_input_film_tytul);
        layout_input_film_dlugosc = (TextInputLayout) findViewById(R.id.layout_input_film_dlugosc);
        layout_input_film_rok = (TextInputLayout) findViewById(R.id.layout_input_film_rok);
        layout_input_film_gatunek = (TextInputLayout) findViewById(R.id.layout_input_film_gatunek);
        layout_input_film_opis = (TextInputLayout) findViewById(R.id.layout_input_film_opis);
        input_film_rezyser = (EditText) findViewById(R.id.input_film_rezyser);
        input_film_tytul = (EditText) findViewById(R.id.input_film_tytul);
        input_film_dlugosc = (EditText) findViewById(R.id.input_film_dlugosc);
        input_film_rok = (EditText) findViewById(R.id.input_film_rok);
        input_film_gatunek = (EditText) findViewById(R.id.input_film_gatunek);
        input_film_opis = (EditText) findViewById(R.id.input_film_opis);

        /**
         Opracowane na podstawie
         https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
         */

        // nasłuchiwanie zmian podczas pisania
        input_film_rezyser.addTextChangedListener(new DodajFilm.MyTextWatcher(input_film_rezyser));
        input_film_tytul.addTextChangedListener(new DodajFilm.MyTextWatcher(input_film_tytul));
        input_film_dlugosc.addTextChangedListener(new DodajFilm.MyTextWatcher(input_film_dlugosc));
        input_film_rok.addTextChangedListener(new DodajFilm.MyTextWatcher(input_film_rok));
        input_film_gatunek.addTextChangedListener(new DodajFilm.MyTextWatcher(input_film_gatunek));
        input_film_opis.addTextChangedListener(new DodajFilm.MyTextWatcher(input_film_opis));

        dodaj = (Button) findViewById(R.id.btndodaj_ksiazke);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Walidacja_formularza();
            }
        });

        Bazadanych = new DBHandler(this);
        //
        //  buttonActivity4.setOnClickListener(new index.StartNewActivity4());

        //Button buttonActivity5 = (Button)findViewById(R.id.dodajk);
        // buttonActivity5.setOnClickListener(new index.StartNewActivity5());

        // mListViewFilm = (ListView) findViewById(R.id.filmList);
        //  mListViewBook = (ListView) findViewById(R.id.bookList);
        //  mDatabaseHelper = new databasehelper(this);

        //populateListViewFilm();
        // populateListViewBook();
    }


    /**
     * Reguła walidacji pola Rezyser
     * walidatory :
     * Długość - MAX_REZYSER_LENGTH
     * Czy puste
     */
    private boolean Rezyser() {

        if (input_film_rezyser.getText().toString().trim().isEmpty()) {
            layout_input_film_rezyser.setError(getString(R.string.blad_pole_puste_kautor));
            requestFocus(input_film_rezyser);
            return false;
        }

        if (input_film_rezyser.getText().toString().trim().length() > MAX_REZYSER_LENGTH) {
            String a = new StringBuilder()
                    .append("Rezyser ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_REZYSER_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_film_rezyser.setError(a);
            requestFocus(input_film_rezyser);
            return false;
        }
        layout_input_film_rezyser.setErrorEnabled(false);


        return true;
    }

    /**
     * Reguła walidacji pola Tytul
     * walidatory :
     * Długość - MAX_TYTUL_LENGTH
     * Czy puste
     */
    private boolean Tytul() {

        if (input_film_tytul.getText().toString().trim().isEmpty()) {
            layout_input_film_tytul.setError(getString(R.string.blad_pole_puste_ktytul));
            requestFocus(input_film_tytul);
            return false;
        }

        if (input_film_tytul.getText().toString().trim().length() > MAX_TYTUL_LENGTH) {
            String a = new StringBuilder()
                    .append("Tytuł ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_TYTUL_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_film_tytul.setError(a);
            requestFocus(input_film_tytul);
            return false;
        }
        layout_input_film_tytul.setErrorEnabled(false);


        return true;
    }

    /**
     * Reguła walidacji pola Minuty
     * walidatory :
     * Regexp - Czy są to liczby, pole ma ustawione android:inputType="number", ale dla pewności
     * Długość - MAX_MINUT_LENGTH
     * Czy puste
     */
    private boolean Minuty() {

        /**
         * Regexp : https://stackoverflow.com/a/17854232/2513428
         */
        String regexStr = "^[0-9]*$";
        if (!input_film_dlugosc.getText().toString().trim().matches(regexStr)) {
            layout_input_film_dlugosc.setError(getString(R.string.blad_pole_liczba_kstrony));
            requestFocus(input_film_dlugosc);
            return false;
        }

        if (input_film_dlugosc.getText().toString().trim().isEmpty()) {
            layout_input_film_dlugosc.setError(getString(R.string.blad_pole_puste_kstrony));
            requestFocus(input_film_dlugosc);
            return false;
        }

        if (input_film_dlugosc.getText().toString().trim().length() > MAX_MINUT_LENGTH) {
            String a = new StringBuilder()
                    .append("Długość filmu ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_MINUT_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_film_dlugosc.setError(a);
            requestFocus(input_film_dlugosc);
            return false;
        }
        layout_input_film_dlugosc.setErrorEnabled(false);


        return true;
    }

    /**
     * Reguła walidacji pola Rok wydania  android:maxLength="4"
     * walidatory :
     * Regexp - Czy są to liczby, i czy te liczby są w przedziale MIN_ROK MAX_ROK
     * Długość - MAX_ROK_LENGTH
     * Czy puste
     */
    private boolean Rok() {
        String pole = input_film_rok.getText().toString().trim();
        /**
         * Regexp : https://stackoverflow.com/a/17854232/2513428
         */
        String regexStr = "^[0-9]*$";
        if (!pole.matches(regexStr)) {
            layout_input_film_rok.setError(getString(R.string.blad_pole_liczba_kRok));
            requestFocus(input_film_rok);
            return false;
        }

        if (pole.isEmpty()) {
            layout_input_film_rok.setError(getString(R.string.blad_pole_puste_kRok));
            requestFocus(input_film_rok);
            return false;
        }

        if (Integer.valueOf(pole) > MAX_ROK || Integer.valueOf(pole) < MIN_ROK) {
            String a = new StringBuilder()
                    .append(getString(R.string.blad_pole_przedzial_kRok))
                    .append(" " + MIN_ROK + " - ")
                    .append(" " + MAX_ROK)
                    .toString(); // budowa komunikatu z błędem
            layout_input_film_rok.setError(a);
            requestFocus(input_film_rok);
            return false;
        }

        if (pole.length() != MAX_ROK_LENGTH) {
            String a = new StringBuilder()
                    .append("Rok wydania ")
                    .append(getString(R.string.blad_dokladna_dlugosc))
                    .append(" " + MAX_ROK_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_film_rok.setError(a);
            requestFocus(input_film_rok);
            return false;
        }
        layout_input_film_rok.setErrorEnabled(false);


        return true;
    }


    /**
     * Reguła walidacji pola Gatunek
     * walidatory :
     * Długość - MAX_GATUNEK_LENGTH
     * Czy puste
     */
    private boolean Gatunek() {

        if (input_film_gatunek.getText().toString().trim().isEmpty()) {
            layout_input_film_gatunek.setError(getString(R.string.blad_pole_puste_ktytul));
            requestFocus(input_film_gatunek);
            return false;
        }


        if (input_film_gatunek.getText().toString().trim().length() > MAX_GATUNEK_LENGTH) {
            String a = new StringBuilder()
                    .append("Gatunek ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_GATUNEK_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_film_gatunek.setError(a);
            requestFocus(input_film_gatunek);
            return false;
        }

        layout_input_film_gatunek.setErrorEnabled(false);


        return true;
    }

    /**
     * Reguła walidacji pola Opis
     * walidatory :
     * Długość - MAX_GATUNEK_LENGTH
     * opcjonalne
     */
    private boolean Opis() {

        if (input_film_opis.getText().toString().trim().length() > MAX_OPIS_LENGTH) {
            String a = new StringBuilder()
                    .append("Opis ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_OPIS_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_film_opis.setError(a);
            requestFocus(input_film_opis);
            return false;
        }

        layout_input_film_opis.setErrorEnabled(false);


        return true;
    }

    /**
     * Walidacja
     * Podpowiedź :: android:hint="@string/bi_label_dodaj_biblioteke"
     * Komunikat błędu @string/blad_pole_puste_nazwaBiblioteki
     * <p>
     * W przypadku błedu zatrzymaj się
     * ! dodaj rekord i przenies do listy
     */
    private void Walidacja_formularza() {

        if (!Tytul()) {
            return;
        }

        if (!Rezyser()) {
            return;
        }

        if (!Minuty()) {
            return;
        }

        if (!Rok()) {
            return;
        }

        if (!Gatunek()) {
            return;
        }

        // opcjonalnie
        if (!Opis()) {
            return;
        }

        boolean czyObejrzany = ((CheckBox) findViewById(R.id.input_obejrzany)).isChecked();

        Log.i(TAG, String.valueOf(czyObejrzany));

        Film f = new Film();
        f.setRezyser(input_film_rezyser.getText().toString().trim());
        f.setTytul(input_film_tytul.getText().toString().trim());
        f.setDlugosc(Integer.valueOf(input_film_dlugosc.getText().toString().trim()));
        f.setRok(input_film_rok.getText().toString().trim());
        f.setGatunek(input_film_gatunek.getText().toString().trim());
        f.setObejrzany(czyObejrzany);
        f.setFK_Biblioteka(idBiblioteki);


        DodajRekord(f); // dodaj rekord, klasa ksiazka

        // przenies do biblioteki
        Intent intent = new Intent(getApplicationContext(), PrzegladarkaBiblioteki.class);
        intent.putExtra("id", idBiblioteki);
        intent.putExtra("nazwa", nazwaBiblioteki);
        startActivity(intent);
    }

    public void DodajRekord(Film f) {
        boolean insertData = Bazadanych.dodajFilm(f);

        if (insertData) {
            Toast.makeText(getApplicationContext(), "Dodano", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Blad", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Ustaw focus na pole z błędem
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Obsługa nasłuchiwania w walidacji
     */
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_film_tytul:
                    Tytul();
                    break;
                case R.id.input_film_rezyser:
                    Rezyser();
                    break;
                case R.id.input_film_dlugosc:
                    Minuty();
                    break;
                case R.id.input_film_rok:
                    Rok();
                    break;
                case R.id.input_film_gatunek:
                    Gatunek();
                    break;
                case R.id.input_film_opis:
                    Opis();
                    break;
            }
        }
    }
}
