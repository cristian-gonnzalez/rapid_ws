package com.meli.backend.rapid.db.place;

import com.meli.backend.rapid.db.SqlStmt;

public class PlaceSelStmt extends SqlStmt  {

    public PlaceSelStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        String sql = "select placeId, name "  +
                     " from Place ";
                     
        return  sql;
    }

    public void setPlace( String name ) {
        String wsql = " name = '" +  name + "' ";
        wheresql.add(wsql);
    }

}
