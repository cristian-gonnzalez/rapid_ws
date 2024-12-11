package com.meli.backend.rapid.req_ctx.place;

import com.meli.backend.rapid.req_ctx.RequestContext;

public class PlaceRequestContext extends RequestContext {
    public PlaceInput input;
    
    public PlaceRequestContext( PlaceInput input ) {
        super();
        if( input == null ) {
            this.input = new PlaceInput();
        }
        else {
            this.input = input;
        }
    }

}
