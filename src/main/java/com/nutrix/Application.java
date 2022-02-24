package com.nutrix;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        System.setProperty("jna.debug_load", "true");
        Micronaut.run(Application.class, args);
    }
}
