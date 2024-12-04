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
    private int getLastID() {
        DataBase db = new DataBase();
        int id = -1;
     
        db.connect();
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
        db.disconnect();
        
        return id;
    }

    /* Gets the last inserted datetiem.
     * 
     * @return The last datetiem if found, -1 otherwise
     */
    private Long getLastDatetime() {
        DataBase db = new DataBase();
        Long id = null;
     
        db.connect();
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
        db.disconnect();
        
        return id;
    }

    private boolean insertReservedSeats(DataBase db, int artistId, int placeId, Date concertDate, int sectorId, List<Integer> seats) throws SQLException {
            String values ="";
            for(int i=0; i<seats.size(); i++) {
                values += "( "+ artistId + ", "+ 
                        placeId + ", " +  
                    "'" + concertDate + "', " +  
                        sectorId + ", " + 
                            seats.get(i) + ")";
                
                if( i != seats.size() -1 )
                    values += ", ";
            }

            String sql = "insert into Seat (`artistId`, `placeId`, `concertDate`, `sectorId`, `seatNumber`) values " + values + ";"; 
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
        
            reserve.setReserveId( getLastID() );
            reserve.setDatetime( getLastDatetime() );
            int occupiedSpace = cs.getOccupiedSpace() + reserve.getTotalInfo().getQuantity();
            
            r =updateConcertSector( db, artistId, placeId, concertDate, sectorId, occupiedSpace );
            if( !r ) {
                db.disconnect();
                return r;
            }
            
            if( seats.size() > 0) {
                insertReservedSeats(db, artistId, placeId, concertDate, sectorId, seats);
            }
            
            db.commit();
        } catch (SQLException e) {
            db.rollback();
        }
    
        db.disconnect();
        return r;
    }
}
