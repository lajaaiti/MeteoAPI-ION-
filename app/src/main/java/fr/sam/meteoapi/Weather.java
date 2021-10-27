package fr.sam.meteoapi;

public class Weather {
    private long date;
    private String timeZone;
    private double temp;
    private String icon;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Weather(double temp, String icon) {
        this.temp = temp;
        this.icon = icon;
    }

    public Weather(long date, String timeZone, double temp, String icon) {
        this.date = date;
        this.timeZone = timeZone;
        this.temp = temp;
        this.icon = icon;
    }
}
