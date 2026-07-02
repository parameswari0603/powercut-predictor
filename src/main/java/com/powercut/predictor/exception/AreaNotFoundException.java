package com.powercut.predictor.exception;

public class AreaNotFoundException
        extends RuntimeException {

    public AreaNotFoundException(String area) {
        super("Area not found: " + area);
    }
}
