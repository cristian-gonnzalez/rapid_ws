package com.meli.backend.rapid.db.concert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.Sql;
import com.meli.backend.rapid.req_ctx.concert.ConcertRgRequestContext;
import com.meli.backend.rapid.ws.models.ConcertRecord;

public class ConcertSql {

    Sql sqlqry;
    ConcertRgRequestContext ctx;  

    public ConcertSql(DataBase db, ConcertStmt stmt) {
        this.sqlqry = new Sql( db, stmt );
        this.ctx = null;
    }

    public void setArgs( ConcertRgRequestContext ctx ) {
        this.ctx = ctx;
    }
        
    public List<ConcertRecord> read() throws SQLException {
        List<Object> objects = new ArrayList<>();
        this.sqlqry.read( new ConcertSqlRecReader(), objects, ctx );

        List<ConcertRecord> records = objects.stream()
                                        .filter(element->element instanceof ConcertRecord)
                                        .map(element->(ConcertRecord)element)
                                        .collect(Collectors.toList());
        return records;
    }
}
