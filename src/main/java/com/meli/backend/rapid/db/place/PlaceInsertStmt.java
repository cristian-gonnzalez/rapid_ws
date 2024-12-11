package com.meli.backend.rapid.db.place;

import com.meli.backend.rapid.db.InsertSqlStmt;
import com.meli.backend.rapid.ws.models.PlaceRecord;

public class PlaceInsertStmt   extends InsertSqlStmt  {


    public PlaceInsertStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        
        String sql = "insert into Place (`name`) values "; 
        return  sql;
    }

    public void add( PlaceRecord record ) {
        String value = "( '"+ record.getName() + "' )";
        values.add(value);
    }

}
