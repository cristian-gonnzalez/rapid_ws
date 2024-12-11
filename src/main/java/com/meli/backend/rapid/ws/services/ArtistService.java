package com.meli.backend.rapid.ws.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.backend.rapid.common.CMutext;
import com.meli.backend.rapid.req_ctx.ArtistRequestContext;
import com.meli.backend.rapid.req_ctx.req_ctx_io.ArtistOutput;
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
        return this.artistRepository.getArtists(ctx);
    }
    
}
