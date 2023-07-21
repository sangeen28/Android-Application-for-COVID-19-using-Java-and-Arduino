package com.example.temphumid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmailContact extends AppCompatActivity {

    EditText mail,subject,message;
    Button send;
    String text = "sangeenkhan2662@gmail.com";
    String text2 = "Registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_contact);

        mail = (EditText) findViewById(R.id.mail);
        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.message);

        mail.setEnabled(false);
        subject.setEnabled(false);


        send =  (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail.setText(text);
                subject.setText(text2);
                String receiver = mail.getText().toString().trim();
                String sub = subject.getText().toString().trim();
                String msg = message.getText().toString().trim();
                sendEmail(receiver,sub,msg);
            }
        });
    }

    private void sendEmail(String receiver, String sub, String msg) {

        Intent mEmailIntent = new Intent(Intent.ACTION_SEND);
        mEmailIntent.setData(Uri.parse("mailto:"));
        mEmailIntent.setType("text/plain");
        mEmailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] {receiver});
        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
        mEmailIntent.putExtra(Intent.EXTRA_TEXT,msg);

        try {
            startActivity(Intent.createChooser(mEmailIntent,"choose an email client"));
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }









    }
}