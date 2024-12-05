package com.meli.backend.rapid.req_ctx;

import com.meli.backend.rapid.req_ctx.req_ctx_io.DelReserveInput;
import com.meli.backend.rapid.req_ctx.req_ctx_io.RequestOutput;

public class DelReserveRequestContext extends RequestContext {
    
    /** Input */
    public DelReserveInput input;

    public DelReserveRequestContext( DelReserveInput input ) {
        super();
        this.input = input;
        this.output = new RequestOutput();
    }
}
