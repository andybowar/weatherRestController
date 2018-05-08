package WeatherAggregate;

import CurrentWeather.*;

import GetCoordinates.FindCoordinates;
import GetCoordinates.Results;
import WeatherStation.CoordinatesEndpoint.CoordinatesEndpoint;
import WeatherStation.StationsEndpoint.Features;
import WeatherStation.StationsEndpoint.StationsEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RestController
public class WeatherController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/weather")
    public Weather weather(@RequestParam(value = "zipCode", defaultValue = "55116") String zipCode) {

        // Hits Google's Geocode API to find coordinates based on a given zip code
        FindCoordinates findCoordinates = restTemplate.getForObject("http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=" + zipCode, FindCoordinates.class);

        // If we've queried the API too many times, it returns an error.
        // getStatus() will catch the error and end the program.
        findCoordinates.getStatus();

        // Stores the results of lat/lon in a one-element long list.
        // Must be a list because of JSON structure.
        // List should never have more than one element.
        List<Results> results = findCoordinates.getResults();

        // Lat and Lng are two separate fields in the Geocode JSON, and
        // the list is contains only one element, so the lat/lon are
        // always the in the first index.
        Double lat = results.get(0).getGeometry().getLocation().getLat();
        Double lng = results.get(0).getGeometry().getLocation().getLng();
        String location = results.get(0).getFormatted_address();

        // Concatenate lat/lon into a single string, then save to file
        String latLon = String.valueOf(lat) + "," + String.valueOf(lng);

        // Get endpoint for the list of observation stations near the given coordinates
        CoordinatesEndpoint coordinatesEndpoint = restTemplate
                .getForObject("https://api.weather.gov/points/"
                        + latLon, CoordinatesEndpoint.class);

        // observationStations variable now contains URL for the endpoint needed to find the list of observation stations
        String observationStations = coordinatesEndpoint.getProperties().getObservationStations();
        String forecastUrl = coordinatesEndpoint.getProperties().getForecast();

        // Stats of each weather station are stored in the 'features' list
        StationsEndpoint stationsEndpoint = restTemplate.getForObject(observationStations, StationsEndpoint.class);
        List<Features> features = stationsEndpoint.getFeatures();

        String stationUrl = features.get(0).getId();

        WeatherCat weatherCategory = restTemplate.getForObject(stationUrl + "/observations/current", WeatherCat.class);

        return new Weather(weatherCategory.getProperties().getTextDescription(),
                weatherCategory.getProperties().getTemperature().convertCelToFah(),
                weatherCategory.getProperties().getDewpoint().convertCelToFah(),
                weatherCategory.getProperties().getWindChill().convertCelToFah(),
                weatherCategory.getProperties().getRelativeHumidity().getValue());
    }
}