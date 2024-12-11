package com.meli.backend.rapid.db.seat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.meli.backend.rapid.db.RecordReader;


public class SeatSqlRecReader implements RecordReader {
    
    public void read(ResultSet rs, List<Object> objects, Object args) throws SQLException {                                    
        int seatnum = rs.getInt("SeatNumber");
        objects.add(seatnum);
    }
}
