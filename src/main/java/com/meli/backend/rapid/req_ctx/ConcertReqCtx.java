/** 
 * This files containts the context for process the request url /concert
 */
package com.meli.backend.rapid.req_ctx;

import com.meli.backend.rapid.common.RecordOutParam;
import com.meli.backend.rapid.req_ctx.req_ctx_io.*;

/** Concert request context.
 *  
 */
public class ConcertReqCtx  extends RequestContext {
    
    /** Request paramaters that specifies the number of records to give in the ouput */
    public RecordOutParam recOutParam;

    /** Input request fields */
    public ConcertInput input;

    /** Initializes the context
     * 
     * @param input Input request fields
     */
    public ConcertReqCtx( ConcertInput input ) {
        super();
        this.input = input;
        if( this.input == null ) {
            this.input = new ConcertInput();
        }
        recOutParam = new RecordOutParam();
    }
}
