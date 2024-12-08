package com.meli.backend.rapid.ws.models;


public class User {
        
    private String name;

    private String surname;

    private double dni;

    public User() {
    }

    public void setDNI( double dni ) {
        this.dni = dni;
    }

    public double getDNI() {
        return this.dni;
    }

    public void setSurname( String surname ) {
        this.surname = surname;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setName( String name ) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}