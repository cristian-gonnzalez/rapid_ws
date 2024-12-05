/** 
 * This file containts the controller to process the POST request with url /concert/reserve
 */
package com.meli.backend.rapid.ws.controller;

import org.springframework.http.HttpStatus;
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

    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error")
    public class controllerException extends RuntimeException {
        public controllerException(HttpStatus status) {
            // TODO: handle http codes
        }
    }

    @PostMapping("/reserve")
    public RequestOutput reserveConcert(@RequestBody (required = true) ReserveInput input) {
        
        ReserveRequestContext ctx = new ReserveRequestContext(input );
        if( validateInput( ctx, ctx.input) ) {
            this.reserveService.reserveConcert(ctx);            
        }
        
        try {
            if( ctx.isOnError() ) {
                ctx.output.setData("");    
                throw new controllerException(ctx.output.getAppStatus().toHttpStatus());        
            }        
        } catch (Exception e) {
        }
        
        return ctx.output;
    }


    @DeleteMapping("/reserve")
    public RequestOutput deleteReserveConcert(@RequestBody (required = true) DelReserveInput input) {
        
        DelReserveRequestContext ctx = new DelReserveRequestContext(input );
        
        if( ctx.input.getReserveId() == null || ctx.input.getArtist() == null || 
            ctx.input.getPlace() == null || ctx.input.getConcertDate() == null || ctx.input.getSector() == null ) {
            ctx.setError(eRCode.lessOrTooMuchFields, "All fields are mandatory");
        }

        this.reserveService.deleteReserve( ctx );
        
        return ctx.output;
    }

}
