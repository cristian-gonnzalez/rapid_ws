package com.meli.backend.rapid.req_ctx.req_ctx_io;

import java.sql.Time;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meli.backend.rapid.ws.models.ConcertSector;

public class ConcertOutput {

    private String artist;

    private String place;
    
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT-03:00")
    private Date concertDate;
    
    private Time concertTime;
    
    private List<ConcertSector> sectors;

    public ConcertOutput() {
    }

    public void setConcertSector(List<ConcertSector> sectors) {
        this.sectors = sectors;
    }

    public List<ConcertSector> getConcertSector() {
        return this.sectors;
    }


    public void setConcertTime( Time time ) {
        this.concertTime = time;
    }

    public Date getConcertTime() {
        return this.concertTime;
    }

    public void setConcertDate( Date date ) {
        this.concertDate = date;
    }

    public Date getConcertDate() {
        return this.concertDate;
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
}
