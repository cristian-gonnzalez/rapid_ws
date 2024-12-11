package com.meli.backend.rapid.req_ctx.concert;


import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonFormat;

/** Contains the request body for a concert object. */
public class ConcertInput {
    
    String artist;
    
    String place;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT-03:00")
    Date concertDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    Time time;

    SectorInput sector;

    public ConcertInput() {
    }

    public void setSector( SectorInput sector ) {
        this.sector = sector;
    }

    public SectorInput getSector() {
        return this.sector;
    }

    public void setConcertDate( Date concertDate ) {
        this.concertDate = concertDate;
    }

    public Date getConcertDate() {
        return this.concertDate;
    }

    public void setArtist( String artist ) {
        this.artist = artist;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setPlace( String place ) {
        this.place = place;
    }

    public String getPlace() {
        return this.place;
    }

    public void setTime( Time time ) {
        this.time = time;
    }

    public Time getTime() {
        return this.time;
    }
}
