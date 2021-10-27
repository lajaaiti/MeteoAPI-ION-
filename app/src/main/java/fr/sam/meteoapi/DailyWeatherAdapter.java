package fr.sam.meteoapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyWeatherAdapter extends ArrayAdapter<Weather> {
    private Context context;
    private List<Weather> weatherList;
    public DailyWeatherAdapter(@NonNull Context context, @NonNull List<Weather> weatherList) {
        super(context, 0, weatherList);
        this.context = context;
        this.weatherList = weatherList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false);

        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvTemp = convertView.findViewById(R.id.tvTemp);
        ImageView iconweather= convertView.findViewById(R.id.tviconweather);

        Weather weather = weatherList.get(position);
        tvTemp.setText(weather.getTemp()+"°C");


            Ion.with(context)
                    .load("https://openweathermap.org/img/w/"+weather.getIcon()+".png")
                    .intoImageView(iconweather);

            Date date = new Date(weather.getDate()*1000);
            DateFormat dateformat = new SimpleDateFormat("EEE, MM, YY", Locale.FRANCE);
            dateformat.setTimeZone(TimeZone.getTimeZone(weather.getTimeZone()));
            tvDate.setText(dateformat.format(date));

        return convertView;
    }


}
