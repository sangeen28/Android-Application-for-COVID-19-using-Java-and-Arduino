package com.example.temphumid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ShowActivity extends AppCompatActivity {

    private static final String DEFAULT = "N/A";
    public static final String TAG ="data";
    TextView show, temp,beat,heart;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    FirebaseAuth mAuth;
    String u;
    private TextToSpeech mtts;
    private TextToSpeech mtts2;
    Vibrator vibrator;

    String t;
    String h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        show = (TextView) findViewById(R.id.show);
        temp = (TextView) findViewById(R.id.temp);
        beat = (TextView) findViewById(R.id.beat);
        heart = (TextView) findViewById(R.id.heart);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


       // updateUI();
    //    FirebaseUser auth = mAuth.getInstance().getCurrentUser();
      //  if (auth == null) {
            Bundle bundle = getIntent().getExtras();
//        String u = bundle.toString();

            if (bundle != null) {
                u = bundle.getString("name");
                //  mRef = mDatabase.getReference().child("name").child(u);
                //  startReading();
               // Log.i("user", u);
                mDatabase = FirebaseDatabase.getInstance();
                mRef = mDatabase.getReference().child("name").child(u).child("temp");
                mRef2 = mDatabase.getReference().child("name").child(u).child("beat");
        //         updateUI();
                startReading();
                startRead();

            }

        }

    private void startRead() {
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds1 : snapshot.getChildren()) {
                    // long value = (long) ds.getValue();
                    //  String url = ds.getValue(String.class);
                    //   String url = value.toString();
                    heart.setText(ds1.getValue().toString());
                    h = heart.getText().toString();
                    int b = Integer.parseInt(h);
                    // Toast.makeText(getApplicationContext(),""+a,Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onDataChange: "+b);
                    if (b>20) {
                        Log.i("condition ","normal");
                        showMessageHeart();
                        // Toast.makeText(getApplicationContext(), "normal", Toast.LENGTH_LONG).show();

                    } else {
                        Log.i("condition ","abnormal");

                       // Toast.makeText(getApplicationContext(), "abnormal", Toast.LENGTH_LONG).show();

                    }
                    //  temp.setText((int) value);
                    //   assert url != null;
                   /* int tem = Integer.parseInt(url);
                    if (tem < 30) {
                        Log.i("temp", String.valueOf(tem));
                    } else {
                        Toast.makeText(getApplicationContext(), "abnormal", Toast.LENGTH_LONG).show();

                    }*/


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"no data read",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // mAuth.signOut();
      //  updateUI();
      //  MainActivity mainActivity = new MainActivity();
     //   mainActivity.dialog.dismiss();
     // startActivity(new Intent(ShowActivity.this,MainActivity.class));

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_show,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      *//*  switch (item.getItemId()) {

                case R.id.logout:
                        mAuth.signOut();
                        updateUI();
                        break;
                case R.id.guardian:
                        if (mAuth.getCurrentUser()==null) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(this);
                            alert.setTitle("NO user");
                            alert.setMessage("PLease login to the application");
                            alert.setIcon(R.drawable.not_user);
                            alert.create();
                            alert.show();
                        }else
                            startActivity(new Intent(ShowActivity.this,AddGuardian.class));
                        break;
                        case R.id.resetpassword :
                        startActivity(new Intent(ShowActivity.this,ResetPassword.class));
                        break;
                    case R.id.shownumber:
                        SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
                        String name = sharedPreferences.getString("name",DEFAULT);
                        if (name.equals(DEFAULT)){
                            Toast.makeText(this,"loading details not found",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(this,"loading details successfully" +name,Toast.LENGTH_SHORT).show();
                        }


        }*//*
        return true;
    }*/
    private void updateUI() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, " logged out", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ShowActivity.this, MainActivity.class));
        } else {
            Toast.makeText(this, "user  logged in showActivity", Toast.LENGTH_LONG).show();
            Bundle bundle = getIntent().getExtras();
         if  (bundle != null)
            u = bundle.getString("name");
            mDatabase = FirebaseDatabase.getInstance();
            mRef = mDatabase.getReference().child("name").child(u).child("temp");
            mRef2 = mDatabase.getReference().child("name").child(u).child("beat");

            startReading();
            startRead();


        }

    }

    public void showData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, " logged out", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ShowActivity.this, MainActivity.class));
        } else {
            Toast.makeText(this, "user  logged in showActivity", Toast.LENGTH_LONG).show();
            Bundle bundle = getIntent().getExtras();
            if  (bundle != null)
                u = bundle.getString("name");
            mDatabase = FirebaseDatabase.getInstance();
            mRef = mDatabase.getReference().child("name").child(u).child("temp");
            mRef2 = mDatabase.getReference().child("name").child(u).child("beat");

            startReading();
            startRead();


        }

    }

    private void startReading() {


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                   // long value = (long) ds.getValue();
                  //  String url = ds.getValue(String.class);
                 //   String url = value.toString();
                    temp.setText(ds.getValue().toString());
                   t = temp.getText().toString();
                   float a = Float.parseFloat(t);
                  // Toast.makeText(getApplicationContext(),""+a,Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onDataChange: "+a);
                    if (a>20) {


                        Log.i("condition ","normal");
                      /*  Intent intent = new Intent(ShowActivity.this,MyBackground.class);
                        intent.setAction("Background");

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(ShowActivity.this,0,intent,0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,0,10,pendingIntent);
                        finish();*/
                        sendMessage();
                         showMessage();
                       // Toast.makeText(getApplicationContext(), "normal", Toast.LENGTH_LONG).show();

                    } else {
                        Log.i("condition ","abnormal");

                //        Toast.makeText(getApplicationContext(), "abnormal", Toast.LENGTH_LONG).show();

                    }
                  //  temp.setText((int) value);
                 //   assert url != null;
                   /* int tem = Integer.parseInt(url);
                    if (tem < 30) {
                        Log.i("temp", String.valueOf(tem));
                    } else {
                        Toast.makeText(getApplicationContext(), "abnormal", Toast.LENGTH_LONG).show();

                    }*/


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"no data read",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showMessageHeart() {
        mtts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int lang = mtts.setLanguage(Locale.ENGLISH);
                    String s = "your heartbeat is "+ h.toString();
                    int speech = mtts.speak(s,TextToSpeech.QUEUE_FLUSH,null);
                }else {
                    Toast.makeText(getApplicationContext(),"not not not ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showMessage() {
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("warning");
        builder.setMessage("your temperature is high");
        builder.setCancelable(true);
        builder.create().show();*/
        mtts2 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int lang = mtts2.setLanguage(Locale.ENGLISH);
                    String s = "your temperature is "+ t.toString();
                    int speech = mtts2.speak(s,TextToSpeech.QUEUE_FLUSH,null);
                }else {
                    Toast.makeText(getApplicationContext(),"not not not ",Toast.LENGTH_LONG).show();
                }
            }
        });



        vibrator.vibrate(1000);
    }

    public void sendMessage() {
               try {
        if(ActivityCompat.checkSelfPermission(ShowActivity.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "called", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

//Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage("+923115506139", null, "hello" +t +"" +h , null, null);
                Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_LONG).show();
            } else {

            //  Toast.makeText(getApplicationContext(), "permissiondenied", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
            }
        }
        } catch (Exception e) {
              Toast.makeText(getApplicationContext(),"message failed",Toast.LENGTH_LONG).show();
            }


    }





}

/*
class MyBackground extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"my task",Toast.LENGTH_LONG).show();
    }
}*/
