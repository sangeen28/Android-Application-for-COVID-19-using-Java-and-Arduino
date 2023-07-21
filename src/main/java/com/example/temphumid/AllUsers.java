package com.example.temphumid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsers extends AppCompatActivity {

    ListView mylist;

    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    DatabaseReference mRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mylist = (ListView) findViewById(R.id.mylist);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("name");

        mRef2 = mDatabase.getReference().child("name").child("student");

        showParent();

    }
    private void showParent() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    String url = ds.getKey();
                    String names = url.toString();
                    arrayList.add(names);

                }
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_view,arrayList);
                mylist.setAdapter(arrayAdapter);
                mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String item = (String) (mylist.getItemAtPosition(position));
                        switch (item) {
                            case "sangeen":
                                Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_LONG).show();
                                break;
                            case "student":
                                mDatabase = FirebaseDatabase.getInstance();
                                mRef = mDatabase.getReference().child("name").child("student");
                                startReading();
                           //  Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"no data read",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void startReading() {


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    String url = ds.getValue(String.class);
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllUsers.this, android.R.style.Theme_Material_Dialog_Alert);

                    builder.setMessage(url);
                    builder.show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"no data read",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ReadData() {
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    String temp = ds.<String>getValue(String.class);
                    AlertDialog.Builder alert = new AlertDialog.Builder(AllUsers.this);
                    alert.setMessage("Temperature =" + temp.toString());
                    alert.create();
                    alert.show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"no data read",Toast.LENGTH_LONG).show();
            }
        });
    }
}