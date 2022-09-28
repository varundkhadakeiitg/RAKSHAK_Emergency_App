package com.example.emergencyrescuehelp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Home extends AppCompatActivity {

    ImageView help;
    ImageView editContact;
    ImageView safe;

    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private Boolean isFineLocationPermissionGranted = false;
    private Boolean isSendSMSPermissionGranted = false;
    private Boolean isCallPhonePermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

        setContentView(R.layout.activity_home);

        help = findViewById(R.id.imageView);
        editContact = findViewById(R.id.imageView3);
        safe = findViewById(R.id.imageView2);

        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if(result.get(Manifest.permission.ACCESS_FINE_LOCATION != null))
                {
                    isFineLocationPermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if(result.get(Manifest.permission.SEND_SMS != null))
                {
                    isSendSMSPermissionGranted = result.get(Manifest.permission.SEND_SMS);
                }
                if(result.get(Manifest.permission.CALL_PHONE != null))
                {
                    isCallPhonePermissionGranted = result.get(Manifest.permission.CALL_PHONE);
                }
            }
        });
        requestPermission();

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(view);
            }
        });

        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2(view);
            }
        });

        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("Contacts",MODE_PRIVATE);
                if(Objects.equals(sp.getString("isEmergency", ""), "y"))
                {
                    openActivity3(view);
                }
            }
        });
    }

    public void openActivity(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openActivity2(View v){
        Intent intent = new Intent(this, Contacts.class);
        startActivity(intent);
    }
    public void openActivity3(View v){
        Intent intent = new Intent(this, Safe.class);
        startActivity(intent);
    }

    private void requestPermission() {
        isFineLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        isSendSMSPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        isCallPhonePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<String>();

        if(!isFineLocationPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(!isSendSMSPermissionGranted){
            permissionRequest.add(Manifest.permission.SEND_SMS);
        }
        if(!isCallPhonePermissionGranted){
            permissionRequest.add(Manifest.permission.CALL_PHONE);
        }

        if(!permissionRequest.isEmpty()){
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sp = getSharedPreferences("Contacts",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("isEmergency","n");
        ed.apply();
    }
}