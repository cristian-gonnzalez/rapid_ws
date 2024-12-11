/**
 *  This files contains the input 
 */
package com.meli.backend.rapid.req_ctx.reserve;

import java.util.*;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


/** Reserve input request 
 * 
 */
public class ReserveInput {

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

    /** Quantity of the space for sector that has no seat. 
     * If you use this field, do not sent the seats field. */
    private Integer qty;

    /** User name. */
    @JsonProperty(required = true)
    private String name;

    /** User surname. */
    @JsonProperty(required = true)
    private String surname;

    /** User id. */
    @JsonProperty(required = true)
    private long dni;

    public ReserveInput() {
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
    
    public void setQuantity( Integer  qty ) {
        this.qty = qty;
    }

    public Integer  getQuantity() {
        return this.qty;
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
