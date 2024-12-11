package com.meli.backend.rapid.db.sector;

import java.sql.Date;

import com.meli.backend.rapid.db.DelSqlStmt;

public class SectorDelStmt extends DelSqlStmt  {

    public SectorDelStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        
        String sql = "delete from ConcertSector "; 
        return  sql;
    }

    public void setDate( Date d ) {
        String wsql = " concertDate = '" +  d + "' ";
        wheresql.add(wsql);
    }

    public void setArtistId( int artistId ) {
        String wsql = " artistId = " +  artistId + " ";
        wheresql.add(wsql);
    }

    public void setPlaceId( int placeId ) {
        String wsql = " placeId = " +  placeId + " ";
        wheresql.add(wsql);
    }

    public void setSectorId(int sectorId) {
        String wsql = " sectorId = " +  sectorId + " ";
        wheresql.add(wsql);
    }

}
