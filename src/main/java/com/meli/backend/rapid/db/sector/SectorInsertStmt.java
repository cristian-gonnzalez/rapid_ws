package com.meli.backend.rapid.db.sector;


import com.meli.backend.rapid.db.InsertSqlStmt;
import com.meli.backend.rapid.ws.models.SectorRecord;

public class SectorInsertStmt  extends InsertSqlStmt  {

    public SectorInsertStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        
        String sql = "insert into ConcertSector (`artistId`, `placeId`, `concertDate`, `sectorId`, "+ 
                                                "`name`, `roomSpace`, `occupiedSpace`, `hasSeat`, `price` ) values "; 
        return  sql;  
    }

    public void add( SectorRecord record ) {
        int hasSet = 1;
        if( !record.getHasSeat())
            hasSet = 0;
        String value = "( "+ record.getSetorKey().getConcerKey().getArtistId() + ", " + 
                             record.getSetorKey().getConcerKey().getPlaceId()  + ", "  + 
                        "'" + record.getSetorKey().getConcerKey().getConcertDate() + "'," +
                            + record.getSetorKey().getSectorId() + "," + 
                        "'" + record.getName() + "'," + 
                            + record.getRoomSpace() + "," + 
                            + record.getOccupiedSpace() + "," + 
                            + hasSet + "," + 
                            + record.getPrice() + " )";
        values.add(value);
    }
}
