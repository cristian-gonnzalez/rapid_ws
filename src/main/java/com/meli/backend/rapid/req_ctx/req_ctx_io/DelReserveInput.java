package com.meli.backend.rapid.req_ctx.req_ctx_io;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DelReserveInput {
     
    /** The artist name. */
    @JsonProperty(required = true)
    private Integer reserveId;

    /** The artist name. */
    @JsonProperty(required = true)
    private String artist;

    /** The place name. */
    @JsonProperty(required = true)
    private String place;
    
    /** The concert date. */
    @JsonFormat(timezone = "GMT-03:00")
    @JsonProperty(required = true)
    private Date concertDate;

    /** The sector name. */
    @JsonProperty(required = true)
    private String sector;

    /** List of seats to reserve. 
     * If you use this field, do not sent the qty field. */
    private List<Integer> seats;

    
    public DelReserveInput() {
    }
    
    public void setReserveId( Integer  reserveId ) {
        this.reserveId = reserveId;
    }

    public Integer  getReserveId() {
        return this.reserveId;
    }
    
    public void setSeats( List<Integer>  seats ) {
        this.seats = seats;
    }

    public List<Integer>  getSeats() {
        return this.seats;
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
