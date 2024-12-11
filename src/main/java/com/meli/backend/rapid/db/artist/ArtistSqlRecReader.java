package com.meli.backend.rapid.db.artist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.meli.backend.rapid.db.RecordReader;
import com.meli.backend.rapid.ws.models.ArtistRecord;

public class ArtistSqlRecReader implements RecordReader {
    
    public void read(ResultSet rs, List<Object> objects, Object args) throws SQLException {                                    
        Integer artist_id = rs.getInt("artistId");
        String name = rs.getString("name");

        ArtistRecord rec = new ArtistRecord();
        rec.setArtistId(artist_id);
        rec.setName(name);

        objects.add(rec);
    }

}
