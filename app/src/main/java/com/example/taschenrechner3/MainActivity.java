package com.example.taschenrechner3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


/**
 * @author Willi Hollatz
 * SMSB4, 17952
 * SoSe 2020
 */

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public TextView textView;
    StringBuilder sb = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button button0 = findViewById(R.id.button0);
        Button buttonPlus = findViewById(R.id.buttonPlus);
        Button buttonMinus = findViewById(R.id.buttonMinus);
        Button buttonMal = findViewById(R.id.buttonMal);
        Button buttonGeteilt = findViewById(R.id.buttonGeteilt);
        Button buttonPunkt = findViewById(R.id.buttonPunkt);
        Button buttonGleich = findViewById(R.id.buttonGleich);
        Button buttonLöschen = findViewById(R.id.buttonLöschen);
        Button buttonKlammerAuf = findViewById(R.id.buttonKlammerAuf);
        Button buttonKlammerZu = findViewById(R.id.buttonKlammerZu);
        Button buttonMore = findViewById(R.id.buttonMore);

        //registerForContextMenu(button1);

        textView = findViewById(R.id.textView);
        textViewAufNull();

        btnListener(button1,"1");
        btnListener(button2,"2");
        btnListener(button3,"3");
        btnListener(button4,"4");
        btnListener(button5,"5");
        btnListener(button6,"6");
        btnListener(button7,"7");
        btnListener(button8,"8");
        btnListener(button9,"9");
        btnListener(button0,"0");
        btnListener(buttonPlus,"+");
        btnListener(buttonMinus,"-");
        btnListener(buttonMal,"*");
        btnListener(buttonGeteilt,"/");
        btnListener(buttonPunkt,".");
        btnListener(buttonKlammerAuf,"(");
        btnListener(buttonKlammerZu,")");


        buttonLöschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backSpace(v);
            }
        });


        /**
         * Löscht den gesamten StringBuilder, wenn Backspace Taste gehalten wird.
         */
        buttonLöschen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteAll(v);
                return false;
            }
        });



        buttonGleich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                berechne();
            }
        });
/*
        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll(v);
            }
        });

 */
        /**
         * On-long-Click-Listener, wird die Taste 1 am Taschenrechner gehalten, wird zwischen Light- und Dark-Theme gewechselt.
         * Kleines Easteregg ;)
         *
         * Quelle: https://www.youtube.com/watch?v=QhGf8fGJM8U
         */
        SharedPreferences appSettingPrefs = getSharedPreferences("AppSettingPrefs",0 );
        final SharedPreferences.Editor sharedPrefsEdit = appSettingPrefs.edit();
        final Boolean NightModeIsOn = appSettingPrefs.getBoolean("NightMode", false);

        if(NightModeIsOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }



        button1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(
                        NightModeIsOn){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPrefsEdit.putBoolean("NightMode", false);
                    sharedPrefsEdit.apply();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPrefsEdit.putBoolean("NightMode", true);
                    sharedPrefsEdit.apply();
                }
                return false;
            }
        });

    }

    /**
    Erstellt einen einfachen OnClickListener für einen Button mit einer ausgabe auf dem Bildschirm.
     */
    public void btnListener(Button btn, final String string){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(string);
                aktualisiereTextView();
            }
        });
    }

    /**
    Folgende Methode ist Inspiriert durch die Methode des Kommilitonen Lukas Noack.
    Sie wurde angepasst/erweitert und ist im Grunde nicht mehr zu erkennen.
     */
    public void berechne(){
        try {
            Expression ergebnis = new ExpressionBuilder(sb.toString()).build();
            double erg = ergebnis.evaluate();
            String EndErgebnis = String.valueOf(erg);
            sb.setLength(0);
            sb.append(EndErgebnis);
            aktualisiereTextView();
        }catch (ArithmeticException e) {
            textView.setText("not defined");
        }catch (Exception e){
            textView.setText("Error");
        }
    }

    /**
     * Setzt den Inhalt des StringBuilder als Inhalt der Textview.
     */
    public void aktualisiereTextView(){
        textView.setText(sb);
    }

    public void textViewAufNull (){
        textView.setText("0");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("StringKeyForStringBuilder", sb.toString());
        savedInstanceState.putString("StringKeyForTextView",textView.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);


        String stringBuilderInstance = savedInstanceState.getString("StringKeyForStringBuilder");
        String textViewInstance = savedInstanceState.getString("StringKeyForTextView");

        if(stringBuilderInstance.equals(textViewInstance)){
            sb.append(stringBuilderInstance);
            aktualisiereTextView();
        }else{
            textView.setText("0");
        }

    }
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0 , "(");
        menu.add(0, v.getId(), 0 , ")");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle() == "("){
            sb.append("(");
            aktualisiereTextView();
        }if(item.getTitle() == ")"){
            sb.append(")");
            aktualisiereTextView();
        }else{
            return false;
        }
        return true;
    }

 */

    public void showMorePopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        popupMenu.setOnMenuItemClickListener(MainActivity.this);
        menuInflater.inflate(R.menu.more_popup_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    public void showKlammerPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        popupMenu.setOnMenuItemClickListener(MainActivity.this);
        menuInflater.inflate(R.menu.klammer_popup_menu, popupMenu.getMenu());
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sin_item:
                sb.append("sin(");
                aktualisiereTextView();
                return true;
            case R.id.cos_item:
                sb.append("cos(");
                aktualisiereTextView();
                return true;
            case R.id.tan_item:
                sb.append("tan(");
                aktualisiereTextView();
                return true;
            case R.id.wurzel_item:
                sb.append("sqrt(");
                aktualisiereTextView();
                return true;
            case R.id.KlammerAuf_item:
                sb.append("(");
                aktualisiereTextView();
                return true;
            case R.id.KlammerZu_item:
                sb.append(")");
                aktualisiereTextView();
                return true;
            default:
                return false;
        }
    }


    public void machSinus(View view) {
        sb.append("sin(");
        aktualisiereTextView();
    }

    public void machCosinus(View view) {
        sb.append("cos(");
        aktualisiereTextView();
    }

    public void machTangens(View view) {
        sb.append("tan(");
        aktualisiereTextView();
    }

    public void machWurzel(View view) {
        sb.append("sqrt(");
        aktualisiereTextView();
    }


    /**
     * Setzt die Länge des Stringbuilder auf 0, aktualisiert die Textview und setzt die Textview auf 0.
     *
     */
    public void deleteAll(View view){
        sb.setLength(0);
        aktualisiereTextView();
        textViewAufNull();
    }


    /**
     * Schneidet das letzte Zeichen des StringBuilder ab. Falls dieser die Länge 0 hat, wird die Textview auf 0 gesetzt.
     */
    public void backSpace(View view){
        if(sb.length() > 0){
            sb.setLength(sb.length()-1);
            aktualisiereTextView();
            if(sb.length() == 0){
                textViewAufNull();
            }
        }
    }
}
