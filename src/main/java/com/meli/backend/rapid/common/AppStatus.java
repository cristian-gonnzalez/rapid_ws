package com.meli.backend.rapid.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppStatus {
    
    public enum eRCode {
        success,
        missingField,
        invalidField,
        invalidFieldContent,
        notFound,
        noRoomAvailable,
        alreadyRserved,
        internalError
    };
    
    @JsonIgnore
    private Map<eRCode, HttpStatus > codoToHttpStatus;

    private eRCode app_rc;

    private String app_msg;

    public AppStatus() {
        this.codoToHttpStatus = new HashMap<>();
        loadHttpSatatusMap();

        this.app_rc = eRCode.success;
        this.app_msg = "Success";
    }

    private void loadHttpSatatusMap() {
        this.codoToHttpStatus.put(eRCode.success, HttpStatus.OK);
        this.codoToHttpStatus.put(eRCode.missingField, HttpStatus.NOT_FOUND);
        this.codoToHttpStatus.put(eRCode.invalidField, HttpStatus.BAD_REQUEST);
        this.codoToHttpStatus.put(eRCode.invalidFieldContent, HttpStatus.BAD_REQUEST);
        this.codoToHttpStatus.put(eRCode.notFound, HttpStatus.NOT_FOUND);
        this.codoToHttpStatus.put(eRCode.noRoomAvailable, HttpStatus.BAD_REQUEST);
        this.codoToHttpStatus.put(eRCode.alreadyRserved, HttpStatus.BAD_REQUEST);
        this.codoToHttpStatus.put(eRCode.internalError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @JsonIgnore
    public HttpStatus toHttpStatus() {
        return this.codoToHttpStatus.get(app_rc);
    }

    public void setCode(eRCode app_rc) {
        this.app_rc = app_rc;
    }
    public eRCode getCode() {
        return this.app_rc;
    }
    public void setMessage(String app_msg) {
        this.app_msg = app_msg;
    }
    public String getMessage() {
        return this.app_msg;   
    }    
}
