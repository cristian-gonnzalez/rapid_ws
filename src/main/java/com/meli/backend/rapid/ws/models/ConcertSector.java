package com.meli.backend.rapid.ws.models;

import java.util.*;

public class ConcertSector {

    private String name;

    private double price;

    private Integer roomSpace;

    private Integer occupiedSpace;

    private Boolean hasSeat;

    private List<Integer> seats;

    public ConcertSector() {
        this.seats = new ArrayList<>();
    }

    public void setPrice(double price ) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public void addSeat(Integer seatnum ) {
        this.seats.add(seatnum);
    }

    public void setSeats(List<Integer>  seats) {
        this.seats = seats;
    }

    public List<Integer>  getSeats() {
        return this.seats;
    }

    public void setRoomSpace(Integer roomSpace ) {
        this.roomSpace = roomSpace;
    }
    
    public Integer getRoomSpace() {
        return this.roomSpace;
    }
    
    public void setOccupiedSpace(Integer occupiedSpace ) {
        this.occupiedSpace = occupiedSpace;
    }
    public Integer getOccupiedSpace() {
        return this.occupiedSpace;
    }

    public void setHasSeat(Boolean hasSeat ) {
        this.hasSeat = hasSeat;
    }
    public Boolean getHasSeat() {
        return this.hasSeat;
    }

    public void setName(String name ) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}


