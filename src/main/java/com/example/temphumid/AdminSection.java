package com.example.temphumid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AdminSection extends AppCompatActivity {

    EditText cnic,name,fname;
    Button users,add,delete;
    DatabaseHelper databaseHelper;
    TextView viewdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_section);
        databaseHelper = new DatabaseHelper(this,null,null,3);

        cnic = (EditText) findViewById(R.id.cnic);
        name = (EditText) findViewById(R.id.name);
        fname = (EditText) findViewById(R.id.fname);

        viewdata = (TextView) findViewById(R.id.viewdata);

        users = (Button) findViewById(R.id.users);
        add =  (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);


        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(AdminSection.this,AllUsers.class));
                String record = databaseHelper.readData();
                viewdata.setText(record);
                Toast.makeText(getApplicationContext(),record +"",Toast.LENGTH_LONG).show();

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Patients patients = new Patients(cnic.getText().toString(),name.getText().toString(),fname.getText().toString());
                databaseHelper.addPatient(patients);
                Toast.makeText(getApplicationContext(),"data saved successfully",Toast.LENGTH_LONG).show();


            }
        });
    }
}