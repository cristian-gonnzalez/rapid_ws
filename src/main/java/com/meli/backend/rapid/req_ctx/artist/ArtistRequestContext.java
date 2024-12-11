package com.meli.backend.rapid.req_ctx.artist;

import com.meli.backend.rapid.req_ctx.RequestContext;

public class ArtistRequestContext extends RequestContext {

    public ArtistInput input;
    
    public ArtistRequestContext( ArtistInput input ) {
        super();
        this.input = input;
    }

}
