package fr.sam.meteoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  final static String API_KEY = "30664bc320ca8e5f3c3f78f7b0e2578e";

    Button btnSearch;
    EditText etCityName;
    ImageView iconweather;
    TextView tvTemp, tvCity;
    ListView lvDaillyweather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        etCityName = findViewById(R.id.etCityName);
        iconweather = findViewById(R.id.iconweather);
        tvTemp = findViewById(R.id.tvTemp);
        tvCity = findViewById(R.id.tvCity);
        lvDaillyweather = findViewById(R.id.lvDaillyweather);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = etCityName.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this, "Entrez le nom de votre Ville !!", Toast.LENGTH_SHORT).show();
                }else{
                    loadweatherByCityName(city);
                }
            }
        });
    }


            private void loadweatherByCityName(String city) {
                Ion.with(MainActivity.this)
                        .load("https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid="+API_KEY)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                // do stuff with the result or error
                                //Log.d("result", result.toString());
                                if(e != null){
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "erreur service", Toast.LENGTH_SHORT).show();
                                }else{
                                    JsonObject main = result.get("main").getAsJsonObject();
                                    double temp = main.get("temp").getAsDouble();
                                    tvTemp.setText(temp+"°C");

                                    JsonObject sys = result.get("sys").getAsJsonObject();
                                    String country = sys.get("country").getAsString();
                                    tvCity.setText(city+", "+country);

                                    JsonArray weather = result.get("weather").getAsJsonArray();
                                    String icon = weather.get(0).getAsJsonObject().get("icon").getAsString();
                                    loadIcon(icon);

                                    JsonObject coord = result.get("coord").getAsJsonObject();
                                    double lon = coord.get("lon").getAsDouble();
                                    double lat = coord.get("lat").getAsDouble();

                                    loadDailyForcast(lon, lat);

                                }
                            }
                        });
                 }

            private void loadDailyForcast(double lon, double lat) {
                String apURL = "\"https://api.openweathermap.org/data/2.5/onecall?lat=\"+lat+\"&lon=\"+lon+\"&exclude=hourly,minutely,current&units=metric&appid=\"+API_KEY";
                Ion.with(MainActivity.this)
                        .load(apURL)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                // do stuff with the result or error
                                //Log.d("result", result.toString());
                                if(e != null){
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "erreur service", Toast.LENGTH_SHORT).show();
                                }else{
                                    List<Weather> weatherList = new ArrayList<>();
                                    String timeZone = result.get("timezone").getAsString();
                                    JsonArray daily =result.get("daily").getAsJsonArray();
                                    for(int i=1; i<daily.size(); i++){
                                        Long date = daily.get(i).getAsJsonObject().get("dt").getAsLong();
                                        Double temp = daily.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("day").getAsDouble();
                                        String icon = daily.get(i).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
                                        weatherList.add(new Weather(date,timeZone,temp,icon));
                                    }

                                    DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(MainActivity.this, weatherList);
                                    lvDaillyweather.setAdapter(dailyWeatherAdapter);
                                }
                            }
                        });

            }


            private void loadIcon(String icon){
                         Ion.with(MainActivity.this)
                            .load("https://openweathermap.org/img/w/"+icon+".png")
                             .intoImageView(iconweather);
            }


    }
