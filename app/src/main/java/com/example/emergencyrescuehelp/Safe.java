package com.example.emergencyrescuehelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;

public class Safe extends AppCompatActivity {

    String[] contact = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);

        SharedPreferences sp = getSharedPreferences("Contacts",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        contact[0]  = sp.getString("contact1","");
        contact[1]  = sp.getString("contact2","");
        contact[2]  = sp.getString("contact3","");
        contact[3]  = sp.getString("contact4","");
        contact[4]  = sp.getString("contact5","");

        SmsManager smsManager = SmsManager.getDefault();
        String message = "I am safe now. Thank you!";

        ed.putString("isEmergency","n");
        ed.apply();

        for(int i=0; i<contact.length; i++)
        {
            if(contact[i] != "")
            {
                smsManager.sendTextMessage(contact[i],null,message,null,null);
            }
        }
    }
}