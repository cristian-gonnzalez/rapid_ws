package com.meli.backend.rapid.db;

import java.sql.Date;

public class ReserveStmt extends SqlStmt  {

    public ReserveStmt() {
        this.sql =  prepareSqlQuery();
        String wsql = " r.artistId = a.artistId  and " +
                      " r.placeId = p.placeId  and " +
                      " r.artistId = c.artistId  and r.placeId = c.placeId and r.concertDate = c.concertDate and " +
                      " r.artistId = cs.artistId  and r.placeId = cs.placeId and r.concertDate = cs.concertDate and r.sectorId = cs.sectorId ";
        this.wheresql.add(wsql);
    }

    public String prepareSqlQuery( ) {
        String sql = "select r.datetime, r.reserveId, r.artistId, r.placeId, r.concertDate, r.sectorId, " +
                             "r.qty, r.name, r.surname, r.dni, r.total "  +
                     " from Reserve as r, Concert as c, Place as p, Artist as a, ConcertSector as cs ";
                     
        return  sql;
    }

    public void setReserveId( int reserveId ) {
        String wsql = " r.reserveId = " +  reserveId + " ";
        wheresql.add(wsql);
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
  
    public void setSector( String name ) {
        String wsql = " cs.name = '" +  name + "' ";
        wheresql.add(wsql);
    }


    public void setUsername( String name ) {
        String wsql = " r.name = '" +  name + "'' ";
        wheresql.add(wsql);
    }

    public void setUsersurname( String surname ) {
        String wsql = " r.surname = '" +  surname + "'' ";
        wheresql.add(wsql);
    }

    public void setDNI( Long dni ) {
        String wsql = " r.dni = " +  dni + " ";
        wheresql.add(wsql);
    }
}

