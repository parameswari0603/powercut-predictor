package com.powercut.predictor.exception;

public class DuplicateReportException
        extends RuntimeException {

    public DuplicateReportException(String area) {
        super("Report already submitted for "
                + area + " in the last 15 minutes.");
    }
}
