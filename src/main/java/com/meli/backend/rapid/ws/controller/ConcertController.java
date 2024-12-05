/** 
 * This file containts the controller to process the GET request with /concert and /concert/range
 */
package com.meli.backend.rapid.ws.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

    @GetMapping
    public RequestOutput getAllConcerts(@RequestParam(name = "rec_num", defaultValue = "0") int rec_num, @RequestParam(name = "offset", defaultValue = "30") int offset, @RequestBody (required=false) ConcertInput input) {
        ConcertRequestContext ctx = new ConcertRequestContext( input );
        ctx.recOutParam.setRecNum(rec_num);
        ctx.recOutParam.setOffset(offset);

        List<ConcertOutput> concerts = concertService.getAllConcerts(ctx);
        ctx.output.setData(concerts);

        try {
            if( ctx.isOnError() ) {
                throw new controllerException(ctx.output.getAppStatus().toHttpStatus());
            }
        } catch (Exception e) {
        }

        return ctx.output;
    }

    @GetMapping("/range")
    public RequestOutput getConcertsByRange(@RequestParam(name = "rec_num", defaultValue = "0") int rec_num, @RequestParam(name = "offset", defaultValue = "30") int offset, @RequestBody (required=false) ConcertRangeInput input) {
        ConcertRgRequestContext ctx = new ConcertRgRequestContext( input );
        ctx.recOutParam.setRecNum(rec_num);
        ctx.recOutParam.setOffset(offset);

        List<ConcertOutput> concerts = concertService.getConcertsByRange(ctx);
        ctx.output.setData(concerts);
        

        try {
            if( ctx.isOnError() ) {
                throw new controllerException(ctx.output.getAppStatus().toHttpStatus());
            }
        } catch (Exception e) {
        }

        return ctx.output;
    }
}
