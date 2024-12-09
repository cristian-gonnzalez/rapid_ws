package com.meli.backend.rapid.req_ctx;

import com.meli.backend.rapid.req_ctx.req_ctx_io.GetReserveInput;

public class GetReserveRequestContext  extends RequestContext {

    /** Request paramaters that specifies the number of records to give in the ouput */
    public RequestContextParam reqParam;

    /** Input */
    public GetReserveInput input;

    public GetReserveRequestContext( GetReserveInput input ) {
        super();
        this.input = input;
        if( this.input == null ) {
            this.input = new GetReserveInput();
        }

        this.reqParam = new RequestContextParam();
    }
}
