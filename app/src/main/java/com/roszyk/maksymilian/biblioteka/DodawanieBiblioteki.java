package com.roszyk.maksymilian.biblioteka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DodawanieBiblioteki extends AppCompatActivity {

    private static final int MAX_NAZWA_LENGTH = 20;
    private EditText input_biblioteka_nazwa;
    private TextInputLayout layout_input_biblioteka_nazwa;
    private Button dodaj;

    DBHandler Bazadancyh;

    /*
        Tworzenie widoku
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodawanie_biblioteki);

        /**
         Opracowane na podstawie
         https://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
         */

        layout_input_biblioteka_nazwa = (TextInputLayout) findViewById(R.id.layout_input_biblioteka_nazwa);
        input_biblioteka_nazwa = (EditText) findViewById(R.id.input_biblioteka_nazwa);
        dodaj = (Button) findViewById(R.id.do_programu); // id przycisku Dodaj

        input_biblioteka_nazwa.addTextChangedListener(new MyTextWatcher(input_biblioteka_nazwa)); // nasłuchiwanie zmian

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Walidacja_formularza();
            }
        });

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
        if (!poprawnaNazwa()) {
            return;
        }


        Bazadancyh = new DBHandler(this);

        DodajRekord(input_biblioteka_nazwa.getText().toString()); // dodaj rekord

        startActivity(new Intent(getApplicationContext(), biblioteka.class)); // przenies do listy po dodaniu
    }

    /**
     * Reguła walidacji pola Nazwa
     */
    private boolean poprawnaNazwa() {

        if (input_biblioteka_nazwa.getText().toString().trim().isEmpty()) {
            layout_input_biblioteka_nazwa.setError(getString(R.string.blad_pole_puste_nazwaBiblioteki));
            requestFocus(input_biblioteka_nazwa);
            return false;
        }
        if (input_biblioteka_nazwa.getText().toString().trim().length() > MAX_NAZWA_LENGTH) {
            String a = new StringBuilder()
                    .append(getString(R.string.blad_dlugosc_nazwaBiblioteki))
                    .append(" " + MAX_NAZWA_LENGTH + " znaków")
                    .toString(); // budowa komunikatu z błędem
            layout_input_biblioteka_nazwa.setError(a);
            requestFocus(input_biblioteka_nazwa);
            return false;
        }
        layout_input_biblioteka_nazwa.setErrorEnabled(false);


        return true;
    }

    public void DodajRekord(String newEntry) {
        boolean insertData = Bazadancyh.addDataLibrary(newEntry);

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
                case R.id.input_biblioteka_nazwa:
                    poprawnaNazwa();
                    break;
            }
        }
    }


}
