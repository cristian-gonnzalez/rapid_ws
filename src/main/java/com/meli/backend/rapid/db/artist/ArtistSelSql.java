package com.meli.backend.rapid.db.artist;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.Sql;
import com.meli.backend.rapid.ws.models.ArtistRecord;

public class ArtistSelSql {
    
    Sql sqlqry;

    public ArtistSelSql(DataBase db, ArtistSelStmt stmt) {
        this.sqlqry = new Sql( db, stmt );
    }
        
    public List<ArtistRecord> read() throws SQLException {
        List<Object> objects = new ArrayList<>();
        this.sqlqry.read( new ArtistSqlRecReader(), objects, null );

        List<ArtistRecord> records = objects.stream()
                                        .filter(element->element instanceof ArtistRecord)
                                        .map(element->(ArtistRecord)element)
                                        .collect(Collectors.toList());
        return records;
    }
}
