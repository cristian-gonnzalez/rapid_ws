package com.meli.backend.rapid.db.place;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.meli.backend.rapid.db.RecordReader;
import com.meli.backend.rapid.ws.models.PlaceRecord;

public class PlaceSqlRecReader implements RecordReader {
    
    public void read(ResultSet rs, List<Object> objects, Object args) throws SQLException {                                    
        Integer id = rs.getInt("placeId");
        String name = rs.getString("name");

        PlaceRecord rec = new PlaceRecord();
        rec.setPlaceId(id);
        rec.setName(name);

        objects.add(rec);
    }

}
