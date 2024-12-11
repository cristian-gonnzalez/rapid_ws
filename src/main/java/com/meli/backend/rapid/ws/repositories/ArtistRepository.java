package com.meli.backend.rapid.ws.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.db.DataBase;
import com.meli.backend.rapid.db.artist.ArtistDelStmt;
import com.meli.backend.rapid.db.artist.ArtistInsertStmt;
import com.meli.backend.rapid.db.artist.ArtistSelSql;
import com.meli.backend.rapid.db.artist.ArtistSelStmt;
import com.meli.backend.rapid.req_ctx.artist.ArtistOutput;
import com.meli.backend.rapid.req_ctx.artist.ArtistRequestContext;
import com.meli.backend.rapid.ws.models.ArtistRecord;

public class ArtistRepository {

    public void saveArtist(ArtistRequestContext ctx) throws SQLException {

        ArtistRecord record = new ArtistRecord();
        record.setName( ctx.input.getName() );

        ArtistInsertStmt stmt = new ArtistInsertStmt();
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
            ctx.setError(eRCode.internalError, "Failed to insert Artist");
        }
    }

    public void deleteArtist(ArtistRequestContext ctx) throws SQLException {
        
        ArtistDelStmt stmt = new ArtistDelStmt();
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
            ctx.setError(eRCode.internalError, "Failed to delete Artist");
        }
    }

    public List<ArtistOutput> getArtists(ArtistRequestContext ctx) throws SQLException {
        ArtistSelStmt stmt = new ArtistSelStmt();

        if( ctx.input.getName() != null ) {
            stmt.setArtist(ctx.input.getName() );
        }

        DataBase db = new DataBase();
        db.connect();

        ArtistSelSql sql = new ArtistSelSql(db, stmt);
        List<ArtistRecord> records = sql.read();
        
        db.disconnect();

        return recordsToOutputs( records );
    }
        
    private List<ArtistOutput> recordsToOutputs(List<ArtistRecord> records) {
        List<ArtistOutput> outputs =  new ArrayList<>();
        for( int i=0;i< records.size(); i++) {
            outputs.add( recordToOutput(records.get(i) ));
        }
        return outputs;
    }

    private ArtistOutput recordToOutput(ArtistRecord artistRecord) {
        ArtistOutput output = new ArtistOutput();
        output.setName(artistRecord.getName());
        return output;
    }
}
