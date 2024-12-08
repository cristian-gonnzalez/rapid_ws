package com.meli.backend.rapid.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface RecordReader {

    public void read(ResultSet rs, List<Object> objects, Object args) throws SQLException;
}
