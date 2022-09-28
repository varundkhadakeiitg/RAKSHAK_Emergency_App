package com.example.emergencyrescuehelp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationListenerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.emergencyrescuehelp.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    public LocationManager locationManager;
    public LocationListener locationListener;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 30;
    private LatLng latLng;
    private Marker marker;
    private Boolean call = true;

    String[] contact = new String[5];
    String police;
    Boolean callpolice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Retrive contacts from Shared Preferences:
        SharedPreferences sp = getSharedPreferences("Contacts",MODE_PRIVATE);
        contact[0]  = sp.getString("contact1","");
        contact[1]  = sp.getString("contact2","");
        contact[2]  = sp.getString("contact3","");
        contact[3]  = sp.getString("contact4","");
        contact[4]  = sp.getString("contact5","");
        police = sp.getString("police","100");
        callpolice = sp.getBoolean("callPolice",false);

        //Update current status as emergency:
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("isEmergency","y");
        ed.apply();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng base = new LatLng(19.22, 73);
        marker = mMap.addMarker(new MarkerOptions().position(base));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    marker.remove();
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //SMS Service:
                String myLatitude = String.valueOf(location.getLatitude());
                String myLongitude = String.valueOf(location.getLongitude());
                String message = "EMERGENCY!!\nI am in danger. Please reach out to me at the following location.\n" + "https://www.google.com/maps/search/?api=1&query=" + myLatitude + "," + myLongitude;
                SmsManager smsManager = SmsManager.getDefault();

                for(int i=0; i<contact.length; i++)
                {
                    if(contact[i] != "")
                    {
                        smsManager.sendTextMessage(contact[i],null,message,null,null);
                    }
                }

                //Phone call ka code
                if(callpolice && call)
                {
                    call = false;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+police));
                    startActivity(intent);
                }
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        locationManager.removeUpdates(locationListener);
        locationManager = null;
        finish();
    }
}