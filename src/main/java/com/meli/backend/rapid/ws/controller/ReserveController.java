/** 
 * This file containts the controller to process the POST request with url /concert/reserve
 */
package com.meli.backend.rapid.ws.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.*;
import com.meli.backend.rapid.req_ctx.req_ctx_io.*;
import com.meli.backend.rapid.ws.services.*;

@RestController
@RequestMapping("/concert")
public class ReserveController {
    
    private ReserveService reserveService;    
    
    public ReserveController() {
        this.reserveService = new ReserveService();
    }

    private boolean validateInput( ReserveRequestContext ctx,  ReserveInput input ) {
        // validates the mandatory fields before delegating to the service
        if( input.getArtist() == null || input.getPlace() == null || 
            input.getConcertDate() == null ||  input.getSector() == null) {
            ctx.setError(eRCode.missingField, "Missing filed");
            return false;
        }

        if( (input.getQuantity() == null && input.getSeats() == null) || 
            (input.getQuantity() != null && input.getSeats() != null)) {
            ctx.setError(eRCode.lessOrTooMuchFields, "One field is required: 'qty' or 'seats'");
            return false;
        }
        if( input.getName() == null || input.getDNI() == null || input.getSurname() == null ) {
            ctx.setError(eRCode.lessOrTooMuchFields, "User name, surname and dni is required to reserve");
            return false;
        }

        return true;
    }

    
    @PostMapping("/reserve")
    public ResponseEntity<RequestOutput> getReserves(@RequestBody (required = true) ReserveInput input) {
        
        ReserveRequestContext ctx = new ReserveRequestContext(input );
        if( validateInput( ctx, ctx.input) ) {
            this.reserveService.reserveConcert(ctx);            
        }
        
        ResponseEntity<RequestOutput> re =  null;
        try {
            
            if(ctx.isOnError())
                ctx.output.setData(new ArrayList<>());

            re = new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
        } catch (Exception e) {
        }
        return re;
    }

    
    @GetMapping("/reserve")
    public ResponseEntity<String> reserveConcert(@RequestBody (required = true) ReserveInput input) {
        
        ResponseEntity<String> re =  null;
        re = new ResponseEntity<String>("Not implemented", HttpStatus.SERVICE_UNAVAILABLE);
        return re;
    }


    @DeleteMapping("/reserve")
    public ResponseEntity<RequestOutput> deleteReserveConcert(@RequestBody (required = true) DelReserveInput input) {
        
        DelReserveRequestContext ctx = new DelReserveRequestContext(input );
        
        if( ctx.input.getReserveId() == null || ctx.input.getArtist() == null || 
            ctx.input.getPlace() == null || ctx.input.getConcertDate() == null || ctx.input.getSector() == null ) {
            ctx.setError(eRCode.lessOrTooMuchFields, "All fields are mandatory");
        }
        ctx.output.setData(new ArrayList<>());

        ResponseEntity<RequestOutput> re =  null;
        try {
            this.reserveService.deleteReserve( ctx );

            if(ctx.isOnError())
                ctx.output.setData(new ArrayList<>());

            re = new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
        } catch (Exception e) {
        }
        return re;
    }
}
