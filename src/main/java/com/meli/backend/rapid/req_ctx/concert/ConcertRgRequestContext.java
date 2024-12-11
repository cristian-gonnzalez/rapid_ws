/** 
 * This files containts the context for process the request url /concert/range
 */
package com.meli.backend.rapid.req_ctx.concert;

import com.meli.backend.rapid.req_ctx.RequestContext;
import com.meli.backend.rapid.req_ctx.RequestContextParam;

/** Concert range request context.
 *  
 */
public class ConcertRgRequestContext extends RequestContext {
    
    /** Request paramaters that specifies the number of records to give in the ouput */
    public RequestContextParam reqParam;

    /** Input request fields */
    public ConcertRangeInput input;
    
    /** Initializes the context
     * 
     * @param input Input request fields
     */
    public ConcertRgRequestContext( ConcertRangeInput input ) {
        super();
        this.input = input;
        if( this.input == null ) {
            this.input = new ConcertRangeInput();
        }
        reqParam = new RequestContextParam();
    }
}
