package com.pixel.jda.bot.util.exception;

public class NexonApiException extends Exception{

    public NexonApiException(String message) {
        super(message);
    }

    public NexonApiException(String message, String errorName, String errorMessage) {
        super(message + "\n" +
                "errorName ::: " + errorName + "\n" +
                "errorMessage ::: " + errorMessage + "\n");
    }
}
