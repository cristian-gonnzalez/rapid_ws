package com.meli.backend.rapid.ws.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.artist.ArtistInput;
import com.meli.backend.rapid.req_ctx.artist.ArtistOutput;
import com.meli.backend.rapid.req_ctx.artist.ArtistRequestContext;
import com.meli.backend.rapid.req_ctx.reserve.RequestOutput;
import com.meli.backend.rapid.ws.services.ArtistService;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    ArtistService artistService;
    public ArtistController() {
        this.artistService = new ArtistService();
    }


    @PostMapping("")
    public ResponseEntity<RequestOutput> createArtist(
                @RequestBody (required=false) ArtistInput input) {
        
        ArtistRequestContext ctx = new ArtistRequestContext( input );
        if( input.getName() == null ) {
            ctx.setError(eRCode.missingField, "name is mandatory");
        }
       
        try {
            artistService.createArtist( ctx );
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(!ctx.isOnError())
                ctx.setError(eRCode.internalError, e.getMessage());
        }
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

   
    @DeleteMapping("")
    public ResponseEntity<RequestOutput> deleteArtist(
                @RequestBody (required=false) ArtistInput input) {
        ArtistRequestContext ctx = new ArtistRequestContext( input );
       
        if( input.getName() == null ) {
            ctx.setError(eRCode.missingField, "name is mandatory");
        }
        else {
            try {
                artistService.deleteArtist( ctx );
            } catch (Exception e) {
                System.err.println(e.getMessage());
                if(!ctx.isOnError())
                    ctx.setError(eRCode.internalError, e.getMessage());
            }
        }
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

    
   
    @GetMapping("")
    public ResponseEntity<RequestOutput> getArtist(
                @RequestBody (required=false) ArtistInput input) {
        
        ArtistRequestContext ctx = new ArtistRequestContext( input );
       
        try {
            List<ArtistOutput> records = artistService.getArtists( ctx );
            ctx.output.setData(records);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(!ctx.isOnError())
                ctx.setError(eRCode.internalError, e.getMessage());
        }
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }
}
