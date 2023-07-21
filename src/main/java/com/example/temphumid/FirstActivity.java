package com.example.temphumid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Locale;

public class FirstActivity extends AppCompatActivity {

    Button about_covid, covid_precautions, covid_symptoms, covid_meter, developers, login;
    TextView forget;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    int LOCATION_REQUEST_CODE = 10001;
    static FirstActivity instance;



    public static FirstActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        instance = this;

        about_covid = (Button) findViewById(R.id.about_covid);
        covid_precautions = (Button) findViewById(R.id.covid_precautions);
        covid_symptoms = (Button) findViewById(R.id.covid_symptoms);
        covid_meter = (Button) findViewById(R.id.covid_meter);
        developers = (Button) findViewById(R.id.developers);
        login = (Button) findViewById(R.id.login);

        forget = (TextView) findViewById(R.id.forget);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Create the LocationRequest object

/*

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(FirstActivity.this);
                        if (ActivityCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());

                        }else {
                            updateLocation();

                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(),"permission should be given",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                       */
/* AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                        builder.setTitle("permission");
                        builder.setMessage("required permission");
                        builder.create();
                        builder.show();*//*



                    }
                }).check();


*/
        developers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this,AboutDevelopers.class));
            }
        });

        covid_precautions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this,CovidPrecautions.class));
            }
        });

        about_covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, AboutCovid.class));
            }
        });

        covid_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(FirstActivity.this);
                dialog.setTitle("going");

                dialog.create();
                dialog.show();
                startActivity(new Intent(FirstActivity.this, CovidSymptoms.class));
                dialog.dismiss();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
            }
        });

        covid_meter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, InitialActivity.class));
            }
        });

    }

    private PendingIntent getPendingIntent() {

        Intent intent = new Intent(FirstActivity.this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(FirstActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


    }

    private void updateLocation() {
        buildLocationRequest();
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void updateView(String value) {
        FirstActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


             //   Toast.makeText(FirstActivity.this, "" + value, Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getLastLocation();
        } else {
            askLocationPermission();
        }
    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    Toast.makeText(getApplicationContext(),""+location.getLongitude(),Toast.LENGTH_LONG).show();
                    Geocoder geocoder = new Geocoder(FirstActivity.this,Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                       Toast.makeText(getApplicationContext(),""+addresses.get(0).getLocality()

                               +""+addresses.get(0).getAdminArea()
                               +""+addresses.get(0).getLocale()



                               ,Toast.LENGTH_LONG).show();
                    }catch (Exception ex) {

                    }

                }else {
                  //  Toast.makeText(getApplicationContext(),"no location",Toast.LENGTH_LONG).show();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
    private void askLocationPermission(){

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getApplicationContext(),"permission necessary",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }else {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
}