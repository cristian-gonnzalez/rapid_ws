/**
 *  Generic response. It is used in all request since we want to give the same response format.
 */

package com.meli.backend.rapid.req_ctx.reserve;

import com.meli.backend.rapid.common.AppStatus;

/** Generic output response for request 
 * 
 */
public class RequestOutput {
    
    /** Data holds the output to give to the user. */
    private Object data;

    /** This field containts the app status information in order to clarify in case if an error occurs. */
    private AppStatus appStatus;

    public RequestOutput() {
        this.appStatus = new AppStatus();
    }

    public  void setAppStatus(AppStatus appStatus) {
        this.appStatus = appStatus;
    }
    
    public  AppStatus getAppStatus() {
        return this.appStatus;
    }

    public  void setData(Object data) {
        this.data = data;
    }
    
    public  Object getData() {
        return this.data;
    }
}
