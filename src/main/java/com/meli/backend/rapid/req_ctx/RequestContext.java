package com.meli.backend.rapid.req_ctx;

import java.util.ArrayList;

import com.meli.backend.rapid.common.AppStatus.eRCode;
import com.meli.backend.rapid.req_ctx.reserve.RequestOutput;

public class RequestContext {

    public RequestOutput output;

    public RequestContext() {
        this.output = new RequestOutput();
        this.output.setData(new ArrayList<>());
    }

    public Boolean isOnError() {
        return ( output.getAppStatus().getCode() != eRCode.success);
    }

    public void setError( eRCode code, String msg) {
        output.getAppStatus().setCode(code);
        output.getAppStatus().setMessage(msg);
    }
}
