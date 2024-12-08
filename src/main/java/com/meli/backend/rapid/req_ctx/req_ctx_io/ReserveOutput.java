package com.meli.backend.rapid.req_ctx.req_ctx_io;

import java.sql.Date;


public class ReserveOutput {

    private Long datetime;

    private int reserveId;

    private ConcertInfo concert;
    
    private Total total;

    private UserInfo user;

    public ReserveOutput() {
        this.concert = new ConcertInfo();
        this.total = new Total();
        this.user = new UserInfo();
    }

    public void setUserInfo( UserInfo user ) {
        this.user = user;
    }

    public UserInfo getUserInfo() {
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

    public class UserInfo {

        private String name;

        private String surname;
    
        private double dni;

        public UserInfo() {
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
