package com.example.temphumid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String DEFAULT = "N/A";

    FirebaseDatabase Database;
    DatabaseReference Ref;
    String e;
    String p;
    String n;
    String username;
    FirebaseUser auth;
    ProgressDialog dialog;
    ProgressDialog dialog2;
    String user_name;
    EditText email, password, admin_name,user;
    TextView title,forget,verify_email;
    Button login, signup, location;
    FirebaseAuth mAuth;
    String name;
    List<Address> addresses;

    String address;

    String total;


    String cityname;
    String country;
    String state;

    FirebaseDatabase mDatabase;
    String mRef;

  //  FusedLocationProviderClient mFusedLocationClient;
  //  int PERMISSION_ID = 44;

    public int Internet_Permission_code= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    /*   if (dialog==null) {

       }
       else {
           dialog.dismiss();
       }*/

        Database = FirebaseDatabase.getInstance();
        Ref = Database.getReference().child("name");

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
      //  location = (Button) findViewById(R.id.location);
        mAuth = FirebaseAuth.getInstance();
        user = (EditText) findViewById(R.id.user);

        forget = (TextView) findViewById(R.id.forget);

        mDatabase = FirebaseDatabase.getInstance();


        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Widget_Material_ActionBar_Solid);
                  builder.setTitle("Password Resetting");
                builder.setMessage("Enter your Email");

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.custom_dialog,null);

                admin_name = view.findViewById(R.id.name);
                title = view.findViewById(R.id.title);
              //  admin_pass = view.findViewById(R.id.pass);
                builder.setView(view);

                //builder.setView(admin_name);
              //  builder.setView(linearLayout);


                builder.setCancelable(true);


                builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //Toast.makeText(getApplicationContext(),"We are good to go !",Toast.LENGTH_LONG).show();
                    // startActivity(new Intent(MainActivity.this,AdminSection.class));
                        String sendmail = admin_name.getText().toString().trim();

                        mAuth.sendPasswordResetEmail(sendmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"mail sent",Toast.LENGTH_LONG).show();
                                    //  startActivity(new Intent(ResetPassword.this,MainActivity.class));
                                }else {
                                    Toast.makeText(getApplicationContext(),"error" +task.getException(),Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  Toast.makeText(getApplicationContext(),"you have entered a wrong code !",Toast.LENGTH_LONG).show();
                       dialog.dismiss();
                    }
                });
                builder.show();



            }
        });



      //      mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);



            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                  //  Toast.makeText(getApplicationContext(), "login clicked", Toast.LENGTH_SHORT).show();
                    ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo==null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Network Issue")
                                .setMessage("you are not connected to the internet")
                                .setCancelable(false)
                                .setPositiveButton("check Network", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                        startActivity(intent);

                                    }
                                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    } else {
                        // Toast.makeText(MainActivity.this, "all ok", Toast.LENGTH_SHORT).show();
                        n = user.getText().toString();
                       e = email.getText().toString().trim();
                       p = password.getText().toString().trim();
                    //    Toast.makeText(getApplicationContext(), "login checking", Toast.LENGTH_SHORT).show();




                        dialog = new ProgressDialog(MainActivity.this);
                        dialog.setTitle("Wait...");
                        dialog.setMessage("Just a Sec ...");
                        dialog.create();
                        dialog.show();



                        if (e.isEmpty()) {
                            email.setError("it is mandatory");
                            dialog.dismiss();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                            email.setError("not a valid email format");
                            dialog.dismiss();
                        }    else if (p.isEmpty()) {
                                password.setError("it is mandatory");
                                dialog.dismiss();
                            }
                    else if (n.isEmpty()) {
                            user.setError("it is mandatory");
                        } /*else if (n!=username){
                            user.setError("not registered");
                            Toast.makeText(getApplicationContext(),""+n,Toast.LENGTH_LONG).show();
                        }*/
                        else {
                          //  Toast.makeText(getApplicationContext(), "logging checking", Toast.LENGTH_SHORT).show();

                         //   Toast.makeText(getApplicationContext(),""+n,Toast.LENGTH_LONG).show();

                            mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(task -> {
                           //     Toast.makeText(getApplicationContext(), "login checking inside", Toast.LENGTH_SHORT).show();

                                if (task.isSuccessful()) {
                                  //  Toast.makeText(getApplicationContext(), "signed in successfully", Toast.LENGTH_LONG).show();
/*                                    FirebaseUser ema = mAuth.getCurrentUser();
                                    assert ema != null;
                                    if (ema.isEmailVerified()) {
                                        Toast.makeText(getApplicationContext(),"email verified",Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(),"email not verified",Toast.LENGTH_LONG).show();

                                    }*/
                                    dialog = new ProgressDialog(MainActivity.this);
                                    dialog.setTitle("Wait...");
                                    dialog.setMessage("Just a Sec ...");
                                    dialog.create();
                                    dialog.show();


                                    auth = mAuth.getInstance().getCurrentUser();
                                    if (auth.isEmailVerified()) {
                                        dialog = new ProgressDialog(MainActivity.this);
                                        dialog.setTitle("Wait...");
                                        dialog.setMessage("Just a Sec ...");
                                        dialog.create();
                                        dialog.show();
                                        String name = user.getText().toString();
                                        Intent i = new Intent(MainActivity.this, ShowActivity.class);


                                        i.putExtra("name", name);
                                       // dialog.dismiss();
                                        startActivity(i);
                                        dialog.dismiss();
                                    }else {
                                        dialog.dismiss();
                                         Toast.makeText(getApplicationContext(),"Please verify Your Email or Check your password",Toast.LENGTH_LONG).show();
                                    }

                                    //  Toast.makeText(getApplicationContext(),"signed in successfully",Toast.LENGTH_LONG).show();

                                    //  startActivity(new Intent(MainActivity.this, ShowActivity.class));
                                    //   updateUI();

                                } else {
                                    dialog = new ProgressDialog(MainActivity.this);
                                    dialog.setTitle("Wait...");
                                    dialog.setMessage("Just a Sec ...");
                                    dialog.create();
                                    dialog.show();
                                    Toast.makeText(getApplicationContext(),"not valid user",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();

                                }/*else {
                                    dialog.dismiss();
                                    //       Toast.makeText(getApplicationContext(), "user not valid", Toast.LENGTH_LONG).show();

                                }*/});

                        }
                        dialog.dismiss();

                    }


                }
            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Contact Admin")
                            .setMessage("To register yourself, Contact us at" +"\n"+
                                    "sangeenkhan2662@gmail.com")
                            .setCancelable(false)
                            .setPositiveButton("Contact", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  //  Toast.makeText(getApplicationContext(), "I want to contact", Toast.LENGTH_LONG).show();
                                  /*  Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(intent);

*/
                                  startActivity(new Intent(MainActivity.this,EmailContact.class));
                                }
                            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "I do not want to contact", Toast.LENGTH_LONG).show();

                        }
                    }).create().show();




                  /*  String e = email.getText().toString().trim();
                    String p = password.getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "user created", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "user not created" + task.getException(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });*/
                    //  startActivity(new Intent(MainActivity.this,AdminSection.class));

                  //  fetchData();
                  //  getData();

                }
            });

        //    location.setOnClickListener(new View.OnClickListener() {
           //     @Override
            //    public void onClick(View v) {


                    // method to get the location
          //          getLastLocation();


             //   }
        //    });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_show,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
/*
        dialog2 = new ProgressDialog(MainActivity.this);
        dialog2.setTitle("Wait ...");
        dialog2.setMessage("Sending Verification Email");
        dialog2.create();
        dialog2.show();*/
        auth = mAuth.getInstance().getCurrentUser();


        if (item.getItemId() == R.id.verify) {
         //   dialog2.dismiss();
            e = email.getText().toString().trim();
            if (e.isEmpty()) {
                email.setError("it is mandatory");
                //   dialog2.dismiss();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                email.setError("not a valid email format");
                // dialog2.dismiss();
            }
             else if (auth == null && auth.isEmailVerified()) {
                Toast.makeText(getApplicationContext(), "Already verified | user not logged in", Toast.LENGTH_LONG).show();
            } else  {
                dialog2 = new ProgressDialog(MainActivity.this);
                dialog2.setTitle("Wait ...");
                dialog2.setMessage("Sending Verification Email");
                dialog2.create();
                dialog2.setCancelable(false);
                dialog2.show();
                if (e.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                    dialog2.dismiss();
                }else {
                    verifyEmail();

                }
            //    Toast.makeText(getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG).show();
            }
        }
        if(item.getItemId()== R.id.add) {
            startActivity(new Intent(MainActivity.this,AddGuardian.class));
        }

            return true;

    }

    private void verifyEmail() {
        auth.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"email verification sent",Toast.LENGTH_LONG).show();
                        dialog2.dismiss();
                    }
                });
    }







  /*  private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
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
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                        //    Toast.makeText(getApplicationContext(),""+location.getLatitude(),Toast.LENGTH_SHORT).show();

                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                 addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                Log.i("locality2",addresses.get(0).getLocality().toString());
                                Log.i("showing2","now");

                                //   password.setText(" " + addresses.get(0).getAddressLine(0));
                                Toast.makeText(getApplicationContext(), "starting location done", Toast.LENGTH_LONG).show();


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                         //   address = addresses.get(0).getAddressLine(0);

                            country = addresses.get(0).getCountryName();
                            cityname = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            String postalcoe = addresses.get(0).getPostalCode();

                            //  Toast.makeText(getApplicationContext(),""+location.getLongitude(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),cityname +","+country+","+state+","+postalcoe,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }


    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
          //  Toast.makeText(getApplicationContext(),""+mLastLocation.getLatitude(),Toast.LENGTH_SHORT).show();

            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
              //  Log.i("locality1",addresses.get(0).getLocality().toString());
              //  Log.i("showing1","now");
              //  password.setText(" " + addresses.get(0).getAddressLine(0));
               // Toast.makeText(getApplicationContext(), "starting location done", Toast.LENGTH_LONG).show();


            } catch (IOException e) {
                e.printStackTrace();
            }
           // Toast.makeText(getApplicationContext(),""+mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();
           //  address = addresses.get(0).getAddressLine(0);
           // cityname = addresses.get(0).getAddressLine(1);
            country = addresses.get(0).getCountryName();
            state = addresses.get(0).getAdminArea();

            cityname = addresses.get(0).getLocality();
            String postalcoe = addresses.get(0).getPostalCode();


            //    String country = addresses.get(0).getAddr
            Toast.makeText(getApplicationContext(),cityname+","+country+","+state+""+postalcoe,Toast.LENGTH_LONG).show();

        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_LONG).show();

        }
    }*/

    @Override
    public void onResume() {
        super.onResume();

       /* if (checkPermissions()) {
            getLastLocation();
        }*/
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this,"error in getting data",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                  if(response.isSuccessful()) {
                      String resp = response.body().string();
                      MainActivity.this.runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  JSONObject jsonObject = new JSONObject(resp);
                                  total = jsonObject.getString("cases");
                                  Toast.makeText(MainActivity.this,"tota cases :"+total,Toast.LENGTH_LONG).show();
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                      });
                  }
            }
        });

    }
    private void getData() {
        String url = "https://disease.sh/v3/covid-19/countries/"+user.getText().toString()+"/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this,"error in getting data",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String resp = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(resp);
                                country = jsonObject.getString("country");
                                Toast.makeText(MainActivity.this,"country name is :"+country,Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }



    /* location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
                  //  getLocation();
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Toast.makeText(getApplicationContext(), "starting location", Toast.LENGTH_LONG).show();

                      Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    email.setText(" " + addresses.get(0).getLocality());
                                    password.setText(" " + addresses.get(0).getAddressLine(0));
                                    Toast.makeText(getApplicationContext(), "starting location done", Toast.LENGTH_LONG).show();


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }       if (location != null) {


                            }*//* else {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                            }*//*
                        }


                    });
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

    }
    */



   /* private void getLocation() {
       *//* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.*//*


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},44);


            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Toast.makeText(getApplicationContext(), "starting location", Toast.LENGTH_LONG).show();

                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        email.setText(" " + addresses.get(0).getLocality());
                        password.setText(" " + addresses.get(0).getAddressLine(0));
                        Toast.makeText(getApplicationContext(), "starting location done", Toast.LENGTH_LONG).show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }


        });
        }

*/



  /*  @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

      *//*  admin_name = new EditText(MainActivity.this);
        admin_pass = new EditText(MainActivity.this);


        admin_name.setHint("enter your name");
        admin_name.setHintTextColor(Color.MAGENTA);


         LinearLayout linearLayout = new LinearLayout(this);
         linearLayout.setOrientation(LinearLayout.VERTICAL);
         linearLayout.addView(admin_name);
         linearLayout.addView(admin_pass);


*//*


        switch (item.getItemId()) {

           // case R.id.admin:
             *//*   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Widget_Material_ActionBar_Solid);
            *//**//* builder.setTitle("Please Signed In As Admin");
                builder.setMessage("Enter your Credentials");
*//**//*
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.custom_dialog,null);

                admin_name = view.findViewById(R.id.name);
                title = view.findViewById(R.id.title);
                admin_pass = view.findViewById(R.id.pass);
                builder.setView(view);

                //builder.setView(admin_name);
              //  builder.setView(linearLayout);


                builder.setCancelable(true);


                builder.setPositiveButton("LogIn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     Toast.makeText(getApplicationContext(),"We are good to go !",Toast.LENGTH_LONG).show();
                     startActivity(new Intent(MainActivity.this,AdminSection.class));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"you have entered a wrong code !",Toast.LENGTH_LONG).show();

                    }
                });
                builder.show();
*//*

             startActivity(new Intent(MainActivity.this,InitialActivity.class));



        }

       return true;
    }*/



    @Override
    protected void onStart() {
        super.onStart();
       // updateUI();
    }

    private void updateUI() {
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user==null) {
           // Toast.makeText(this,"no user logged in",Toast.LENGTH_LONG).show();
        }else {

          String name = user.getDisplayName();
            Intent i = new Intent(MainActivity.this, ShowActivity.class);


            i.putExtra("name", name);
            startActivity(i);
          //  startActivity(new Intent(MainActivity.this,ShowActivity.class));
        }
    }

}