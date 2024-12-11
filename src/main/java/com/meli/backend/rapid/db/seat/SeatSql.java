package com.meli.backend.rapid.db.seat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.Sql;
import com.meli.backend.rapid.db.sector.SectorSqlRecReader;

public class SeatSql {

    Sql sqlqry;    
    
    public SeatSql(DataBase db, SeatStmt stmt) {
        this.sqlqry = new Sql( db, stmt );
    }
    
    public List<Integer> read() throws SQLException {
        List<Object> objects = new ArrayList<>();
        this.sqlqry.read( new SeatSqlRecReader(), objects, null );

        List<Integer> records = objects.stream()
                                        .filter(element->element instanceof Integer)
                                        .map(element->(Integer)element)
                                        .collect(Collectors.toList());

        return records;
    }

}
