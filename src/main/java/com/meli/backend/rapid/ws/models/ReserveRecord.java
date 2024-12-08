package com.meli.backend.rapid.ws.models;


public class ReserveRecord {

    private ReserveKey reserveKey; 
    
    private Long datetime;

    private int quantity;

    private Double total_amount;

    private User user;

    private ConcertRecord concert;
    
    public ReserveRecord() {

    }

    public void setReserveKey( ReserveKey reserveKey ) {
        this.reserveKey = reserveKey;
    }

    public ReserveKey getReserveKey() {
        return this.reserveKey;
    }

    public void setConcert( ConcertRecord concert ) {
        this.concert = concert;
    }

    public ConcertRecord getConcert() {
        return this.concert;
    }

    public void setDatetime( Long datetime ) {
        this.datetime = datetime;
    }

    public Long getDatetime() {
        return this.datetime;
    }

    public void setQuantity( int qty) {
        this.quantity = qty;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setTotalAmoun( Double total_amount ) {
        this.total_amount = total_amount;
    }

    public Double geTotalAmount() {
        return this.total_amount;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }    
}
