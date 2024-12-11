package com.meli.backend.rapid.db.concert;


import com.meli.backend.rapid.db.InsertSqlStmt;
import com.meli.backend.rapid.ws.models.ConcertRecord;

public class ConcertInsertStmt extends InsertSqlStmt  {


    public ConcertInsertStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        
        String sql = "insert into Concert (`artistId`, `placeId`, `concertDate`, `hour`) values "; 
        return  sql;
    }

    public void add( ConcertRecord record ) {
        String value = "( "+ record.getConcerKey().getArtistId() + ", " + 
                             record.getConcerKey().getPlaceId()  + ", "  + 
                       "'" + record.getConcerKey().getConcertDate() + "'," + 
                       "'" + record.getTime() + "' )";
        values.add(value);
    }
}
