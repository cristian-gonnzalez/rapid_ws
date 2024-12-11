package com.meli.backend.rapid.db.reserve;

import java.sql.Timestamp;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.RecordReader;
import com.meli.backend.rapid.db.concert.ConcertSql;
import com.meli.backend.rapid.db.concert.ConcertStmt;
import com.meli.backend.rapid.db.seat.SeatSql;
import com.meli.backend.rapid.db.seat.SeatStmt;
import com.meli.backend.rapid.ws.models.ConcertKey;
import com.meli.backend.rapid.ws.models.ConcertRecord;
import com.meli.backend.rapid.ws.models.SectorRecord;
import com.meli.backend.rapid.ws.models.ReserveKey;
import com.meli.backend.rapid.ws.models.ReserveRecord;
import com.meli.backend.rapid.ws.models.SectorKey;
import com.meli.backend.rapid.ws.models.User;

public class ReserveRecReader implements RecordReader {

    private List<Integer> readSeats(ReserveKey reserveKey) throws SQLException {
        
        SeatStmt seatStmt = new SeatStmt();
        seatStmt.setReserveId(reserveKey.getReserveId());
        seatStmt.setArtistId(reserveKey.getSectorKey().getConcerKey().getArtistId());
        seatStmt.setDate(reserveKey.getSectorKey().getConcerKey().getConcertDate());
        seatStmt.setPlaceId(reserveKey.getSectorKey().getConcerKey().getPlaceId());
        seatStmt.setSectorId(reserveKey.getSectorKey().getSectorId());

        DataBase newdb = new DataBase();
        newdb.connect();

        SeatSql seatSql = new SeatSql(newdb, seatStmt);
        List<Integer> seats = seatSql.read();

        newdb.disconnect();

        return seats;
    }

    private ConcertRecord readConcert(SectorKey sectorKey ) throws SQLException {

        ConcertStmt stmt = new ConcertStmt();
        ConcertKey concertKey =sectorKey.getConcerKey();

        stmt.setArtistId(concertKey.getArtistId()); 
        stmt.setPlaceId(concertKey.getPlaceId()); 
        stmt.setDate(concertKey.getConcertDate()); 
        
        DataBase db = new DataBase();
        db.connect();
        ConcertSql sql = new ConcertSql(db, stmt);
        List<ConcertRecord> concerts = sql.read();
        db.disconnect();

        ConcertRecord concert = concerts.get(0);

        List<SectorRecord> sectors = concert.getSectors();
        int i=0;
        for(; i<sectors.size(); i++) {
            if( sectors.get(i).getSetorKey().getSectorId() == sectorKey.getSectorId()) {
                break;
            }
        }

        SectorRecord cs = sectors.get(i);

        List<SectorRecord> aux = new ArrayList<>();
        aux.add(cs);
        concert.setSectors(aux);

        return concert;
    }


    public void read(ResultSet rs, List<Object> objects, Object args) throws SQLException {
                        
        Timestamp date;

        date = rs.getTimestamp("datetime");

        Long datetime = date.getTime();

        int reserveId = rs.getInt("reserveId");
        int artistId = rs.getInt("artistId");
        int placeId = rs.getInt("placeId");
        Date concerDate = rs.getDate("concertDate");
        int sectorId = rs.getInt("sectorId");
        int quantity = rs.getInt("qty");
        Double total_amount = rs.getDouble("total");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        Long dni = rs.getLong("dni");
        


        ConcertKey concertKey = new ConcertKey();
        concertKey.setArtistId(artistId);
        concertKey.setPlaceId(placeId);
        concertKey.setConcertDate(concerDate);
        SectorKey sectorKey = new SectorKey();
        sectorKey.setConcerKey(concertKey);
        sectorKey.setSectorId(sectorId);
        ReserveKey reserveKey = new ReserveKey();
        reserveKey.setReserveId(reserveId);
        reserveKey.setSectorKey(sectorKey);

        User user = new User();
        user.setDNI(dni);
        user.setName(name);
        user.setSurname(surname);


        ReserveRecord rec = new ReserveRecord();
        rec.setReserveKey(reserveKey);
        rec.setDatetime((Long)datetime);
        rec.setTotalAmoun(total_amount);
        rec.setQuantity(quantity);
        rec.setUser(user);

        ConcertRecord concert = readConcert(sectorKey);
        
        if( concert.getSectors().get(0).getHasSeat() ) {
            List<Integer> seats = readSeats(reserveKey);
            concert.getSectors().get(0).setSeats(seats);
        }
        rec.setConcert(concert);

        objects.add(rec);
    }
}  