/** 
 * This files containts the context for process the request url /concert/reserve
 */
package com.meli.backend.rapid.req_ctx;

import com.meli.backend.rapid.req_ctx.req_ctx_io.*;

/** Reserve request context */
public class ReserveRequestContext extends RequestContext {

    /** Input */
    public ReserveInput input;

    public ReserveRequestContext( ReserveInput input ) {
        super();
        this.input = input;
        this.output = new RequestOutput();
        this.output.setData(new ReserveOutput());
    }
}

