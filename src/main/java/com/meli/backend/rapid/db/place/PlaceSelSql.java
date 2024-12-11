package com.meli.backend.rapid.db.place;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.Sql;
import com.meli.backend.rapid.ws.models.PlaceRecord;

public class PlaceSelSql {
      Sql sqlqry;

    public PlaceSelSql(DataBase db, PlaceSelStmt stmt) {
        this.sqlqry = new Sql( db, stmt );
    }
        
    public List<PlaceRecord> read() throws SQLException {
        List<Object> objects = new ArrayList<>();
        this.sqlqry.read( new PlaceSqlRecReader(), objects, null );

        List<PlaceRecord> records = objects.stream()
                                        .filter(element->element instanceof PlaceRecord)
                                        .map(element->(PlaceRecord)element)
                                        .collect(Collectors.toList());
        return records;
    }

}
