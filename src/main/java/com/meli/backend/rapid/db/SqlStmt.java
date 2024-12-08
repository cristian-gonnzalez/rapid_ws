package com.meli.backend.rapid.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SqlStmt {

    protected String sql;
    protected List<String> wheresql;
    protected String limit;
    protected String orderby;

    public SqlStmt() {
        this.sql =  null;
        this.wheresql = new ArrayList<>();
        this.limit = "";
        this.orderby = "";
    }

    public void setSqlBaseQuery( String sql ) {
        this.sql = sql;
    }
 
    public void setLimit( int limit, int offset ) {
        this.limit = " limit " + limit + " offset " +  offset +  " ";
    }

    public String build() throws SQLException {
        
        if( this.sql == null )
            throw new SQLException("No sql query");

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
        sql += orderby;
        sql += limit;
        return sql;

    }
}
