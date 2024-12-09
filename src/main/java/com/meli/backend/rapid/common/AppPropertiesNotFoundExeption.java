package com.meli.backend.rapid.common;

public class AppPropertiesNotFoundExeption extends RuntimeException {

    public AppPropertiesNotFoundExeption(String msg) {
        super(msg);
    }

}
