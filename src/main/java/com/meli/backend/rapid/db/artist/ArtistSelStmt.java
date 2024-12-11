package com.meli.backend.rapid.db.artist;

import com.meli.backend.rapid.db.SqlStmt;

public class ArtistSelStmt extends SqlStmt  {

    public ArtistSelStmt() {
        super();
        this.sql =  prepareSqlQuery();
    }

    public String prepareSqlQuery( ) {
        String sql = "select artistId, name "  +
                     " from Artist ";
                     
        return  sql;
    }

    public void setArtist( String name ) {
        String wsql = " name = '" +  name + "' ";
        wheresql.add(wsql);
    }
}
