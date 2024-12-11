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
import com.meli.backend.rapid.req_ctx.place.PlaceInput;
import com.meli.backend.rapid.req_ctx.place.PlaceOutput;
import com.meli.backend.rapid.req_ctx.place.PlaceRequestContext;
import com.meli.backend.rapid.req_ctx.reserve.RequestOutput;
import com.meli.backend.rapid.ws.services.PlaceService;


@RestController
@RequestMapping("/place")
public class PlaceController {

    private PlaceService service;

    public PlaceController() {
        this.service = new PlaceService();
    }

    @PostMapping("")
    public ResponseEntity<RequestOutput> createPlace(
                @RequestBody (required=false) PlaceInput input) {
        
        PlaceRequestContext ctx = new PlaceRequestContext( input );
        if( input.getName() == null ) {
            ctx.setError(eRCode.missingField, "name is mandatory");
        }
       
        try {
            service.createPlace( ctx );
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(!ctx.isOnError())
                ctx.setError(eRCode.internalError, e.getMessage());
        }
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }
   
    @DeleteMapping("")
    public ResponseEntity<RequestOutput> deletePlace(
                @RequestBody (required=false) PlaceInput input) {
        PlaceRequestContext ctx = new PlaceRequestContext( input );
       
        if( input.getName() == null ) {
            ctx.setError(eRCode.missingField, "name is mandatory");
        }
        else {
            try {
                service.deletePlace( ctx );
            } catch (Exception e) {
                System.err.println(e.getMessage());
                if(!ctx.isOnError())
                    ctx.setError(eRCode.internalError, e.getMessage());
            }
        }
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }

    @GetMapping("")
    public ResponseEntity<RequestOutput> getPlace(
                @RequestBody (required=false) PlaceInput input) {
        
        PlaceRequestContext ctx = new PlaceRequestContext( input );
       
        try {
            List<PlaceOutput> records = service.getPlaces( ctx );
            ctx.output.setData(records);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(!ctx.isOnError())
                ctx.setError(eRCode.internalError, e.getMessage());
        }
        
        return new ResponseEntity<RequestOutput>(ctx.output, ctx.output.getAppStatus().toHttpStatus());
    }
   
 }
