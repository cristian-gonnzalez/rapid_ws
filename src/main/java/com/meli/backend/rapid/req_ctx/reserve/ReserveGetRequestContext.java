package com.meli.backend.rapid.req_ctx.reserve;

import com.meli.backend.rapid.req_ctx.RequestContext;
import com.meli.backend.rapid.req_ctx.RequestContextParam;

public class ReserveGetRequestContext  extends RequestContext {

    /** Request paramaters that specifies the number of records to give in the ouput */
    public RequestContextParam reqParam;

    /** Input */
    public ReserveGetInput input;

    public ReserveGetRequestContext( ReserveGetInput input ) {
        super();
        this.input = input;
        if( this.input == null ) {
            this.input = new ReserveGetInput();
        }

        this.reqParam = new RequestContextParam();
    }
}
