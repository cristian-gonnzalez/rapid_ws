/** 
 * This file containts the controller to process the GET request with /concert and /concert/range
 */
package com.meli.backend.rapid.ws.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import com.meli.backend.rapid.ws.services.ConcertService;
import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.concert.ConcertInput;
import com.meli.backend.rapid.req_ctx.concert.ConcertOutput;
import com.meli.backend.rapid.req_ctx.concert.ConcertRangeInput;
import com.meli.backend.rapid.req_ctx.concert.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.concert.ConcertRgRequestContext;
import com.meli.backend.rapid.req_ctx.concert.SectorInput;
import com.meli.backend.rapid.req_ctx.reserve.RequestOutput;

@RestController
@RequestMapping("/concert")
public class ConcertController {
    
    private ConcertService concertService;
 
    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @PostMapping("/sector")
    public ResponseEntity<RequestOutput> createSector( 
                @RequestBody ConcertInput input) {
        ConcertRequestContext ctx = new ConcertRequestContext( input );
        
        if( ctx.input.getArtist() == null ||
            ctx.input.getPlace() == null ||
            ctx.input.getConcertDate() == null ||
            ctx.input.getSector() == null ) {
            ctx.setError(eRCode.missingField, "artist, place, concertDate and sector are mandatories");
        }
        else {

            SectorInput sector = ctx.input.getSector();
            if( sector.getName() == null || 
                sector.getRoomSpace() == null || 
                sector.getOccupiedSpace() == null || 
                sector.getPrice() == null || 
                sector.getHasSeat() == null ) {
                    ctx.setError(eRCode.missingField, "name, roomSpace, occupiedSpace, price and hasSeat are mandatories in sector");
            }
            else {

                try {
                    concertService.createSector(ctx);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    if(!ctx.isOnError())
                        ctx.setError(eRCode.internalError, e.getMessage());
                }
            }
        }

        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

    @DeleteMapping("/sector")
    public ResponseEntity<RequestOutput> deleteSector( 
                @RequestBody ConcertInput input) {
        ConcertRequestContext ctx = new ConcertRequestContext( input );
        
        if( ctx.input.getArtist() == null ||
            ctx.input.getPlace() == null ||
            ctx.input.getConcertDate() == null ||
            ctx.input.getSector() == null ) {
            ctx.setError(eRCode.missingField, "artist, place, concertDate and sector are mandatories");
        }
        else {

            SectorInput sector = ctx.input.getSector();
            if( sector.getName() == null  ) {
                ctx.setError(eRCode.missingField, "name is mandatory in sector");
            }
            else {

                try {
                    concertService.deleteSector(ctx);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    if(!ctx.isOnError())
                        ctx.setError(eRCode.internalError, e.getMessage());
                }
            }
        }

        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

    @PostMapping("")
    public ResponseEntity<RequestOutput> createConcert( 
                @RequestBody ConcertInput input) {
        ConcertRequestContext ctx = new ConcertRequestContext( input );
        
        if( ctx.input.getArtist() == null ||
            ctx.input.getPlace() == null ||
            ctx.input.getConcertDate() == null ||
            ctx.input.getTime() == null ) {
            ctx.setError(eRCode.missingField, "artist, place, concertData and time are mandatories");
        }
        else {
            try {
                concertService.createConcert(ctx);
            } catch (HttpMessageNotReadableException e) {
                System.err.println(e.getMessage());
                if(!ctx.isOnError())
                    ctx.setError(eRCode.invalidFieldContent, e.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                if(!ctx.isOnError())
                    ctx.setError(eRCode.internalError, e.getMessage());
            }
        }

        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

    @DeleteMapping("")
    public ResponseEntity<RequestOutput> deleteConcert(
                @RequestBody ConcertInput input) {
            ConcertRequestContext ctx = new ConcertRequestContext( input );
        
        if( ctx.input.getArtist() == null ||
            ctx.input.getPlace() == null ||
            ctx.input.getConcertDate() == null ) {
            ctx.setError(eRCode.missingField, "artist, place and concertData are mandatories");
        }
        else {
            try {
                concertService.deleteConcert( ctx );
            } catch (Exception e) {
                System.err.println(e.getMessage());
                if(!ctx.isOnError())
                    ctx.setError(eRCode.internalError, e.getMessage());
            }
        }
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }


    @GetMapping("")
    public ResponseEntity<RequestOutput> getAllConcerts(
                @RequestParam(name = "rec_num", defaultValue = "0") int rec_num, 
                @RequestParam(name = "offset", defaultValue = "30") int offset, 
                @RequestBody (required=false) ConcertInput input) {
        ConcertRequestContext ctx = new ConcertRequestContext( input );
        ctx.reqParam.setRecNum(rec_num);
        ctx.reqParam.setOffset(offset);

        try {
            List<ConcertOutput> concerts = concertService.getAllConcerts(ctx);
            ctx.output.setData(concerts);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(!ctx.isOnError())
                ctx.setError(eRCode.internalError, e.getMessage());
        }
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

    
    @GetMapping("/range")
    public ResponseEntity<RequestOutput> getConcertsByRange(
                @RequestParam(name = "rec_num", defaultValue = "0") int rec_num, 
                @RequestParam(name = "offset", defaultValue = "30") int offset, 
                @RequestBody (required=false) ConcertRangeInput input) {
        // prepares the context 
        ConcertRgRequestContext ctx = new ConcertRgRequestContext( input );
        ctx.reqParam.setRecNum(rec_num);
        ctx.reqParam.setOffset(offset);

        List<ConcertOutput> concerts = concertService.getConcertsByRange(ctx);
        ctx.output.setData(concerts);
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }
}
