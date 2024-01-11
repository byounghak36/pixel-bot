package com.pixel.jda.bot.util.exception;

public class PixelException extends Exception{

        public PixelException(String message) {
            super(message);
        }

        public PixelException(String message, String errorName, String errorMessage) {
            super(message + "\n" +
                    "errorName ::: " + errorName + "\n" +
                    "errorMessage ::: " + errorMessage + "\n");
        }

}
