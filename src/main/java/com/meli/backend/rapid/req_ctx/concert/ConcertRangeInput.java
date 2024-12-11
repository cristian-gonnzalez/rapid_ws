package com.meli.backend.rapid.req_ctx.concert;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ConcertRangeInput {
        
        private String artist;
        
        private String place;
        
        @JsonFormat(timezone = "GMT-03:00")
        private Date fromDate;
        
        @JsonFormat(timezone = "GMT-03:00")
        private Date untilDate;
    
        @JsonFormat(timezone = "GMT-03:00")
        private Double fromPrice;
        
        @JsonFormat(timezone = "GMT-03:00")
        private Double untilPrice;

        private Boolean dateASC;

        private Boolean priceASC;
    
        public ConcertRangeInput() {
            this.dateASC = true;
            this.priceASC = true;
        }

        public void setPriceASC( Boolean priceASC ) {
            this.priceASC = priceASC;
        }
    
        public Boolean getPriceASC() {
            return this.priceASC;
        }

        public void setDateASC( Boolean dateASC ) {
            this.dateASC = dateASC;
        }
    
        public Boolean getDateASC() {
            return this.dateASC;
        }
    
        public void setFromPrice( Double fromPrice ) {
            this.fromPrice = fromPrice;
        }
    
        public Double getFromPrice() {
            return this.fromPrice;
        }
        
        public void setUntilPrice( Double untilPrice ) {
            this.untilPrice = untilPrice;
        }
    
        public Double getUntilPrice() {
            return this.untilPrice;
        }

        public void setUntilDate( Date untilDate ) {
            this.untilDate = untilDate;
        }
    
        public Date getUntilDate() {
            return this.untilDate;
        }
    
        public void setFromDate( Date fromDate ) {
            this.fromDate = fromDate;
        }
    
        public Date getFromDate() {
            return this.fromDate;
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
