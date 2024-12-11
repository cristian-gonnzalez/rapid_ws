package com.meli.backend.rapid.db.concert;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.RecordReader;
import com.meli.backend.rapid.db.sector.SectorSql;
import com.meli.backend.rapid.db.sector.SectorStmt;
import com.meli.backend.rapid.req_ctx.concert.ConcertRgRequestContext;
import com.meli.backend.rapid.ws.models.ConcertKey;
import com.meli.backend.rapid.ws.models.ConcertRecord;
import com.meli.backend.rapid.ws.models.SectorRecord;

public class ConcertSqlRecReader implements RecordReader {
    
    private List<SectorRecord> readSectors(ConcertKey concertKey, Object args) throws SQLException {

        SectorStmt sectorstmt = new SectorStmt();
        sectorstmt.setArtistId(concertKey.getArtistId());
        sectorstmt.setPlaceId(concertKey.getPlaceId());
        sectorstmt.setDate(concertKey.getConcertDate());

        if( args != null ) {
            ConcertRgRequestContext ctx = (ConcertRgRequestContext)args;
            if( ctx.input.getFromPrice() != null || ctx.input.getFromPrice() != null ) {
                sectorstmt.setPriceRange(ctx.input.getFromPrice(), ctx.input.getFromPrice());
            }
            if( ctx.input.getPriceASC() != null)
                sectorstmt.setPriceASC(ctx.input.getPriceASC());;
        }
        
        DataBase new_db = new DataBase();
        new_db.connect();
      
        SectorSql sectorSql = new SectorSql(new_db, sectorstmt);
        List<SectorRecord> sectors = sectorSql.read();
      
        new_db.disconnect();

        return sectors;
    }


    public void read(ResultSet rs, List<Object> objects, Object args) throws SQLException {
    
        int artistId = rs.getInt("artistId");
        String artistName = rs.getString(2);
        int placeId = rs.getInt("placeId");
        String placeName = rs.getString(4);
        Date concerDate = rs.getDate("concertDate");
        Time hour = rs.getTime("hour");

        ConcertKey concertKey = new ConcertKey();
        concertKey.setArtistId(artistId);
        concertKey.setPlaceId(placeId);
        concertKey.setConcertDate(concerDate);

        ConcertRecord rec = new ConcertRecord();
        rec.setConcerKey(concertKey);
        rec.setArtist( artistName );
        rec.setPlace( placeName );
        rec.setTime(hour);
        
        List<SectorRecord> sectors = readSectors(concertKey, args);
        rec.setSectors(sectors);
        
        objects.add(rec);
    }
}  
