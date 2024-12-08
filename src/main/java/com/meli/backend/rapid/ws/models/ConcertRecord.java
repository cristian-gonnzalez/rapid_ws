package com.meli.backend.rapid.ws.models;

import java.sql.Time;
import java.util.List;

public class ConcertRecord {

    ConcertKey concertKey;

    String place;

    String artist;

    Time time;

    List<SectorRecord> sectors;

    public ConcertRecord() {
    }
    
    public void setPlace( String place ) {
        this.place = place;
    }

    public String getPlace() {
        return this.place;
    }

    public void setArtist( String artist ) {
        this.artist = artist;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setSectors( List<SectorRecord> sectors ) {
        this.sectors = sectors;
    }

    public List<SectorRecord> getSectors() {
        return this.sectors;
    }

    public void setTime( Time time ) {
        this.time = time;
    }

    public Time getTime() {
        return this.time;
    }

    public void setConcerKey( ConcertKey concertKey ) {
        this.concertKey = concertKey;
    }

    public ConcertKey getConcerKey() {
        return this.concertKey;
    }
}
