package com.meli.backend.rapid.db.artist;


import com.meli.backend.rapid.db.InsertSqlStmt;
import com.meli.backend.rapid.ws.models.ArtistRecord;

public class ArtistInsertStmt  extends InsertSqlStmt  {


    public ArtistInsertStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        
        String sql = "insert into Artist (`name`) values "; 
        return  sql;
    }

    public void add( ArtistRecord record ) {
        String value = "( '"+ record.getName() + "' )";
        values.add(value);
    }
}
