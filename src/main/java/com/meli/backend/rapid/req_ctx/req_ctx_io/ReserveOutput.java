package com.meli.backend.rapid.req_ctx.req_ctx_io;

import java.sql.Date;

import com.meli.backend.rapid.ws.models.User;


public class ReserveOutput {

    private Long datetime;

    private int reserveId;

    private ConcertInfo concert;
    
    private Total total;

    private User user;

    public ReserveOutput() {
        this.concert = new ConcertInfo();
        this.total = new Total();
        this.user = new User();
    }

    public void setUserInfo( User user ) {
        this.user = user;
    }

    public User getUserInfo() {
        return this.user;
    }

    public void setTotalInfo( Total total ) {
        this.total = total;
    }

    public Total getTotalInfo() {
        return this.total;
    }

    public void setConcertInfo( ConcertInfo concert ) {
        this.concert = concert;
    }

    public ConcertInfo getConcertInfo() {
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

    public class ConcertInfo {

        private String artist;

        private String place;
    
        private Date concertDate;
        
        private SectorOutput sector;

        public ConcertInfo() {
        }
        
        public Date getConcertDate() {
            return this.concertDate;
        }
        public void setConcertDate(Date concertDate) {
            this.concertDate = concertDate;
        }

        public SectorOutput getSector() {
            return this.sector;
        }
        public void setSector(SectorOutput sector) {
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

}
