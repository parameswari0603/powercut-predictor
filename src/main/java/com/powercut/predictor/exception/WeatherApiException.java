package com.powercut.predictor.exception;

public class WeatherApiException
        extends RuntimeException {

    public WeatherApiException(String message) {
        super("Weather API error: " + message);
    }
}