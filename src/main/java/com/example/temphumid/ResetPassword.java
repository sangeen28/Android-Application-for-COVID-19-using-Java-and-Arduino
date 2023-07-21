package com.example.temphumid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ResetPassword extends AppCompatActivity {


    GraphView graphView;



    EditText contactmail;
    Button sentmail;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        contactmail = (EditText) findViewById(R.id.contactmail);
        sentmail = (Button) findViewById(R.id.sentmail);
        mAuth = FirebaseAuth.getInstance();


      sentmail.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final String uname = "sangeencs@gmail.com";
              final String pswd = "Smaham@543";
              String msg = "This is an alert from Smart Mast";


              Properties properties = new Properties();
              properties.put("mail.smtp.auth","true");
              properties.put("mail.smtp.starttls.enable","true");
              properties.put("mail.smtp.host","smtp.gmail.com");
              properties.put("mail.smtp.port","25");

              Session session = Session.getInstance(properties,
              new javax.mail.Authenticator(){
                  @Override
                  protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication(uname,pswd);
                  }
              });

              try {
                  Message message = new MimeMessage(session);
                  message.setFrom(new InternetAddress(uname));
                  message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(contactmail.getText().toString()));
                  message.setSubject("from sangeen");
                  message.setText(msg);
                  Transport.send(message);
                  Toast.makeText(getApplicationContext(),"message sent",Toast.LENGTH_LONG).show();
              } catch (MessagingException e) {
                  throw  new RuntimeException(e);
              }
          }
      });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);







    }
}