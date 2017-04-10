package com.pfe.geolocalisation.hopitaux.maphot;

/**
 * Created by oussama on 09/04/17.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class Localisation extends FragmentActivity implements OnMapReadyCallback {
    GoogleApiClient mGoogleApiClient;
    GoogleMap map;

     double h_lat ;
     double h_lng ;
     double u_lat ;
     double u_lng ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localisation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//         user = new LatLng(Double.valueOf(u_lat),Double.valueOf(u_lng));


    }


    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Marker user_marker,hopital_marker;

        Bundle b = getIntent().getExtras();

        h_lat = b.getDouble("latitude");
        h_lng = b.getDouble("longitude");
        u_lat = b.getDouble("user_lat");
        u_lng = b.getDouble("user_lng");

         final LatLng hopital = new LatLng(h_lat,h_lng);
         final LatLng user = new LatLng(u_lat,u_lng);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
     //   LatLng center = new LatLng(29.048073, 1.688602);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(user));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo((float) 5.4));
        googleMap.setMyLocationEnabled(true);
   /*     Toast.makeText(Localisation.this, "Hop lat :"+h_lat+" | Hot lng :"+h_lng, Toast.LENGTH_SHORT).show();
        Toast.makeText(Localisation.this, "user lat :"+u_lat+" | User Lng :"+u_lng, Toast.LENGTH_SHORT).show(); */

        BitmapDescriptor icon_hopital = BitmapDescriptorFactory.fromResource(R.drawable.hospital);

        googleMap.addMarker(new MarkerOptions()
                .position(hopital)
                .title("Hopital")
                .snippet("Your destination")
                .icon(icon_hopital));

        BitmapDescriptor icon_user = BitmapDescriptorFactory.fromResource(R.drawable.user);
        googleMap.addMarker(new MarkerOptions()
                .position(user)
                .title("You are here")
                .icon(icon_user));

        Double distance = CalculationByDistance(user,hopital);
        Toast.makeText(this, "La distance entre vous et l'hopital sélectionné est :" +distance+" km" , Toast.LENGTH_LONG).show();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double distance = (double) Math.round(valueResult * 100) / 100;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return distance;
    }

}