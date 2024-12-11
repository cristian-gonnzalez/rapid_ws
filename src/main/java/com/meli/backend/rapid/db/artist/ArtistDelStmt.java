package com.meli.backend.rapid.db.artist;

import com.meli.backend.rapid.db.DelSqlStmt;

public class ArtistDelStmt extends DelSqlStmt  {

    public ArtistDelStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        
        String sql = "delete from Artist "; 
        return  sql;
    }

    public void addName( String name ) {
        String s = " name = '" + name + "' ";
        wheresql.add(s);
    }
}
