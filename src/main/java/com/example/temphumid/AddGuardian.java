package com.example.temphumid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

public class AddGuardian extends AppCompatActivity {

    private CountryCodePicker code_picker;
    private TextView phoneTextView;
    private Button sendBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guardian);

      //  phone = (EditText) findViewById(R.id.phone);
      //  save = (Button) findViewById(R.id.save);
        code_picker = (CountryCodePicker) findViewById(R.id.code_picker);
        phoneTextView = (EditText) findViewById(R.id.editTextPhone);
        sendBtn = (Button) findViewById(R.id.btnSend);
        listeners();




      /*  save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("MyData",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("name",phone.getText().toString());
                editor.commit();

                Toast.makeText(getApplicationContext(), "Your details has been save" , Toast.LENGTH_SHORT).show();
            }
        });*/

    }


    private void listeners(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Variable
                String code = code_picker.getSelectedCountryCode();
              //  String country = ccp.getSelectedCountryEnglishName();
                String number = phoneTextView.getText().toString();

                // Create Toast
                Context context = getApplicationContext();
                CharSequence text =  " " + "+" +code +""+number;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                SharedPreferences sharedPref = getSharedPreferences("MyData",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("name", (String) text);
                editor.commit();

                Toast.makeText(getApplicationContext(), "Your details has been save"+ text , Toast.LENGTH_SHORT).show();
            }
        });
    }




}