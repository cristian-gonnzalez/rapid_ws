/** 
 * This file containts the controller to process the GET request with /concert and /concert/range
 */
package com.meli.backend.rapid.ws.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meli.backend.rapid.req_ctx.req_ctx_io.*;
import com.meli.backend.rapid.ws.services.ConcertService;
import com.meli.backend.rapid.req_ctx.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.ConcertRgRequestContext;

@RestController
@RequestMapping("/concert")
public class ConcertController {
    
    private ConcertService concertService;
    
    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping("")
    public ResponseEntity<RequestOutput> getAllConcerts(@RequestParam(name = "rec_num", defaultValue = "0") int rec_num, @RequestParam(name = "offset", defaultValue = "30") int offset, @RequestBody (required=false) ConcertInput input) {
        ConcertRequestContext ctx = new ConcertRequestContext( input );
        ctx.reqParam.setRecNum(rec_num);
        ctx.reqParam.setOffset(offset);

        List<ConcertOutput> concerts = concertService.getAllConcerts(ctx);
        ctx.output.setData(concerts);

        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

    
    @GetMapping("/range")
    public ResponseEntity<RequestOutput> getConcertsByRange(@RequestParam(name = "rec_num", defaultValue = "0") int rec_num, @RequestParam(name = "offset", defaultValue = "30") int offset, @RequestBody (required=false) ConcertRangeInput input) {
        // prepares the context 
        ConcertRgRequestContext ctx = new ConcertRgRequestContext( input );
        ctx.reqParam.setRecNum(rec_num);
        ctx.reqParam.setOffset(offset);

        List<ConcertOutput> concerts = concertService.getConcertsByRange(ctx);
        ctx.output.setData(concerts);
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }
}
