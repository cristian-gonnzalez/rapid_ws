package com.meli.backend.rapid.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Sql {

    private SqlStmt stmt;
    
    private DataBase db;
    
    public Sql(DataBase db, SqlStmt stmt) {
        this.stmt = stmt;
        this.db = db;
    }

    public void read(RecordReader reader, List<Object> records, Object args) throws SQLException {
        String sql = stmt.build();

        db.prepareStmt();
        
        System.out.println(sql);

        try {
            ResultSet rs = db.execute(sql);
            while (rs.next()) {
                reader.read(rs, records, args);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeStmt();
    }
}
