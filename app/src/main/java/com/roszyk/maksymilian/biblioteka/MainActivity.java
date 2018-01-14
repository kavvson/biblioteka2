package com.roszyk.maksymilian.biblioteka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /*
        Tworzenie widoku
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button do_programu = (Button) findViewById(R.id.do_programu); // przycisk


        do_programu.setOnClickListener(new PrzejdzDoProgramu()); // przejdz do programu


    }

    private class PrzejdzDoProgramu implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, biblioteka.class);
            startActivity(intent);
        }
    }
}
