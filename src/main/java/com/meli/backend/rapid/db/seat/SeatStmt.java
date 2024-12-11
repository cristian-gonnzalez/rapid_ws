package com.meli.backend.rapid.db.seat;

import java.sql.Date;

import com.meli.backend.rapid.db.SqlStmt;

public class SeatStmt extends SqlStmt {
    
    public SeatStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        String sql = "select seatNumber from Seat as s ";
        return  sql;
    }
 
    public void setReserveId( int reserveId ) {
        String wsql = " s.reserveId = " + reserveId + " ";
        wheresql.add(wsql);
    }

    public void setArtistId( int artistId ) {
        String wsql = " s.artistId = " + artistId + " ";
        wheresql.add(wsql);
    }

    public void setPlaceId( int placeId ) {
        String wsql = " s.placeId = " + placeId + " ";
        wheresql.add(wsql);
    }

    public void setDate( Date d ) {
        String wsql = " s.concertDate = '" +  d + "' ";
        wheresql.add(wsql);
    }

    public void setSectorId( int sectorId ) {
        String wsql = " s.sectorId = " +  sectorId + " ";
        wheresql.add(wsql);
    }
}
