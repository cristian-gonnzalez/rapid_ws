package com.meli.backend.rapid.req_ctx.concert;


import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/** Contains the request body for a concert object. */
public class ConcertInput {
    
    String artist;
    
    String place;

    @JsonFormat(timezone = "GMT-03:00")
    Date concertDate;

    public ConcertInput() {
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
}
