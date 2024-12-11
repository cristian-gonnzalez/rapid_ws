package com.meli.backend.rapid.db.reserve;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.Sql;
import com.meli.backend.rapid.ws.models.ReserveRecord;

public class ReserveSql {

    Sql sqlqry;

    public ReserveSql(DataBase db, ReserveStmt stmt) {
        this.sqlqry = new Sql( db, stmt );
    }
        
    public List<ReserveRecord> read() throws SQLException {
        List<Object> objects = new ArrayList<>();
        this.sqlqry.read( new ReserveRecReader(), objects, null );

        List<ReserveRecord> records = objects.stream()
                                        .filter(element->element instanceof ReserveRecord)
                                        .map(element->(ReserveRecord)element)
                                        .collect(Collectors.toList());
        return records;
    }

}
