package com.meli.backend.rapid.db;

import java.sql.Date;

public class ConcertStmt extends SqlStmt  {

    public ConcertStmt() {
        this.sql =  prepareSqlQuery();
        this.wheresql.add(" p.placeId = c.placeId and a.artistId = c.artistId ");
    }

    public String prepareSqlQuery( ) {
        String sql = "select a.artistId, a.name, p.placeId, p.name, c.concertDate, c.hour " +
                     "from concert as c, place as p, artist as a ";
        return  sql;
    }

    public void setArtist( String name ) {
        String wsql = " a.name = '" +  name + "' ";
        wheresql.add(wsql);
    }

    public void setPlace( String name ) {
        String wsql = " p.name = '" +  name + "' ";
        wheresql.add(wsql);
    }

    public void setDate( Date d ) {
        String wsql = " c.concertDate = '" +  d + "' ";
        wheresql.add(wsql);
    }

    public void setArtistId( int artistId ) {
        String wsql = " a.artistId = " +  artistId + " ";
        wheresql.add(wsql);
    }

    public void setPlaceId( int placeId ) {
        String wsql = " p.placeId = " +  placeId + " ";
        wheresql.add(wsql);
    }

    public void setDateRange( Date from, Date until ) {
        String wsql = "";        
        if( from != null) {
             wsql += " c.concertDate >= '" +  from + "' ";
             if( until != null ) {
                wsql += " and ";
             }
        }
        if( until != null ) {
            wsql += " c.concertDate <= '" +  until + "' ";
        }
        wheresql.add(wsql);
    }

    public void setDateADC( Boolean value ) {
        orderby = " order by c.concertDate desc ";
    
        if( value )
            orderby = " order by c.concertDate asc ";
    }
}
