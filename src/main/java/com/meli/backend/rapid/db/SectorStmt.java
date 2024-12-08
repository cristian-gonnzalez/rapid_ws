package com.meli.backend.rapid.db;

import java.sql.Date;

public class SectorStmt extends SqlStmt {

    public SectorStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        String sql = "select distinct cs.name, cs.sectorId, cs.roomSpace, cs.occupiedSpace, cs.hasSeat, cs.price, " + 
        " cs.artistId, cs.placeId, cs.concertDate " + 
        " from ConcertSector cs";
        return  sql;
    }
 
    public void setArtistId( int artistId ) {
        String wsql = " cs.artistId = " + artistId + " ";
        wheresql.add(wsql);
    }

    public void setPlaceId( int placeId ) {
        String wsql = " cs.placeId = " + placeId + " ";
        wheresql.add(wsql);
    }

    public void setDate( Date d ) {
        String wsql = " cs.concertDate = '" +  d + "' ";
        wheresql.add(wsql);
    }

    public void setSector( String name ) {
        String wsql = " cs.name = " +  name + " ";
        wheresql.add(wsql);
    }

    public void setPriceRange(Double from, Double until ) {
        String s = "";
        if( from != null ) {
            s = " cs.price >= " +  from + " ";
        }
        if( until != null ) {
            if( from != null ) {
                s += " and ";
            }
            s += " cs.price =< " +  until + " ";
        }
        wheresql.add(s);
    
    }

    public void setPriceASC( Boolean value ) {
        orderby = " order by cs.price desc ";
        if( value )
            orderby = " order by c.price asc ";
    }
}
