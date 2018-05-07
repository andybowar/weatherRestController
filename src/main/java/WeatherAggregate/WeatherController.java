package WeatherAggregate;

import CurrentWeather.*;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Component
@RestController
public class WeatherController {

    private Temperature temperature;
    private Dewpoint dewpoint;
    private WindChill windChill;
    private RelativeHumidity humidity;

    private RestTemplate restTemplate = new RestTemplate();

    private WeatherCat weatherCategory = restTemplate.getForObject("https://api.weather.gov/stations/KMSP/observations/current", WeatherCat.class);

    public WeatherCat getWeatherCat() {
        return weatherCategory;
    }

    @RequestMapping("/weather")
    public Weather weather() {
        return new Weather(weatherCategory.getProperties().getTemperature().convertCelToFah(),
                weatherCategory.getProperties().getDewpoint().convertCelToFah(),
                weatherCategory.getProperties().getWindChill().convertCelToFah(),
                weatherCategory.getProperties().getRelativeHumidity().getValue());
    }
}