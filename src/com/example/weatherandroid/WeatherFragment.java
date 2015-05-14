package com.example.weatherandroid;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherandroid.R;
import com.example.weatherandroid.RemoteFetch;

@SuppressLint("NewApi")
public class WeatherFragment extends Fragment {
	
	
    Typeface weatherFont;
    
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
     
    Handler handler;
 
    public WeatherFragment(){  
        handler = new Handler();
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(R.layout.fragment_main, container, false);
        cityField = (TextView)listView.findViewById(R.id.city_field);
        updatedField = (TextView)listView.findViewById(R.id.updated_field);
        detailsField = (TextView)listView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)listView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)listView.findViewById(R.id.weather_icon);
         
        weatherIcon.setTypeface(weatherFont);
        return listView;
    }

private void updateWeatherData(final String city){
    new Thread(){
        public void run(){
            final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
            if(json == null){
                handler.post(new Runnable(){
                    public void run(){
                        Toast.makeText(getActivity(),
                                getActivity().getString(R.string.place_not_found),
                                Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                handler.post(new Runnable(){
                    public void run(){
                       
						renderWeather(json);
                    }
                });
            }              
        }
    }.start();
}
  
 private void renderWeather(JSONObject json){
    try {
        cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                ", " +
                json.getJSONObject("sys").getString("country"));
         
        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
        JSONObject main = json.getJSONObject("main");
        detailsField.setText(
                details.getString("description").toUpperCase(Locale.US) +
                "\n" + "Humidity: " + main.getString("humidity") + "%" +
                "\n" + "Pressure: " + main.getString("pressure") + " hPa");
         
        currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");
 
        DateFormat df = DateFormat.getDateTimeInstance();
        String updatedOn = df.format(new Date(json.getLong("dt")*1000));
        updatedField.setText("Last update: " + updatedOn);
 
        setWeatherIcon(details.getInt("id"),
                json.getJSONObject("sys").getLong("sunrise") * 1000,
                json.getJSONObject("sys").getLong("sunset") * 1000);
         
    }catch(Exception e){
        Log.e("WeatherAndroid", "One or more fields not found in the JSON data");
    }
}

@SuppressLint("NewApi")
private void setWeatherIcon(int actualId, long sunrise, long sunset){
    int id = actualId / 100;
    String icon = "";
    if(actualId == 800){
        long currentTime = new Date().getTime();
        if(currentTime>=sunrise && currentTime<sunset) {
            icon = getActivity().getString(R.string.weather_sunny);
        } else {
            icon = getActivity().getString(R.string.weather_clear_night);
        }
    } else {
        switch(id) {
        case 2 : icon = getActivity().getString(R.string.weather_thunder);
                 break;        
        case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                 break;    
        case 7 : icon = getActivity().getString(R.string.weather_foggy);
                 break;
        case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                 break;
        case 6 : icon = getActivity().getString(R.string.weather_snowy);
                 break;
        case 5 : icon = getActivity().getString(R.string.weather_rainy);
                 break;
        }
    }
    weatherIcon.setText(icon);
}

public void addCity(String city){
    updateWeatherData(city);
}


}
