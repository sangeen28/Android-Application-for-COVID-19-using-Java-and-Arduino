package com.example.temphumid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InitialActivity extends AppCompatActivity {


    Handler handler;
    ProgressDialog dialog3;
    Spinner user;
    TextView data,country,t_cases,today_cases,t_deaths,today_deaths,tests,continent,recovered;
    Button get;
    String c,d,e,f,g,h,i,j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);



        user = (Spinner) findViewById(R.id.user);
      //  data = (TextView) findViewById(R.id.data);
        get = (Button) findViewById(R.id.get);

      country = (TextView) findViewById(R.id.country);
      t_cases = (TextView) findViewById(R.id.t_cases);
      today_cases = (TextView) findViewById(R.id.today_cases);
      t_deaths = (TextView) findViewById(R.id.t_deaths); 
      today_deaths = (TextView)  findViewById(R.id.today_deaths);
      tests = (TextView) findViewById(R.id.tests);
      continent = (TextView) findViewById(R.id.continent);
      recovered = (TextView) findViewById(R.id.recovered);
      dialog3 = new ProgressDialog(InitialActivity.this);





        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog3.setTitle("Wait ...");
                dialog3.setMessage("Just a Sec");
                dialog3.create();
                dialog3.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dialog3.dismiss();
                    }
                }, 3000);

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo==null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                    new AlertDialog.Builder(InitialActivity.this)
                            .setTitle("Network Issue")
                            .setMessage("you are not connected to the internet")
                            .setCancelable(false)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    recreate();
                                }
                            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            dialog3.dismiss();
                        }
                    }).create().show();
                } else {

                    dialog3.setTitle("Wait ...");
                    dialog3.setMessage("Just a Sec");
                    dialog3.create();
                    dialog3.show();
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dialog3.dismiss();
                        }
                    }, 3000);
                    show();
                  //  dialog3.dismiss();
                }
            }
        });
    }

    private void show() {
        String url = "https://disease.sh/v3/covid-19/countries/"+user.getSelectedItem().toString()+"/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                Toast.makeText(InitialActivity.this, "error in getting data", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                if (response.isSuccessful()) {
                                                    String resp = response.body().string();


                                                    Runnable runnable = new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            try {
                                                                JSONObject jsonObject = new JSONObject(resp);
                                                                 c = jsonObject.getString("country");
                                                                d = jsonObject.getString("cases");
                                                                e = jsonObject.getString("todayCases");
                                                                f = jsonObject.getString("deaths");
                                                                g = jsonObject.getString("todayDeaths");
                                                                h = jsonObject.getString("tests");
                                                                i = jsonObject.getString("recovered");
                                                                j = jsonObject.getString("continent");
                                                               // country.setText(c);
                                                              /*  data.setText("Country|"+c +"\n"
                                                                              +"cases|"+d+
                                                                        "\n"+e
                                                                        +"\n"+f
                                                                        +"\n"+g



                                                                );*/


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    country.setText(c);
                                                                    t_cases.setText(d);
                                                                    today_cases.setText(e);
                                                                    t_deaths.setText(f);
                                                                    today_deaths.setText(g);
                                                                    tests.setText(h);
                                                                    recovered.setText(i);
                                                                    continent.setText(j);
                                                                }
                                                            });
                                                        }

                                                    };
                                                    Thread thread = new Thread(runnable);
                                                    thread.start();

                                                }

                                            }

                                        });
    }
}












/*
                    InitialActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {


                                JSONObject jsonObject = new JSONObject(resp);
                                String c = jsonObject.getString("country");
                                String d = jsonObject.getString("cases");
                            *//*    String e = jsonObject.getString("iso2");
                                String f = jsonObject.getString("todaycases");
                                String g = jsonObject.getString("deaths");
                                String h = jsonObject.getString("todayDeaths");
                                String i = jsonObject.getString("recovered");
                                String j = jsonObject.getString("todayRecovered");
                                String k = jsonObject.getString("active");
                                String l = jsonObject.getString("critical");
*//*
                                data.setText("Country:"+c+"\n"+"Total Cases:"+d


                                );

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });*/
