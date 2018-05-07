package WeatherAggregate;

import CurrentWeather.*;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Component
@RestController
public class WeatherController {

    private Temperature temperature;
    private Dewpoint dewpoint;
    private WindChill windChill;
    private RelativeHumidity humidity;
    private String station;

    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/weather")
    public Weather weather(@RequestParam(value = "station", defaultValue = "KMSP") String station) {
        WeatherCat weatherCategory = restTemplate.getForObject("https://api.weather.gov/stations/" + station + "/observations/current", WeatherCat.class);
        return new Weather(weatherCategory.getProperties().getTemperature().convertCelToFah(),
                weatherCategory.getProperties().getDewpoint().convertCelToFah(),
                weatherCategory.getProperties().getWindChill().convertCelToFah(),
                weatherCategory.getProperties().getRelativeHumidity().getValue());
    }
}