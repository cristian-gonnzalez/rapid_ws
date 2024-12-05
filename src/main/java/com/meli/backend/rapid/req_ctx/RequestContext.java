package com.meli.backend.rapid.req_ctx;

import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.req_ctx_io.RequestOutput;

public class RequestContext {

    /** Ourput */
    public RequestOutput output;

    public RequestContext() {
        this.output = new RequestOutput();
    }

    public Boolean isOnError() {
        return ( output.getAppStatus().getCode() != eRCode.success);
    }

    public void setError( eRCode code, String msg) {
        output.getAppStatus().setCode(code);
        output.getAppStatus().setMessage(msg);
    }
}
