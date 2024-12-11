/** 
 * This files containts the context for process the request url /concert/reserve
 */
package com.meli.backend.rapid.req_ctx.reserve;

import com.meli.backend.rapid.req_ctx.RequestContext;

/** Reserve request context */
public class ReserveRequestContext extends RequestContext {

    /** Input */
    public ReserveInput input;

    public ReserveRequestContext( ReserveInput input ) {
        super();
        this.input = input;
        if(this.input == null) {
            this.input = new ReserveInput();
        }

        this.output.setData(new ReserveOutput());       
    }
}

