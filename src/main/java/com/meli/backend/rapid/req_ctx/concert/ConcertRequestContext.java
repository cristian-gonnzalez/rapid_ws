/** 
 * This files containts the context for process the request url /concert
 */
package com.meli.backend.rapid.req_ctx.concert;

import com.meli.backend.rapid.req_ctx.RequestContext;
import com.meli.backend.rapid.req_ctx.RequestContextParam;

/** Concert request context.
 *  
 */
public class ConcertRequestContext  extends RequestContext {
    
    /** Request paramaters that specifies the number of records to give in the ouput */
    public RequestContextParam reqParam;

    /** Input request fields */
    public ConcertInput input;

    /** Initializes the context
     * 
     * @param input Input request fields
     */
    public ConcertRequestContext( ConcertInput input ) {
        super();
        this.input = input;
        if( this.input == null ) {
            this.input = new ConcertInput();
        }
        reqParam = new RequestContextParam();
    }
}
