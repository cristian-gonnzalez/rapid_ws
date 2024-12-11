package com.meli.backend.rapid.req_ctx.reserve;

import com.meli.backend.rapid.req_ctx.RequestContext;

public class ReserveDelRequestContext extends RequestContext {
    
    /** Input */
    public ReserveDelInput input;

    public ReserveDelRequestContext( ReserveDelInput input ) {
        super();
        this.input = input;
        this.output = new RequestOutput();
    }
}
