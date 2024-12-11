package com.meli.backend.rapid.req_ctx;

import com.meli.backend.rapid.req_ctx.req_ctx_io.ArtistInput;

public class ArtistRequestContext extends RequestContext {

    public ArtistInput input;
    
    public ArtistRequestContext( ArtistInput input ) {
        super();
        this.input = input;
    }

}
