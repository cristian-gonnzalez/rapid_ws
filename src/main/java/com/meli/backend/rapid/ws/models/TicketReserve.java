package com.meli.backend.rapid.ws.models;

import java.sql.Date;
import java.util.List;

public class TicketReserve {

    private Long datetime;

    private int reserveId;

    private Concert concert;
    
    private Total total;

    private User onUser;

    public TicketReserve() {
        this.concert = new Concert();
        this.total = new Total();
        this.onUser = new User();
    }

    public void setUser( User onUser ) {
        this.onUser = onUser;
    }

    public User getUser() {
        return this.onUser;
    }

    public void setTotalInfo( Total total ) {
        this.total = total;
    }

    public Total getTotalInfo() {
        return this.total;
    }

    public void setConcertInfo( Concert concert ) {
        this.concert = concert;
    }

    public Concert getConcertInfo() {
        return this.concert;
    }

    public void setDatetime( Long datetime ) {
        this.datetime = datetime;
    }

    public Long getDatetime() {
        return this.datetime;
    }

    public void setReserveId( int reserveId ) {
        this.reserveId = reserveId;
    }

    public int getReserveId() {
        return this.reserveId;
    }

    public class Concert {

        private String artist;

        private String place;
    
        private Date concerDate;
        
        private String sector;

        List<Integer> seats;

        public Concert() {
        }
        
        public List<Integer> getSeats() {
            return this.seats;
        }
        public void setSeats(List<Integer> seats) {
            this.seats = seats;
        }

        public Date getConcerDate() {
            return this.concerDate;
        }
        public void setConcerDate(Date concerDate) {
            this.concerDate = concerDate;
        }

        public String getSector() {
            return this.sector;
        }
        public void setSector(String sector) {
            this.sector = sector;
        }

        public String getArtist() {
            return this.artist;
        }
        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getPlace() {
            return this.place;
        }
        public void setPlace(String place) {
            this.place = place;
        }
    
    }

    public class Total {

        private int qty;

        private double price;
    
        private double total;
        
        public Total() {
        }

        public void setTotal( double total) {
            this.total = total;
        }
        public double getTotal() {
            return this.total;
        }
        public void setPrice( double price) {
            this.price = price;
        }
        public double getPrice() {
            return this.price;
        }
        public void setQuantity( int qty) {
            this.qty = qty;
        }
        public int getQuantity() {
            return this.qty;
        }
    } 

    public class User {

        private String name;

        private String surname;
    
        private double dni;

        public User() {
        }

        public void setDNI( double dni ) {
            this.dni = dni;
        }
        public double getDNI() {
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
    }
}
