package com.meli.backend.rapid.ws.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.place.PlaceDelStmt;
import com.meli.backend.rapid.db.place.PlaceInsertStmt;
import com.meli.backend.rapid.db.place.PlaceSelSql;
import com.meli.backend.rapid.db.place.PlaceSelStmt;
import com.meli.backend.rapid.req_ctx.place.PlaceOutput;
import com.meli.backend.rapid.req_ctx.place.PlaceRequestContext;
import com.meli.backend.rapid.ws.models.PlaceRecord;

public class PlaceRepository {

    public void savePlace(PlaceRequestContext ctx) throws SQLException {

        PlaceRecord record = new PlaceRecord();
        record.setName( ctx.input.getName() );

        PlaceInsertStmt stmt = new PlaceInsertStmt();
        stmt.add(record);
        String sql = stmt.build();

        System.out.println(sql);

        DataBase db = new DataBase();
        db.connect();
        db.prepareStmt();
        boolean r = db.executeUpdte(sql);
        db.closeStmt();
        db.disconnect();
        
        if( !r ) {
            ctx.setError(eRCode.internalError, "Failed to insert Place");
        }

    }

    public void deletePlace(PlaceRequestContext ctx) throws SQLException {
        
        PlaceDelStmt stmt = new PlaceDelStmt();
        stmt.addName(ctx.input.getName());
        String sql = stmt.build();

        System.out.println(sql);

        DataBase db = new DataBase();
        db.connect();
        db.prepareStmt();
        boolean r = db.executeUpdte(sql);
        db.closeStmt();
        db.disconnect();
        
        if( !r ) {
            ctx.setError(eRCode.internalError, "Failed to delete Place");
        }
    }

    public List<PlaceOutput> getPlaces(PlaceRequestContext ctx) throws SQLException {
        PlaceSelStmt stmt = new PlaceSelStmt();

        if( ctx.input.getName() != null ) {
            stmt.setPlace(ctx.input.getName() );
        }

        DataBase db = new DataBase();
        db.connect();

        PlaceSelSql sql = new PlaceSelSql(db, stmt);
        List<PlaceRecord> records = sql.read();
        
        db.disconnect();

        return recordsToOutputs( records );
    }

    private List<PlaceOutput> recordsToOutputs(List<PlaceRecord> records) {
        List<PlaceOutput> outputs =  new ArrayList<>();
        for( int i=0;i< records.size(); i++) {
            outputs.add( recordToOutput(records.get(i) ));
        }
        return outputs;
    }

    private PlaceOutput recordToOutput(PlaceRecord record) {
        PlaceOutput output = new PlaceOutput();
        output.setName(record.getName());
        return output;
    }
}
