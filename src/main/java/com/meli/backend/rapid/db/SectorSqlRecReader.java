package com.meli.backend.rapid.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.meli.backend.rapid.ws.models.ConcertKey;
import com.meli.backend.rapid.ws.models.SectorRecord;
import com.meli.backend.rapid.ws.models.SectorKey;

public class SectorSqlRecReader implements RecordReader {
    
    public void read(ResultSet rs, List<Object> objects, Object args) throws SQLException {
                                         
        String name = rs.getString(1);
        int sectorId = rs.getInt("sectorId");
        int roomSpace = rs.getInt("roomSpace");
        int occupiedSpace = rs.getInt("occupiedSpace");
        Boolean hasSeat = (rs.getInt("hasSeat") == 1);
        double price = rs.getDouble("price");

        int artistId = rs.getInt("artistId");
        int placeId = rs.getInt("placeId");
        Date concerDate = rs.getDate("concertDate");

        ConcertKey concertKey = new ConcertKey();
        concertKey.setArtistId(artistId);
        concertKey.setPlaceId(placeId);
        concertKey.setConcertDate(concerDate);
        SectorKey sectorKey = new SectorKey();
        sectorKey.setConcerKey(concertKey);
        sectorKey.setSectorId(sectorId);

        SectorRecord rec = new SectorRecord();
        rec.setSetorKey(sectorKey);
        rec.setHasSeat( hasSeat );
        rec.setName(name);
        rec.setRoomSpace(roomSpace);
        rec.setOccupiedSpace(occupiedSpace);
        rec.setPrice(price);

        if( hasSeat ) {
            SeatStmt seatStmt = new SeatStmt();
            seatStmt.setArtistId(artistId);
            seatStmt.setDate(concerDate);
            seatStmt.setPlaceId(placeId);
            seatStmt.setSectorId(sectorId);

            DataBase newdb = new DataBase();
            newdb.connect();

            SeatSql seatSql = new SeatSql(newdb, seatStmt);
            rec.setSeats( seatSql.read() );

            newdb.disconnect();
        }

        objects.add(rec);
    }
}  
