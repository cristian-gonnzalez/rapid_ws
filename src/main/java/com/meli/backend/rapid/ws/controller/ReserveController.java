/** 
 * This file containts the controller to process the POST request with url /concert/reserve
 */
package com.meli.backend.rapid.ws.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.reserve.*;
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
            ctx.setError(eRCode.missingField, "One field is required: 'qty' or 'seats'");
            return false;
        }
        if( input.getName() == null || input.getDNI() == null || input.getSurname() == null ) {
            ctx.setError(eRCode.missingField, "User name, surname and dni is required to reserve");
            return false;
        }

        return true;
    }

    
    @PostMapping("/reserve")
    public ResponseEntity<RequestOutput> createReserve(@RequestBody (required = true) ReserveInput input) { 
        ReserveRequestContext ctx = new ReserveRequestContext(input );
        
        if( validateInput( ctx, ctx.input) ) {
            this.reserveService.createReserve(ctx);            
        }
            
        if(ctx.isOnError())
            ctx.output.setData(new ArrayList<>());

        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

    
    @GetMapping("/reserve")
    public ResponseEntity<RequestOutput> getReserves(@RequestParam(name = "rec_num", defaultValue = "0") int rec_num, 
                                                     @RequestParam(name = "offset", defaultValue = "30") int offset, 
                                                     @RequestBody (required=false) ReserveGetInput input) {
                                                        ReserveGetRequestContext ctx = new ReserveGetRequestContext( input );
        ctx.reqParam.setRecNum(rec_num);
        ctx.reqParam.setOffset(offset);

        try {
            List<ReserveOutput> reserves = reserveService.getReserves(ctx);
            ctx.output.setData(reserves);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if( !ctx.isOnError() ) {
                ctx.setError(eRCode.internalError, "Unknow error");
                ctx.output.setData(new ArrayList<>());
            }   
        }

        return new ResponseEntity<RequestOutput>(ctx.output, 
                                                 ctx.output.getAppStatus().toHttpStatus());
    }


    @DeleteMapping("/reserve")
    public ResponseEntity<RequestOutput> deleteReserve(@RequestBody (required = true) ReserveDelInput input) {
        
        ReserveDelRequestContext ctx = new ReserveDelRequestContext(input );
        
        if( ctx.input.getReserveId() == null || ctx.input.getArtist() == null || 
            ctx.input.getPlace() == null || ctx.input.getConcertDate() == null || ctx.input.getSector() == null ) {
            ctx.setError(eRCode.missingField, "All fields are mandatory");
        }
        ctx.output.setData(new ArrayList<>());

        try {
       
            this.reserveService.deleteReserve( ctx );

            if(ctx.isOnError())
                ctx.output.setData(new ArrayList<>());

        } catch (Exception e) {
            System.err.println(e.getMessage());
            if( !ctx.isOnError() ) {
                ctx.setError(eRCode.internalError, "Unknow error");
                ctx.output.setData(new ArrayList<>());
            }   
        }

        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }
}
