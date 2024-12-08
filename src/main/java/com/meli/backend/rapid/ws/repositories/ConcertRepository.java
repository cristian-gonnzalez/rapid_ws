package com.meli.backend.rapid.ws.repositories;

import java.sql.*;
import java.util.*;

import com.meli.backend.rapid.db.ConcertSql;
import com.meli.backend.rapid.db.ConcertStmt;
import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.req_ctx.*;
import com.meli.backend.rapid.ws.models.ConcertRecord;

public class ConcertRepository {
         
    public ConcertRepository() {
    }


    /** Gets the concert by range 
     * 
     * @param ctx Context.
     * @return The list of concerts.
     */
    public List<ConcertRecord> getConcertsByRange(ConcertRgRequestContext ctx) throws SQLException {

        ConcertStmt stmt = new ConcertStmt();

        if( ctx.input.getArtist() != null ) {
            stmt.setArtist(ctx.input.getArtist() ); 
        }
        if( ctx.input.getPlace() != null ) {
            stmt.setPlace(ctx.input.getPlace() ); 
        }
        if( ctx.input.getFromDate() != null || ctx.input.getUntilDate() != null ) {
            stmt.setDateRange(ctx.input.getFromDate(), ctx.input.getUntilDate());
        }
        if( ctx.input.getDateASC() != null ) {
            stmt.setDateADC( ctx.input.getDateASC() );
        }

        stmt.setLimit( ctx.reqParam.getOffset(), ctx.reqParam.getRecNum() );
        
        DataBase db = new DataBase();
        db.connect();
        ConcertSql sql = new ConcertSql(db, stmt);
        sql.setArgs( ctx );
        List<ConcertRecord> concerts = sql.read();
        db.disconnect();

        return concerts;
    }


    /** Gets the concerts.
     * 
     * @param ctx Context
     * @return List of concerts
    * @throws SQLException 
    */
    public List<ConcertRecord> getConcerts( ConcertRequestContext ctx) throws SQLException {

        ConcertStmt stmt = new ConcertStmt();

        if( ctx.input.getArtist() != null ) {
            stmt.setArtist(ctx.input.getArtist() ); 
        }
        if( ctx.input.getPlace() != null ) {
            stmt.setPlace(ctx.input.getPlace() ); 
        }
        if( ctx.input.getConcertDate() != null ) {
            stmt.setDate(ctx.input.getConcertDate() ); 
        }

        stmt.setLimit( ctx.reqParam.getOffset(), ctx.reqParam.getRecNum() );
        
        DataBase db = new DataBase();
        db.connect();
        ConcertSql sql = new ConcertSql(db, stmt);
        List<ConcertRecord> concerts = sql.read();
        db.disconnect();

        return concerts;
    }
}
