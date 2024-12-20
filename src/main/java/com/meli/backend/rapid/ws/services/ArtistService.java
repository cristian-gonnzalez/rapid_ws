package com.meli.backend.rapid.ws.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.req_ctx.artist.ArtistOutput;
import com.meli.backend.rapid.req_ctx.artist.ArtistRequestContext;
import com.meli.backend.rapid.ws.models.ArtistRecord;
import com.meli.backend.rapid.ws.repositories.ArtistRepository;

@Service
public class ArtistService {

    ArtistRepository artistRepository;

    public ArtistService() {
        this.artistRepository = new ArtistRepository();
    }

    public void createArtist( ArtistRequestContext ctx ) throws SQLException {
        synchronized(CMutext.getInstance()) {
            this.artistRepository.saveArtist(ctx);
        }
    }

    public void deleteArtist(ArtistRequestContext ctx) throws SQLException {
        synchronized(CMutext.getInstance()) {
            this.artistRepository.deleteArtist(ctx);
        }
    }

    public List<ArtistOutput> getArtists(ArtistRequestContext ctx) throws SQLException {
        return recordsToOutputs(this.artistRepository.getArtists(ctx));
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
