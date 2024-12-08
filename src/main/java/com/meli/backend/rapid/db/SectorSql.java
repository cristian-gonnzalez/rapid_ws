package com.meli.backend.rapid.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.meli.backend.rapid.ws.models.SectorRecord;

public class SectorSql {

    Sql sqlqry;  
    
    public SectorSql(DataBase db, SectorStmt stmt) {
        this.sqlqry = new Sql( db, stmt );
    }

    
    public List<SectorRecord> read() throws SQLException {
        List<Object> objects = new ArrayList<>();
        this.sqlqry.read( new SectorSqlRecReader(), objects, null );

        List<SectorRecord> records = objects.stream()
                                        .filter(element->element instanceof SectorRecord)
                                        .map(element->(SectorRecord)element)
                                        .collect(Collectors.toList());

        return records;
    }
}
