package com.roszyk.maksymilian.biblioteka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DatabaseHelper";

    /*
        Tworzenie widoku
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button do_programu = (Button) findViewById(R.id.do_programu); // przycisk
        // do odkomentowanie jak dodajesz nowa tabele
        //   getBaseContext().deleteDatabase("people_table");


        do_programu.setOnClickListener(new PrzejdzDoProgramu());


    }

    private class PrzejdzDoProgramu implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Biblioteka.class);
            startActivity(intent);
        }
    }
}
