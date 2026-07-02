package com.powercut.predictor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate =
            new RestTemplate();

    public double getCurrentTemperature(String city) {
        try {
            String url = apiUrl + "?q=" + city
                    + "&appid=" + apiKey
                    + "&units=metric";
            Map response = restTemplate
                    .getForObject(url, Map.class);
            Map main = (Map) response.get("main");
            return ((Number) main.get("temp"))
                    .doubleValue();
        } catch (Exception e) {
            System.out.println(
                    "Weather API unavailable, using default");
            return 32.0;
        }
    }
}
