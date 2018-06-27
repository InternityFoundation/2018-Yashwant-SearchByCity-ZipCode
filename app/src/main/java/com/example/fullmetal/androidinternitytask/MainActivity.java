package com.example.fullmetal.androidinternitytask;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private String json_url;
    private EditText search_city_zipcode;
    private RadioButton radioButton1, radioButton2;
    private RequestQueue requestQueue;
    private TextView pressure_text, humidity_text, visibilty_text, temp_text, temp_min_text, temp_max_text, location_text;
    String description;
    LinearLayout linearLayout1;
    ImageView img;
    int cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_city_zipcode = findViewById(R.id.search_city_zipcode);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);

        pressure_text = findViewById(R.id.pressure);
        humidity_text = findViewById(R.id.humidity);
        visibilty_text = findViewById(R.id.visibility);
        temp_text = findViewById(R.id.temp);
        temp_min_text = findViewById(R.id.temp_min);
        temp_max_text = findViewById(R.id.temp_max);
        location_text = findViewById(R.id.location);
        linearLayout1 = findViewById(R.id.linear_layout1);
        img = findViewById(R.id.img);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void showWeather(View view) {
        String text = search_city_zipcode.getText().toString();
        if (text.contentEquals("")) {
            Toast.makeText(this, "Enter some data first!", Toast.LENGTH_SHORT).show();
        } else {
            if (radioButton1.isChecked()) {
                json_url = "http://api.openweathermap.org/data/2.5/weather?appid=51bd186d81e9633cd022b45a26cdf1f2&q=" + text;
            } else if (radioButton2.isChecked()) {
                json_url = "http://api.openweathermap.org/data/2.5/weather?appid=51bd186d81e9633cd022b45a26cdf1f2&zip=" + text + ",IN";
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, json_url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        cod = response.getInt("cod");
                        if (cod == 200) {
                            JSONObject sys = response.getJSONObject("sys");
                            JSONObject main = response.getJSONObject("main");
                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);

                            description = weather.getString("main");

                            int temp = (int)(main.getInt("temp")-273.15);
                            int temp_min = (int)(main.getInt("temp_min")-273.15);
                            int temp_max = (int)(main.getInt("temp_max")-273.15);

                            pressure_text.setText("Pressure     : " + main.getInt("pressure") + " mBar");
                            humidity_text.setText("Humidity     : " + main.getInt("humidity") + "%");
                            visibilty_text.setText("Visibility      : " + response.getInt("visibility") + "km");
                            temp_text.setText(temp + "°C");
                            temp_min_text.setText("Min. Temp   : " + temp_min + "°C");
                            temp_max_text.setText("Max. Temp  : " + temp_max + "°C");
                            location_text.setText("Location     : " + response.getString("name") + ", " + sys.getString("country"));

                            if(description.contentEquals("Haze") || description.contentEquals("Clouds")){
                                linearLayout1.setBackgroundResource(R.color.haze);
                                img.setImageResource(R.drawable.cloudy);
                            }
                            else if(description.contentEquals("Thunderstorm")){
                                linearLayout1.setBackgroundResource(R.color.tstorm);
                                img.setImageResource(R.drawable.tstorm);
                            }
                            else if(description.contentEquals("Rain")){
                                linearLayout1.setBackgroundResource(R.color.rainy);
                                img.setImageResource(R.drawable.rain);
                            }
                            else{
                                linearLayout1.setBackgroundResource(R.color.sunny);
                                img.setImageResource(R.drawable.sunny);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(MainActivity.this, "Some Error Occured!", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(request);
        }
    }
}
//// degree - °