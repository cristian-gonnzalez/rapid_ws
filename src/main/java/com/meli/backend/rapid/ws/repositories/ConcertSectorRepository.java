package com.meli.backend.rapid.ws.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.meli.backend.rapid.common.DataBase;
import com.meli.backend.rapid.req_ctx.req_ctx_io.*;
import com.meli.backend.rapid.ws.models.ConcertSector;

public class ConcertSectorRepository {
   
   
        public ConcertSectorRepository() {
        }

        /** Gets the seats of the concert sector for specific concert date and place
         * 
         * @param artistId The artist id.
         * @param placeId The place id.
         * @param concertDate The concert date.
         * @param sectorId The sector id.
         * @return The list of seats;
         */
        public List<Integer> getSeats(int artistId, int placeId, Date concertDate, int sectorId ) {

        String concertDatestr = "s.concertDate";
        if( concertDate != null )
            concertDatestr = "'" + concertDate.toString() + "'"; 

        String querystr = "select seatNumber from Seat as s " + 
                          "where  s.artistId = " + artistId + " and "+ 
                          " s.placeId = " + placeId + " and " + 
                          " s.sectorId = " + sectorId + " and " + 
                          " s.concertDate = " + concertDatestr + ";";
        System.out.println(querystr);

        List<Integer> seats = new ArrayList<>();
        DataBase db = new DataBase();
        db.connect();
        db.prepareStmt();
        
        try {
            ResultSet rs = db.execute(querystr);
            
            while (rs.next()) {
                int seatnum = rs.getInt("SeatNumber");
                seats.add(seatnum);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeStmt();
        db.disconnect();

        return seats;
    }

    /** Gets the sector of the concert.
     * 
     * @param rg Range input parameters.
     * @param artistId The artist id.
     * @param placeId The place id.
     * @param concertDate The concert date.
     * @param sectorname The sector name.
     * @return The list of sectors of the concert.
     */
    public List<ConcertSector> getConcertSectors(ConcertRangeInput rg, int artistId, int placeId, Date concertDate, String sectorname ) {

        String wheresql = "";
        if( rg != null )
        {   
            if( rg.getFromPrice() != null ) {
                wheresql += " and cs.price >= " +  rg.getFromPrice() + " ";
            }

            if( rg.getUntilPrice() != null ) {
                wheresql += " and cs.price =< " +  rg.getUntilPrice() + " ";
            }

            if( rg.getPriceASC() != null ) {
                wheresql += " order by cs.price asc ";
            }
            else {
                wheresql += " order by cs.price desc ";
            }
        }

        HashMap<String, ConcertSector> dic = new HashMap<>();

        DataBase db = new DataBase();
        db.connect();
        db.prepareStmt();

        String sectorqry = "' ";
        if( !sectorname.isEmpty()) {
            sectorqry = "' and cs.name = '" + sectorname + "' ";
        }
        
        try {
            String querystr = "select distinct cs.name, cs.sectorId, cs.roomSpace, cs.occupiedSpace, cs.hasSeat, cs.price " + 
            " from ConcertSector cs" + 
            " where cs.placeId = " + placeId + " and " +
                  " cs.artistId = " + artistId + " and " +
                  " cs.concertDate = '" + concertDate + sectorqry + wheresql +  " ;";

            System.out.println(querystr);

            ResultSet rs = db.execute(querystr);
            
            while (rs.next()) {
                
                String name = rs.getString(1);
                int sectorId = rs.getInt("sectorId");
                int roomSpace = rs.getInt("roomSpace");
                int occupiedSpace = rs.getInt("occupiedSpace");
                Boolean hasSeat = (rs.getInt("hasSeat") == 1);
                double price = rs.getDouble("price");
                
                ConcertSector obj = dic.get(name);
                if(obj  == null ) {
                    obj  = new ConcertSector();
                    obj.setHasSeat( hasSeat );
                    obj.setName(name);
                    obj.setRoomSpace(roomSpace);
                    obj.setOccupiedSpace(occupiedSpace);
                    obj.setPrice(price);

                    if( hasSeat ) {
                        obj .setSeats( getSeats(artistId, placeId, concertDate, sectorId) );
                    }
                }
                dic.put(name, obj);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeStmt();
        db.disconnect();

        List<ConcertSector> sectors; 
        sectors = dic.values().stream().collect(
            Collectors.toCollection(ArrayList::new));

        return sectors;
    }
}
