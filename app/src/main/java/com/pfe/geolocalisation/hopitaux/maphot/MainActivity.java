package com.pfe.geolocalisation.hopitaux.maphot;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    SimpleCursorAdapter mAdapter;
    List<String> list ;

    String[] latitude ,longitude;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;
    GPSTracker gps;

    double user_lat ;
    double user_lng ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /****************************************************************************************************/

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

             user_lat = gps.getLatitude();
             user_lng = gps.getLongitude();


        }else{

            gps.showSettingsAlert();
        }



        /***************************************************************************************************/

        listView =(ListView)findViewById(R.id.lv);
        ArrayAdapter adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.hopitaux,
                        android.R.layout.simple_list_item_1
                );

         latitude = getResources().getStringArray(R.array.laitude);
         longitude = getResources().getStringArray(R.array.longitude);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Double lat = Double.valueOf(latitude[position].toString());
                Double lng = Double.valueOf(longitude[position].toString());

           /*     Toast.makeText(MainActivity.this, "Lat :"+lat+" | Long :"+lng, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Lat :"+user_lat+" | Long :"+user_lng, Toast.LENGTH_SHORT).show();
            */
                Intent sendingIntent = new Intent(MainActivity.this, Localisation.class);
                Bundle b = new Bundle();
                b.putDouble("latitude",lat);
                b.putDouble("longitude",lng);
                b.putDouble("user_lat",user_lat);
                b.putDouble("user_lng",user_lng);
                sendingIntent.putExtras(b);
                startActivity(sendingIntent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }

        });




    }
}
