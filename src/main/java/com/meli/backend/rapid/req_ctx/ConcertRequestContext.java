/** 
 * This files containts the context for process the request url /concert
 */
package com.meli.backend.rapid.req_ctx;

import com.meli.backend.rapid.common.RequestParam;
import com.meli.backend.rapid.req_ctx.req_ctx_io.*;

/** Concert request context.
 *  
 */
public class ConcertRequestContext  extends RequestContext {
    
    /** Request paramaters that specifies the number of records to give in the ouput */
    public RequestParam reqParam;

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
        reqParam = new RequestParam();
    }
}
