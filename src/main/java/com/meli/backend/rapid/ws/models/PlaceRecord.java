package com.meli.backend.rapid.ws.models;

public class PlaceRecord {

    Integer place_id;

    String name;

    public PlaceRecord() {
    }

    public Integer getPlaceId() {
        return this.place_id;
    }

    public void setPlaceId(Integer place_id ) {
        this.place_id = place_id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name ) {
        this.name = name;
    }

}
