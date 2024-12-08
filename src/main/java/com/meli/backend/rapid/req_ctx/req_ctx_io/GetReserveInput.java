package com.meli.backend.rapid.req_ctx.req_ctx_io;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GetReserveInput {

    private int reserveId;

    private String artist;

    private String place;
    
    /** The concert date. */
    @JsonFormat(timezone = "GMT-03:00")
    private Date concertDate;

    private String sector;
    
    private String name;

    private String surname;

    private long dni;

    public GetReserveInput() {
    }

    public void setReserveId( int reserveId ) {
        this.reserveId = reserveId;
    }

    public int getReserveId() {
        return this.reserveId;
    }
    
    public void setDNI( Long dni ) {
        this.dni = dni;
    }

    public Long getDNI() {
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
    
    public void setSector( String sector ) {
        this.sector = sector;
    }

    public String getSector() {
        return this.sector;
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

    public void setConcertDate( Date concertDate ) {
        this.concertDate = concertDate;
    }

    public Date getConcertDate( ) {
        return this.concertDate;
    }
}
