package CurrentWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCat {

    private Properties properties;

    public WeatherCat() {

    }
    public Properties getProperties() {
        return properties;
    }
}
