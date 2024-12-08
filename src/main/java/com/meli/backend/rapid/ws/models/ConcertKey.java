package com.meli.backend.rapid.ws.models;

import java.sql.Date;

public class ConcertKey {

    int artistId;

    int placeId;

    Date concertDate;

    public ConcertKey() {
    }

    public void setArtistId( int artist ) {
        this.artistId = artist;
    }

    public int getArtistId() {
        return this.artistId;
    }

    public void setPlaceId( int placeId ) {
        this.placeId = placeId;
    }

    public int getPlaceId() {
        return this.placeId;
    }

    public void setConcertDate( Date date ) {
        this.concertDate = date;
    }

    public Date getConcertDate() {
        return this.concertDate;
    }
}
