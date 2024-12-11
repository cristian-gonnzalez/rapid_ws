package com.meli.backend.rapid.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DelSqlStmt {

    protected String sql;
    protected List<String> wheresql;

    public DelSqlStmt() {
        this.sql =  null;
        this.wheresql = new ArrayList<>();
    }

    public void setSqlBaseQuery( String sql ) {
        this.sql = sql;
    }
 
    public String build() throws SQLException {
        
        if( this.sql == null )
            throw new SQLException("No sql query");

        if( wheresql.size() == 0)
            throw new SQLException("No where implementation");

        String sql = this.sql;
        if( wheresql.size() > 0 ) {
            sql += " where ";
            for(int i =0; i< wheresql.size(); i++ ) {
                sql += wheresql.get(i);
                if( i != wheresql.size() -1) {
                    sql += " and "; 
                }
            }
        }
        return sql;
    }
}
