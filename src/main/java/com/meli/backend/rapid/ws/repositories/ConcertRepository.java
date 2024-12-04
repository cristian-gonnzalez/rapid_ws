package com.meli.backend.rapid.ws.repositories;

import java.sql.*;
import java.util.*;
import java.sql.Date;

import com.meli.backend.rapid.common.DataBase;
import com.meli.backend.rapid.req_ctx.*;
import com.meli.backend.rapid.req_ctx.req_ctx_io.*;
import com.meli.backend.rapid.ws.models.ConcertSector;

public class ConcertRepository {
    
    ConcertSectorRepository sectorRepository;
     
    public ConcertRepository() {
        sectorRepository = new ConcertSectorRepository();    
    }

    /* Gets the id.
     *
     * @param field_name The field.
     * @param value The value.
     * @param table The table name.
     * @return the ID if found, -1 otherwise
     * 
     */
    private int getIDQry( String field_name, String value, String table ) {
        DataBase db = new DataBase();
        int id = -1;
     
        db.connect();
        db.prepareStmt();
        String querystr = "select "+ field_name + " from " + table + " where name = '" + value +"';";
        System.out.println(querystr);

        try {
            ResultSet rs = db.execute(querystr);
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeStmt();
        db.disconnect();
        
        return id;
    }
    
    /** Gets the artist ID
     * 
     * @param artist the artist name
     * @return the ID if found, -1 otherwise
     */
    public int getPlaceID(String place) {
        return getIDQry( "placeId", place, "Place" );
    }

    /** Gets the artist ID
     * 
     * @param artist the artist name
     * @return the ID if found, -1 otherwise
     */
    public int getArtistID(String artist) {
        return getIDQry( "artistId", artist, "Artist" );
    }
    
    /** Gets the sector ID.
     * 
     * @param artist The artist name
     * @param place The place name
     * @param concertDate The concert date
     * @param sector    The concert sector name
     * @return  the ID if found, -1 otherwise
     */
    public int getSectorID( int artistId, int placeId, Date concertDate, String sector ) {
        
        DataBase db = new DataBase();
        int sectorId = -1;

        db.connect();
        db.prepareStmt();
        String querystr = "select sectorId from ConcertSector " + 
                          " where artistId =" + artistId + "  and " + 
                          " placeId = " + placeId + "  and " + 
                          " concertDate = '" + concertDate.toString() + "'  and " + 
                          " name = '" + sector + "';";
        System.out.println(querystr);

        try {
            ResultSet rs = db.execute(querystr);
            
            while (rs.next()) {
                sectorId = rs.getInt("sectorId");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeStmt();
        db.disconnect();
        return sectorId;
    }

    /** Checks wheter the concert exist
     * 
     * @param artist The artist name
     * @param place The place name
     * @param concertDate The concert date
     * @param sector    The concert sector name
     * @return  true if the concert exists, false otherwise
     */
    public Boolean existsConcert( String artist, String place, Date concertDate, String sector ) {
        
        int artistId = getArtistID(artist);
        int placeId = getPlaceID(place);
        if( artistId == -1 || placeId == -1 )
            return false;
        
        int sectorId = getSectorID( artistId, placeId, concertDate, sector );
        
        return (sectorId != -1);
    }

    /** Gets the concerts
     * 
     * @param rg Input request.
     * @param wheresql Where sql clause.
     * @param limitsql Limit sql clause.
     * @return The list of concerts.
     */
    private List<ConcertOutput> _getConcerts( ConcertRangeInput rg, String wheresql, String limitsql )  {
        
        List<ConcertOutput> concerts = new ArrayList<>();
      
        DataBase db = new DataBase();

        db.connect();
        db.prepareStmt();
        String querystr = "select a.artistId, a.name, p.placeId, p.name, c.concertDate, c.hour " +
                          "from concert as c, " +
                                "place as p, " +
                                "artist as a " +
                          "where p.placeId = c.placeId and " +
                                "a.artistId = c.artistId " + wheresql + limitsql;
        System.out.println(querystr);

        try {
            ResultSet rs = db.execute(querystr);
            
            while (rs.next()) {

                int artistId = rs.getInt("artistId");
                String artistName = rs.getString(2);
                int placeId = rs.getInt("placeId");
                String placeName = rs.getString(4);
                Date concerDate = rs.getDate("concertDate");
                
                ConcertOutput row = new ConcertOutput();
                
                row.setArtist( artistName );
                row.setPlace( placeName );
                row.setConcertDate( concerDate );
                row.setConcertTime( rs.getTime("hour") );

                List<ConcertSector> sectors = sectorRepository.getConcertSectors( rg, artistId, placeId, concerDate, "");
                row.setConcertSector(sectors);

                concerts.add(row);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeStmt();
        db.disconnect();

        return concerts;
    }

    /** Gets the concert by range 
     * 
     * @param ctx Context.
     * @return The list of concerts.
     */
    public List<ConcertOutput> getConcertsByRange(ConcertRgReqCtx ctx) {
         
        /* Builds the where sql clause to add in the query */
        String wheresql = "";
        if( ctx.input.getArtist() != null ) {
            wheresql += " and a.name = '" +  ctx.input.getArtist() + "' ";
        }

        if( ctx.input.getPlace() != null ) {
            wheresql += " and p.name = '" +  ctx.input.getPlace() + "' ";
        }

        if( ctx.input.getFromDate() != null ) {
            wheresql += " and c.concertDate >= '" +  ctx.input.getFromDate() + "' ";
        }
            
        if( ctx.input.getUntilDate() != null ) {
            wheresql += " and c.concertDate <= '" +  ctx.input.getUntilDate() + "' ";
        }

        if( ctx.input.getDateASC() != null ) {
            wheresql += " order by c.concertDate asc ";
        }
        else {
            wheresql += " order by c.concertDate DESC ";
        }

        String limitsql = " limit " + ctx.recOutParam.getOffset() + " offset " +  ctx.recOutParam.getRecNum() +  " ";

        List<ConcertOutput> concerts = _getConcerts( ctx.input, wheresql, limitsql );        
        return concerts;
    }

    /** Gets the concerts.
     * 
     * @param ctx Context
     * @return List of concerts
     */
    public List<ConcertOutput> getConcerts(ConcertReqCtx ctx) {

            String wheresql = "";
            if( ctx.input.getArtist() != null ) {
                wheresql += " and a.name = '" +  ctx.input.getArtist() + "' ";
            }

            if( ctx.input.getPlace() != null ) {
                wheresql += " and p.name = '" +  ctx.input.getPlace() + "' ";
            }

            if( ctx.input.getConcertDate() != null ) {
                wheresql += " and c.concertDate = '" +  ctx.input.getConcertDate() + "' ";
            }
            
            String limitsql = " limit " + ctx.recOutParam.getOffset() + " offset " +  ctx.recOutParam.getRecNum() +  " ";

            List<ConcertOutput> concerts = _getConcerts( null, wheresql, limitsql );
            return concerts;
    }
}
