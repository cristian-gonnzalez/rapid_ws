package com.meli.backend.rapid.db.place;

import com.meli.backend.rapid.db.DelSqlStmt;

public class PlaceDelStmt extends DelSqlStmt  {

    public PlaceDelStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        
        String sql = "delete from Place "; 
        return  sql;
    }

    public void addName( String name ) {
        String s = " name = '" + name + "' ";
        wheresql.add(s);
    }

}
