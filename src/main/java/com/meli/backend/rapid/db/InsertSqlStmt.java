package com.meli.backend.rapid.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertSqlStmt extends SqlStmt {

    
    protected List<String> values;

    public InsertSqlStmt() {
        super();
        values = new ArrayList<>();
    }

    public String build() throws SQLException {
        for(int i=0; i<values.size(); i++) {
            sql += values.get(i);
            if( i != values.size() -1)
                sql += ", ";
        }
        return super.build();
    }

}
