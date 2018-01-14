package com.roszyk.maksymilian.biblioteka;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.roszyk.maksymilian.biblioteka.Model.Ksiazka;

/**
 * Created by Kavvson on 13 sty 2018.
 */

public class DodajKsiazke extends AppCompatActivity {

    DBHandler Bazadanych;
    private static final int MAX_AUTOR_LENGTH = 50;
    private static final int MAX_TYTUL_LENGTH = 50;
    private static final int MAX_STRONY_LENGTH = 5; // ze wzgledu ze dane sa trzymane w TEXT :: limit to XXXXX stron
    private static final int MAX_ROK_LENGTH = 4; // ze wzgledu ze dane sa trzymane w TEXT :: limit to XXXX rok, dalej jest walidacja roku

    private static final int MIN_ROK = 1900;
    private static final int MAX_ROK = 2999;

    private static final int MAX_GATUNEK_LENGTH = 50;
    private static final int MAX_OPIS_LENGTH = 50;

    private EditText input_ksiazka_autor,
            input_ksiazka_tytul,
            input_ksiazka_strony,
            input_ksiazka_datawydania,
            input_ksiazka_gatunek,
            input_ksiazka_opis;
    private TextInputLayout layout_input_ksiazka_autor,
            layout_input_ksiazka_tytul,
            layout_input_ksiazka_strony,
            layout_input_ksiazka_datawydania,
            layout_input_ksiazka_gatunek,
            layout_input_ksiazka_opis;
    private Button dodaj;
    private static final String TAG = "Dodawanie Ksiazki";
    private int idBiblioteki;
    private String nazwaBiblioteki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        idBiblioteki = intent.getIntExtra("id", 0); // odbieramy parametr po wybraniu biblioteki
        nazwaBiblioteki = intent.getStringExtra("nazwa"); // odbieramy parametr po wybraniu biblioteki

        setTitle("Dodawanie książki - " + nazwaBiblioteki); // zmiana tytulu toolbara
        setContentView(R.layout.dodaj_ksiazke);

        layout_input_ksiazka_autor = (TextInputLayout) findViewById(R.id.layout_input_ksiazka_autor);
        layout_input_ksiazka_tytul = (TextInputLayout) findViewById(R.id.layout_input_ksiazka_tytul);
        layout_input_ksiazka_strony = (TextInputLayout) findViewById(R.id.layout_input_ksiazka_strony);
        layout_input_ksiazka_datawydania = (TextInputLayout) findViewById(R.id.layout_input_ksiazka_datawydania);
        layout_input_ksiazka_gatunek = (TextInputLayout) findViewById(R.id.layout_input_ksiazka_gatunek);
        layout_input_ksiazka_opis = (TextInputLayout) findViewById(R.id.layout_input_ksiazka_opis);
        input_ksiazka_autor = (EditText) findViewById(R.id.input_ksiazka_autor);
        input_ksiazka_tytul = (EditText) findViewById(R.id.input_ksiazka_tytul);
        input_ksiazka_strony = (EditText) findViewById(R.id.input_ksiazka_strony);
        input_ksiazka_datawydania = (EditText) findViewById(R.id.input_ksiazka_datawydania);
        input_ksiazka_gatunek = (EditText) findViewById(R.id.input_ksiazka_gatunek);
        input_ksiazka_opis = (EditText) findViewById(R.id.input_ksiazka_opis);

        /**
         Opracowane na podstawie
         https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
         */

        // nasłuchiwanie zmian podczas pisania
        input_ksiazka_autor.addTextChangedListener(new DodajKsiazke.MyTextWatcher(input_ksiazka_autor));
        input_ksiazka_tytul.addTextChangedListener(new DodajKsiazke.MyTextWatcher(input_ksiazka_tytul));
        input_ksiazka_strony.addTextChangedListener(new DodajKsiazke.MyTextWatcher(input_ksiazka_strony));
        input_ksiazka_datawydania.addTextChangedListener(new DodajKsiazke.MyTextWatcher(input_ksiazka_datawydania));
        input_ksiazka_gatunek.addTextChangedListener(new DodajKsiazke.MyTextWatcher(input_ksiazka_gatunek));
        input_ksiazka_opis.addTextChangedListener(new DodajKsiazke.MyTextWatcher(input_ksiazka_opis));

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
     * Reguła walidacji pola Autor
     * walidatory :
     * Długość - MAX_AUTOR_LENGTH
     * Czy puste
     */
    private boolean Autor() {

        if (input_ksiazka_autor.getText().toString().trim().isEmpty()) {
            layout_input_ksiazka_autor.setError(getString(R.string.blad_pole_puste_kautor));
            requestFocus(input_ksiazka_autor);
            return false;
        }

        if (input_ksiazka_autor.getText().toString().trim().length() > MAX_AUTOR_LENGTH) {
            String a = new StringBuilder()
                    .append("Autor ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_AUTOR_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_ksiazka_autor.setError(a);
            requestFocus(input_ksiazka_autor);
            return false;
        }
        layout_input_ksiazka_autor.setErrorEnabled(false);


        return true;
    }

    /**
     * Reguła walidacji pola Tytul
     * walidatory :
     * Długość - MAX_TYTUL_LENGTH
     * Czy puste
     */
    private boolean Tytul() {

        if (input_ksiazka_tytul.getText().toString().trim().isEmpty()) {
            layout_input_ksiazka_tytul.setError(getString(R.string.blad_pole_puste_ktytul));
            requestFocus(input_ksiazka_tytul);
            return false;
        }

        if (input_ksiazka_tytul.getText().toString().trim().length() > MAX_TYTUL_LENGTH) {
            String a = new StringBuilder()
                    .append("Tytuł ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_TYTUL_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_ksiazka_tytul.setError(a);
            requestFocus(input_ksiazka_tytul);
            return false;
        }
        layout_input_ksiazka_tytul.setErrorEnabled(false);


        return true;
    }

    /**
     * Reguła walidacji pola Strony
     * walidatory :
     * Regexp - Czy są to liczby, pole ma ustawione android:inputType="number", ale dla pewności
     * Długość - MAX_STRONY_LENGTH
     * Czy puste
     */
    private boolean Strony() {

        /**
         * Regexp : https://stackoverflow.com/a/17854232/2513428
         */
        String regexStr = "^[0-9]*$";
        if (!input_ksiazka_strony.getText().toString().trim().matches(regexStr)) {
            layout_input_ksiazka_strony.setError(getString(R.string.blad_pole_liczba_kstrony));
            requestFocus(input_ksiazka_strony);
            return false;
        }

        if (input_ksiazka_strony.getText().toString().trim().isEmpty()) {
            layout_input_ksiazka_strony.setError(getString(R.string.blad_pole_puste_kstrony));
            requestFocus(input_ksiazka_strony);
            return false;
        }

        if (input_ksiazka_strony.getText().toString().trim().length() > MAX_STRONY_LENGTH) {
            String a = new StringBuilder()
                    .append("Liczba stron ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_STRONY_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_ksiazka_strony.setError(a);
            requestFocus(input_ksiazka_strony);
            return false;
        }
        layout_input_ksiazka_strony.setErrorEnabled(false);


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
        String pole = input_ksiazka_datawydania.getText().toString().trim();
        /**
         * Regexp : https://stackoverflow.com/a/17854232/2513428
         */
        String regexStr = "^[0-9]*$";
        if (!pole.matches(regexStr)) {
            layout_input_ksiazka_datawydania.setError(getString(R.string.blad_pole_liczba_kRok));
            requestFocus(input_ksiazka_datawydania);
            return false;
        }

        if (pole.isEmpty()) {
            layout_input_ksiazka_datawydania.setError(getString(R.string.blad_pole_puste_kRok));
            requestFocus(input_ksiazka_datawydania);
            return false;
        }

        if (Integer.valueOf(pole) > MAX_ROK || Integer.valueOf(pole) < MIN_ROK) {
            String a = new StringBuilder()
                    .append(getString(R.string.blad_pole_przedzial_kRok))
                    .append(" " + MIN_ROK + " - ")
                    .append(" " + MAX_ROK)
                    .toString(); // budowa komunikatu z błędem
            layout_input_ksiazka_datawydania.setError(a);
            requestFocus(input_ksiazka_datawydania);
            return false;
        }

        if (pole.length() != MAX_ROK_LENGTH) {
            String a = new StringBuilder()
                    .append("Rok wydania ")
                    .append(getString(R.string.blad_dokladna_dlugosc))
                    .append(" " + MAX_ROK_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_ksiazka_datawydania.setError(a);
            requestFocus(input_ksiazka_datawydania);
            return false;
        }
        layout_input_ksiazka_datawydania.setErrorEnabled(false);


        return true;
    }


    /**
     * Reguła walidacji pola Gatunek
     * walidatory :
     * Długość - MAX_GATUNEK_LENGTH
     * Czy puste
     */
    private boolean Gatunek() {

        if (input_ksiazka_gatunek.getText().toString().trim().isEmpty()) {
            layout_input_ksiazka_gatunek.setError(getString(R.string.blad_pole_puste_ktytul));
            requestFocus(input_ksiazka_gatunek);
            return false;
        }


        if (input_ksiazka_gatunek.getText().toString().trim().length() > MAX_GATUNEK_LENGTH) {
            String a = new StringBuilder()
                    .append("Gatunek ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_GATUNEK_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_ksiazka_gatunek.setError(a);
            requestFocus(input_ksiazka_gatunek);
            return false;
        }

        layout_input_ksiazka_gatunek.setErrorEnabled(false);


        return true;
    }

    /**
     * Reguła walidacji pola Opis
     * walidatory :
     * Długość - MAX_GATUNEK_LENGTH
     * opcjonalne
     */
    private boolean Opis() {

        if (input_ksiazka_opis.getText().toString().trim().length() > MAX_OPIS_LENGTH) {
            String a = new StringBuilder()
                    .append("Opis ")
                    .append(getString(R.string.blad_dlugosc))
                    .append(" " + MAX_OPIS_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_ksiazka_opis.setError(a);
            requestFocus(input_ksiazka_opis);
            return false;
        }

        layout_input_ksiazka_opis.setErrorEnabled(false);


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
        if (!Autor()) {
            return;
        }

        if (!Tytul()) {
            return;
        }

        if (!Strony()) {
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

        boolean czyPrzeczytana = ((CheckBox) findViewById(R.id.input_przeczytana)).isChecked();

        Log.i(TAG, String.valueOf(czyPrzeczytana));

        Ksiazka k = new Ksiazka();
        k.setAutor(input_ksiazka_autor.getText().toString().trim());
        k.setTytul(input_ksiazka_tytul.getText().toString().trim());
        k.setStrony(Integer.valueOf(input_ksiazka_strony.getText().toString().trim()));
        k.setRok(input_ksiazka_datawydania.getText().toString().trim());
        k.setGatunek(input_ksiazka_gatunek.getText().toString().trim());
        k.setPrzeczytana(czyPrzeczytana);
        k.setFK_Biblioteka(idBiblioteki);


        DodajRekord(k); // dodaj rekord, klasa ksiazka

        // przenies do biblioteki
        Intent intent = new Intent(getApplicationContext(), PrzegladarkaBiblioteki.class);
        intent.putExtra("id", idBiblioteki);
        intent.putExtra("nazwa", nazwaBiblioteki);
        startActivity(intent);
    }

    public void DodajRekord(Ksiazka k) {
        boolean insertData = Bazadanych.dodajKsiazke(k);

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
                case R.id.input_ksiazka_autor:
                    Autor();
                    break;
                case R.id.input_ksiazka_tytul:
                    Tytul();
                    break;
                case R.id.input_ksiazka_strony:
                    Strony();
                    break;
                case R.id.input_ksiazka_datawydania:
                    Rok();
                    break;
                case R.id.input_ksiazka_gatunek:
                    Gatunek();
                    break;
                case R.id.input_ksiazka_opis:
                    Opis();
                    break;
            }
        }
    }
}
