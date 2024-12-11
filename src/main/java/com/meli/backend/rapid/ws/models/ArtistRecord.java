package com.meli.backend.rapid.ws.models;

public class ArtistRecord {

    Integer artist_id;

    String name;

    public ArtistRecord() {
    }

    public Integer getArtistId() {
        return this.artist_id;
    }

    public void setArtistId(Integer artist_id ) {
        this.artist_id = artist_id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name ) {
        this.name = name;
    }
}
