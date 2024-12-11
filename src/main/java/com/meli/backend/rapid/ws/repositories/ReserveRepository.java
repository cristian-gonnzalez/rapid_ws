package com.meli.backend.rapid.ws.repositories;

import java.sql.*;
import java.util.List;

import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.reserve.ReserveSql;
import com.meli.backend.rapid.db.reserve.ReserveStmt;
import com.meli.backend.rapid.req_ctx.concert.SectorOutput;
import com.meli.backend.rapid.req_ctx.reserve.ReserveGetRequestContext;
import com.meli.backend.rapid.req_ctx.reserve.ReserveOutput;
import com.meli.backend.rapid.req_ctx.reserve.ReserveRequestContext;
import com.meli.backend.rapid.ws.models.SectorRecord;
import com.meli.backend.rapid.ws.models.ConcertKey;
import com.meli.backend.rapid.ws.models.ReserveKey;
import com.meli.backend.rapid.ws.models.ReserveRecord;
import com.meli.backend.rapid.ws.models.SectorKey;

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

    private boolean insertReservedSeats(DataBase db, ReserveOutput reserve, SectorRecord sector_rec ) throws SQLException {
            
        List<Integer> seats = sector_rec.getSeats();
        String values ="";
            for(int i=0; i<seats.size(); i++) {
                values += "( "+ reserve.getReserveId() + ", " + 
                sector_rec.getSetorKey().getConcerKey().getArtistId() + ", " + 
                sector_rec.getSetorKey().getConcerKey().getPlaceId() + ", " +  
                          "'" + sector_rec.getSetorKey().getConcerKey().getConcertDate() + "', " +  
                          sector_rec.getSetorKey().getSectorId() + ", " + 
                            seats.get(i) + ")";
                
                if( i != seats.size() -1 )
                    values += ", ";
            }

            String sql = "insert into Seat (`reserveId`, `artistId`, `placeId`, `concertDate`, `sectorId`, `seatNumber`) values " + values + ";"; 
            return execsql(db, sql);    
    }

    private boolean updateConcertSector(DataBase db, SectorRecord cs) throws SQLException {
        SectorKey sectorKey = cs.getSetorKey();
        ConcertKey concertKey = sectorKey.getConcerKey();
        String sql = "update ConcertSector " + 
                         "set occupiedSpace = " + cs.getOccupiedSpace() + " " +
                         " where artistId = " + concertKey.getArtistId() + " and " + 
                               " placeId = " + concertKey.getPlaceId()  + " and " + 
                               " concertDate = '" + concertKey.getConcertDate()  + "' and " + 
                               " sectorId = " + sectorKey.getSectorId()  + ";";
        System.out.println(sql);
        return execsql(db, sql);
     }


    public Boolean insertReserve(DataBase db, ReserveOutput reserve, SectorRecord sector_rec ) throws SQLException {
        String sql = "insert into Reserve (`artistId`, `placeId`, `concertDate`, `sectorId`, `qty`, `name`, `surname`, `dni`, `total`) values " +
        "( "+ sector_rec.getSetorKey().getConcerKey().getArtistId() + ", "+ 
              sector_rec.getSetorKey().getConcerKey().getPlaceId() + ", " +  
        "'" + sector_rec.getSetorKey().getConcerKey().getConcertDate() + "', " +  
              sector_rec.getSetorKey().getSectorId() + ", " + 
              reserve.getTotalInfo().getQuantity()  + ", " + 
        "'" + reserve.getUserInfo().getName()  + "', " + 
        "'" + reserve.getUserInfo().getSurname()  + "', " + 
              reserve.getUserInfo().getDNI()  + ", " + 
              reserve.getTotalInfo().getTotal() + ");"; 
        return execsql(db, sql);
            
    }

    /** Save the reserve and udpate related tables
     * 
     * @param ctx Context.
     * @param reserve Reserve output.
     * @param sector_rec Sector record.
     * 
     * @return true if success, false otherwise.
     */
    public Boolean saveReserve(ReserveRequestContext ctx, ReserveOutput reserve, SectorRecord sector_rec ) throws SQLException {

        Boolean r = false;
        
        // create an db instance and connect to it
        DataBase db = new DataBase();
        db.connect();
        
        try {
            // since we are going to update more than one table, we need to begin a transaction
            db.begin();

            // insert the reserve
            r = insertReserve(db, reserve, sector_rec);
            if( !r ) {
                System.err.println("Failed to save reserve in table");
                db.rollback();
                db.disconnect();
                return r;
            }
        
            // fill the output with the reserved id and the datetime 
            reserve.setReserveId( getLastID(db) );
            reserve.setDatetime( getLastDatetime(db) );

            // update the sector
            SectorOutput cs = reserve.getConcertInfo().getSector();
            r = updateConcertSector( db, sector_rec );
            if( !r ) {
                System.err.println("Failed to update sector in table");
                db.rollback();
                db.disconnect();
                return r;
            }
            
            // if the sector has seats, insert the records in the seat table 
            if( cs.getHasSeat() ) {
                r = insertReservedSeats(db, reserve, sector_rec );
                if( !r ) {
                    System.err.println("Failed to save seats in table");
                    db.rollback();
                    db.disconnect();
                    return r;
                }
            }
            
            // commit the transaction
            db.commit();
        } catch (SQLException e) {
            db.rollback();
        }
    
        db.disconnect();
        return r;
    }


    public List<ReserveRecord> getReserves(ReserveGetRequestContext ctx) throws SQLException{

        // creates an statement
        ReserveStmt stmt = new ReserveStmt();
        if( ctx.input.getReserveId() != 0)
            stmt.setReserveId(ctx.input.getReserveId());
        if( ctx.input.getArtist() != null)
            stmt.setArtist(ctx.input.getArtist());
        if( ctx.input.getPlace() != null)
            stmt.setPlace(ctx.input.getPlace());
        if( ctx.input.getConcertDate() != null)
            stmt.setDate(ctx.input.getConcertDate());
        if( ctx.input.getSector() != null)
            stmt.setSector(ctx.input.getSector());
        if( ctx.input.getName() != null)
            stmt.setUsername(ctx.input.getName());
        if( ctx.input.getSurname() != null)
            stmt.setUsersurname(ctx.input.getSurname());
        if( ctx.input.getDNI() != 0)
            stmt.setDNI(ctx.input.getDNI());
       
        DataBase db = new DataBase();
        db.connect();
        ReserveSql sql = new ReserveSql(db, stmt);
        List<ReserveRecord> reserveRecords = sql.read();
        db.disconnect();

        return reserveRecords;
    } 


    public ReserveRecord getReserve(ReserveRequestContext ctx) throws SQLException{

        // creates an statement
        ReserveStmt stmt = new ReserveStmt();
        if( ctx.input.getReserveId() != null)
            stmt.setReserveId(ctx.input.getReserveId());
        stmt.setArtist(ctx.input.getArtist());
        stmt.setPlace(ctx.input.getPlace());
        stmt.setDate(ctx.input.getConcertDate());
        stmt.setSector(ctx.input.getSector());

        DataBase db = new DataBase();
        db.connect();
        ReserveSql sql = new ReserveSql(db, stmt);
        List<ReserveRecord> reserveRecords = sql.read();
        db.disconnect();

        if( reserveRecords.size() > 0 )
            return reserveRecords.get(0);

        return null;
    } 

    
    public boolean deleteReserve(ReserveRecord rec, SectorRecord sector) throws SQLException {
        
        ReserveKey reserveKey = rec.getReserveKey();
        SectorKey sectorKey = reserveKey.getSectorKey();
        ConcertKey concertKey = sectorKey.getConcerKey();

        String wsql = " where reserveId = " + reserveKey.getReserveId() + " and " + 
        " artistId = " + concertKey.getArtistId() + " and " + 
        " placeId = " + concertKey.getPlaceId()  + " and " + 
        " concertDate = '" + concertKey.getConcertDate()  + "' and " + 
        " sectorId = " + sectorKey.getSectorId()  + ";";

        boolean r = false;
        DataBase db = new DataBase();
        db.connect();
        
        try {    
            db.begin();

            String sql;    
            if( sector.getHasSeat()) {
                
                sql = "delete from Seat " + wsql;
                System.out.println( sql );
                r = execsql(db, sql);
                if(!r) {
                    System.err.println("Failed to delete seats");    
                    db.rollback();
                    db.disconnect();
                    return r;
                }
            }

            r =updateConcertSector( db, sector );
            if( !r ) {
                System.err.println("Failed to update sector");    
                db.rollback();
                db.disconnect();
                return r;
            }

            sql = "delete from Reserve " + wsql;
            System.err.println(sql);
            r = execsql(db, sql);
            if( !r ) {
                System.err.println("Failed to delete reserve");    
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
}
