package WeatherAggregate;

public class Weather {
    private long temperture;
    private long dewpoint;
    private long windChill;
    private String humidity;

    public Weather(long temperature, long dewpoint, long windChill, String humidity) {
        this.temperture = temperature;
        this.dewpoint = dewpoint;
        this.windChill = windChill;
        this.humidity = humidity;
    }

    public long getTemperture() {
        return temperture;
    }

    public long getDewpoint() {
        return dewpoint;
    }

    public long getWindChill() {
        return windChill;
    }

    public String getHumidity() {
        return humidity;
    }
}
