package com.example.emergencyrescuehelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Contacts extends AppCompatActivity {

    ImageView save;
    EditText tcontact1;
    EditText tcontact2;
    EditText tcontact3;
    EditText tcontact4;
    EditText tcontact5;
    EditText tpolice;
    CheckBox cbpolice;

    String c1="",c2="",c3="",c4="",c5="",p="";
    Boolean callpolice = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        save = findViewById(R.id.imageView4);
        tcontact1 = findViewById(R.id.editTextPhone1);
        tcontact2 = findViewById(R.id.editTextPhone2);
        tcontact3 = findViewById(R.id.editTextPhone3);
        tcontact4 = findViewById(R.id.editTextPhone4);
        tcontact5 = findViewById(R.id.editTextPhone5);
        tpolice = findViewById(R.id.editTextpolice);
        cbpolice = findViewById(R.id.checkBox);

        SharedPreferences sp2 = getSharedPreferences("Contacts",MODE_PRIVATE);
        tcontact1.setText(sp2.getString("contact1",""));
        tcontact2.setText(sp2.getString("contact2",""));
        tcontact3.setText(sp2.getString("contact3",""));
        tcontact4.setText(sp2.getString("contact4",""));
        tcontact5.setText(sp2.getString("contact5",""));
        tpolice.setText(sp2.getString("police","100"));
        cbpolice.setChecked(sp2.getBoolean("callPolice",true));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1 = tcontact1.getText().toString();
                c2 = tcontact2.getText().toString();
                c3 = tcontact3.getText().toString();
                c4 = tcontact4.getText().toString();
                c5 = tcontact5.getText().toString();
                p = tpolice.getText().toString();
                callpolice = cbpolice.isChecked();

                SharedPreferences sp = getSharedPreferences("Contacts",MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("contact1",c1);
                ed.putString("contact2",c2);
                ed.putString("contact3",c3);
                ed.putString("contact4",c4);
                ed.putString("contact5",c5);
                ed.putString("police",p);
                ed.putBoolean("callPolice", callpolice);
                ed.apply();
                Toast.makeText(Contacts.this, "Contacts Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}