/** 
 * This file containts the controller to process the GET request with /concert and /concert/range
 */
package com.meli.backend.rapid.ws.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meli.backend.rapid.req_ctx.req_ctx_io.*;
import com.meli.backend.rapid.ws.services.ConcertService;
import com.meli.backend.rapid.req_ctx.ConcertRequestContext;
import com.meli.backend.rapid.req_ctx.ConcertRgRequestContext;

@RestController
@RequestMapping("/concert")
public class ConcertController {
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error")
    public class controllerException extends RuntimeException {
        public controllerException(HttpStatus status) {
            // TODO: handle http codes
        }
    }

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

        ResponseEntity<RequestOutput> re =  null;
        try {
            re = new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
        } catch (Exception e) {
        }
        return re;
    }

    
    @GetMapping("/range")
    public ResponseEntity<RequestOutput> getConcertsByRange(@RequestParam(name = "rec_num", defaultValue = "0") int rec_num, @RequestParam(name = "offset", defaultValue = "30") int offset, @RequestBody (required=false) ConcertRangeInput input) {
        ConcertRgRequestContext ctx = new ConcertRgRequestContext( input );
        ctx.reqParam.setRecNum(rec_num);
        ctx.reqParam.setOffset(offset);

        List<ConcertOutput> concerts = concertService.getConcertsByRange(ctx);
        ctx.output.setData(concerts);
        
        ResponseEntity<RequestOutput> re =  null;
        try {
            re = new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
        } catch (Exception e) {
        }
        return re;
    }
}
