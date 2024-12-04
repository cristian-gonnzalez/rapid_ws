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

    private boolean validateInput( ReserveReqCtx ctx,  ReserveInput input ) {
        // validates the mandatory fields before delegating to the service
        if( input.getArtist() == null) {
            ctx.output.getAppStatus().setCode(eRCode.missingField);
            ctx.output.getAppStatus().setMessage("Missing filed 'artist'");
            
            return false;
        }
        if( input.getPlace() == null) {
            ctx.output.getAppStatus().setCode(eRCode.missingField);
            ctx.output.getAppStatus().setMessage("Missing filed 'place'");
            
            return false;
        }
        if( input.getConcertDate() == null) {
            ctx.output.getAppStatus().setCode(eRCode.missingField);
            ctx.output.getAppStatus().setMessage("Missing filed 'concertDate'");
            
            return false;
        }
        if( input.getSector() == null) {
            ctx.output.getAppStatus().setCode(eRCode.missingField);
            ctx.output.getAppStatus().setMessage("Missing filed 'sector'");
            
            return false;
        }
        if( (input.getQuantity() == null && input.getSeats() == null) || 
            (input.getQuantity() != null && input.getSeats() != null)) {
            ctx.output.getAppStatus().setCode(eRCode.lessOrTooMuchFields);
            ctx.output.getAppStatus().setMessage("One field is required: 'qty' or 'seats'");
            
            return false;
        }
        if( input.getName() == null || input.getDNI() == null || 
            input.getSurname() == null ) {
            ctx.output.getAppStatus().setCode(eRCode.lessOrTooMuchFields);
            ctx.output.getAppStatus().setMessage("User name, surname and dni is required to reserve");
            
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
        
        ReserveReqCtx ctx = new ReserveReqCtx(input );
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

}
