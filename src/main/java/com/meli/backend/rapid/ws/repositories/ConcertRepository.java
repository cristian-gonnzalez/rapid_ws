package com.meli.backend.rapid.ws.repositories;

import java.sql.*;
import java.util.*;

import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.concert.ConcertDelStmt;
import com.meli.backend.rapid.db.concert.ConcertInsertStmt;
import com.meli.backend.rapid.db.concert.ConcertSql;
import com.meli.backend.rapid.db.concert.ConcertStmt;
import com.meli.backend.rapid.db.sector.SectorDelStmt;
import com.meli.backend.rapid.db.sector.SectorInsertStmt;
import com.meli.backend.rapid.req_ctx.concert.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.concert.ConcertRgRequestContext;
import com.meli.backend.rapid.ws.models.ConcertKey;
import com.meli.backend.rapid.ws.models.ConcertRecord;
import com.meli.backend.rapid.ws.models.SectorKey;
import com.meli.backend.rapid.ws.models.SectorRecord;

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


    public void saveConcert(ConcertRequestContext ctx, ConcertRecord record) throws SQLException {
        ConcertInsertStmt stmt = new ConcertInsertStmt();
        stmt.add(record);
        String sql = stmt.build();

        System.out.println("Saving concert: " + sql);

        DataBase db = new DataBase();
        db.connect();
        db.prepareStmt();
        boolean r = db.executeUpdte(sql);
        db.closeStmt();
        db.disconnect();
        
        if( !r ) {
            ctx.setError(eRCode.internalError, "Failed to insert Artist");
        }
    }


    public void deleteConcert(ConcertRequestContext ctx, ConcertKey key) throws SQLException {
            
        ConcertDelStmt concertStmt = new ConcertDelStmt();
        concertStmt.setArtistId(key.getArtistId());
        concertStmt.setPlaceId(key.getPlaceId());
        concertStmt.setDate(key.getConcertDate());

        SectorDelStmt sectorStmt = new SectorDelStmt();
        sectorStmt.setArtistId(key.getArtistId());
        sectorStmt.setPlaceId(key.getPlaceId());
        sectorStmt.setDate(key.getConcertDate());
        
        String sectorSql = sectorStmt.build();
        String concertSql = concertStmt.build();

        boolean r = false;

        DataBase db = new DataBase();
        db.connect();
        db.begin();

        db.prepareStmt();
        System.out.println(sectorSql);
        db.executeUpdte(sectorSql);
        db.closeStmt();
        
        db.prepareStmt();
        System.out.println(concertSql);
        r = db.executeUpdte(concertSql);
        db.closeStmt();
        if( !r ) {
            ctx.setError(eRCode.internalError, "Failed to delete concert");
            db.rollback();
            db.disconnect();
            return;
        }

        db.commit();
        db.disconnect();
    }


    public void saveSector(ConcertRequestContext ctx, SectorRecord record) throws SQLException {
        
        SectorInsertStmt stmt = new SectorInsertStmt();
        stmt.add(record);
        String sql = stmt.build();

        System.out.println("Saving concert: " + sql);

        DataBase db = new DataBase();
        db.connect();
        db.prepareStmt();
        boolean r = db.executeUpdte(sql);
        db.closeStmt();
        db.disconnect();
        
        if( !r ) {
            ctx.setError(eRCode.internalError, "Failed to insert Sectr");
        }
    }


    public void deleteSector(ConcertRequestContext ctx, SectorKey key) throws SQLException {
    
        SectorDelStmt sectorStmt = new SectorDelStmt();
        sectorStmt.setArtistId(key.getConcerKey().getArtistId());
        sectorStmt.setPlaceId(key.getConcerKey().getPlaceId());
        sectorStmt.setDate(key.getConcerKey().getConcertDate());
        sectorStmt.setSectorId(key.getSectorId());
        
        String sectorSql = sectorStmt.build();
 
        boolean r = false;

        DataBase db = new DataBase();
        db.connect();

        db.prepareStmt();
        System.out.println(sectorSql);
        r = db.executeUpdte(sectorSql);
        db.closeStmt();
        
        if( !r ) {
            ctx.setError(eRCode.internalError, "Failed to delete sector");
            db.rollback();
            db.disconnect();
            return;
        }
        db.disconnect();
    }
}
