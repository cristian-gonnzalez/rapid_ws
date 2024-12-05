package com.meli.backend.rapid.ws.repositories;

import java.sql.*;
import java.util.List;

import com.meli.backend.rapid.common.DataBase;
import com.meli.backend.rapid.ws.models.ConcertSector;
import com.meli.backend.rapid.ws.models.TicketReserve;

public class ReserveRepository {

    public ReserveRepository() {
    }

    /** Executes and DELETE/UPDATE/INSERT
     * 
     * @param db Database.
     * @param sql Sql statement
     * @return true if if was excecuted successfully, false otherwise
     */
    private boolean execsql( DataBase db, String sql ) throws SQLException {
        System.out.println(sql);
        db.prepareStmt();
        boolean r = db.executeUpdte(sql);
        db.closeStmt();
        return r;
    }

    /** Gets the las inserted ID
     * 
     * @return The last id if found, -1 otherwise
     */
    private int getLastID(DataBase db) {
        int id = -1;
     
        db.prepareStmt();
        String querystr = "SELECT LAST_INSERT_ID(reserveId) from reserve;";
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
        
        return id;
    }

    /* Gets the last inserted datetiem.
     * 
     * @return The last datetiem if found, -1 otherwise
     */
    private Long getLastDatetime(DataBase db) {
        Long id = null;
     
        db.prepareStmt();
        String querystr = "SELECT LAST_INSERT_ID(datetime) from reserve;";
        System.out.println(querystr);

        try {
            ResultSet rs = db.execute(querystr);
            while (rs.next()) {
                id = rs.getLong(1);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeStmt();
        
        return id;
    }

    private boolean insertReservedSeats(DataBase db, int reserveId,  int artistId, int placeId, Date concertDate, int sectorId, List<Integer> seats) throws SQLException {
            String values ="";
            for(int i=0; i<seats.size(); i++) {
                values += "( "+ reserveId + ", " + 
                                artistId + ", " + 
                                placeId + ", " +  
                          "'" + concertDate + "', " +  
                                sectorId + ", " + 
                            seats.get(i) + ")";
                
                if( i != seats.size() -1 )
                    values += ", ";
            }

            String sql = "insert into Seat (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`, `seatNumber`) values " + values + ";"; 
            return execsql(db, sql);    
    }

    private boolean updateConcertSector(DataBase db,int artistId, int placeId, Date concertDate, int sectorId, int occupiedSpace) throws SQLException {
        String sql = "update ConcertSector " + 
                         "set occupiedSpace = " + occupiedSpace + " " +
                         " where artistId = " + artistId + " and " + 
                               " placeId = " + placeId  + " and " + 
                               " concertDate = '" + concertDate  + "' and " + 
                               " sectorId = " + sectorId  + ";";
        return execsql(db, sql);
     }

    /** Saves the reserve and udpate tables
     * 
     * @param reserve Reserve info.
     * @param artistId The artist id.
     * @param placeId The place id.
     * @param concertDate The concert date.
     * @param sectorId The sector id.
     * @param seats The list of seats to reserve.
     * @param cs The sector of concert.
     * @return true if success, false otherwise.
     */
    public boolean saveReserve(TicketReserve reserve, int artistId, int placeId, Date concertDate, int sectorId, List<Integer> seats, ConcertSector cs) throws SQLException {

        String sql = "insert into Reserve (`artistId`, `placeId`, `concertDate`, `sectorId`, `qty`, `name`, `surname`, `dni`, `total`) values " +
        "( "+ artistId + ", "+ 
                placeId + ", " +  
        "'" + concertDate + "', " +  
                sectorId + ", " + 
                reserve.getTotalInfo().getQuantity()  + ", " + 
        "'" + reserve.getUser().getName()  + "', " + 
        "'" + reserve.getUser().getSurname()  + "', " + 
                reserve.getUser().getDNI()  + ", " + 
                reserve.getTotalInfo().getTotal() + ");"; 
        
        boolean r = false;
        DataBase db = new DataBase();
        db.connect();
        
        try {
            
            db.begin();

            r = execsql(db, sql);
            if( !r ) {
                db.rollback();
                db.disconnect();
                return r;
            }
        
            reserve.setReserveId( getLastID(db) );
            reserve.setDatetime( getLastDatetime(db) );
            int occupiedSpace = cs.getOccupiedSpace() + reserve.getTotalInfo().getQuantity();
            
            r =updateConcertSector( db, artistId, placeId, concertDate, sectorId, occupiedSpace );
            if( !r ) {
                db.rollback();
                db.disconnect();
                return r;
            }
            
            if( seats.size() > 0) {
                insertReservedSeats(db, reserve.getReserveId(), artistId, placeId, concertDate, sectorId, seats);
            }
            
            db.commit();
        } catch (SQLException e) {
            db.rollback();
        }
    
        db.disconnect();
        return r;
    }

    public boolean deleteReserve(int reserveId, int artistId, int placeId, Date concertDate, int sectorId, 
                                 List<Integer> seats, ConcertSector cs ) throws SQLException {

        int qty = getReserveQuantity(reserveId, artistId, placeId, concertDate, sectorId);
        
        
        boolean r = false;
        DataBase db = new DataBase();
        db.connect();
        
        try {    
            db.begin();

            String sql;    
            if( seats.size() > 0) {
                sql = "delete from Seat " +
                " where reserveId = " + reserveId + " and " + 
                " artistId = " + artistId + " and " + 
                " placeId = " + placeId  + " and " + 
                " concertDate = '" + concertDate  + "' and " + 
                " sectorId = " + sectorId  + ";";
                r = execsql(db, sql);
            }

            sql = "delete from Reserve " +
                    " where reserveId = " + reserveId + " and " + 
                    " artistId = " + artistId + " and " + 
                    " placeId = " + placeId  + " and " + 
                    " concertDate = '" + concertDate  + "' and " + 
                    " sectorId = " + sectorId  + ";";
            r = execsql(db, sql);
            if( !r ) {
                db.rollback();
                db.disconnect();
                return r;
            }
        
            int occupiedSpace = cs.getOccupiedSpace() - qty;
            r =updateConcertSector( db, artistId, placeId, concertDate, sectorId, occupiedSpace );
            if( !r ) {
                db.rollback();
                db.disconnect();
                return r;
            }
            
            
            db.commit();
        } catch (SQLException e) {
            db.rollback();
        }
    
        db.disconnect();
        return r;
    }



     private int getReserveQuantity( int reserveId, int artistId, int placeId, Date concertDate, int sectorId)  {
        
        int qty = 0;
      
        DataBase db = new DataBase();

        db.connect();
        db.prepareStmt();
        String querystr = "select qty from Reserve " +
                          " where reserveId = " + reserveId + " and " + 
                          " artistId = " + artistId + " and " + 
                          " placeId = " + placeId  + " and " + 
                          " concertDate = '" + concertDate  + "' and " + 
                          " sectorId = " + sectorId  + ";";
        System.out.println(querystr);

        try {
            ResultSet rs = db.execute(querystr);
            
            while (rs.next()) {
                qty = rs.getInt("qty");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeStmt();
        db.disconnect();

        return qty;
    }
}
