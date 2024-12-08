package com.meli.backend.rapid.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppStatus {
    
    public enum eRCode {
        success,
        missingField,
        invalidFields,
        lessOrTooMuchFields,
        valueNotFound,
        noRoomAvailable,
        seatReserved,
        failToApllyEffect,
        unknownError
    };
    
    @JsonIgnore
    private Map<eRCode, HttpStatus > codoToHttpStatus;

    private eRCode app_rc;

    private String app_msg;

    public AppStatus() {
        codoToHttpStatus = new HashMap<>();
        loadHttpSatatusMap();

        this.app_rc = eRCode.success;
        this.app_msg = "Success";
    }

    private void loadHttpSatatusMap() {
        codoToHttpStatus.put(eRCode.success, HttpStatus.OK);
        codoToHttpStatus.put(eRCode.missingField, HttpStatus.NOT_FOUND);
        codoToHttpStatus.put(eRCode.invalidFields, HttpStatus.BAD_REQUEST);
        codoToHttpStatus.put(eRCode.lessOrTooMuchFields, HttpStatus.BAD_REQUEST);
        codoToHttpStatus.put(eRCode.valueNotFound, HttpStatus.BAD_REQUEST);
        codoToHttpStatus.put(eRCode.noRoomAvailable, HttpStatus.BAD_REQUEST);
        codoToHttpStatus.put(eRCode.seatReserved, HttpStatus.BAD_REQUEST);
        codoToHttpStatus.put(eRCode.failToApllyEffect, HttpStatus.BAD_REQUEST);
        codoToHttpStatus.put(eRCode.unknownError, HttpStatus.BAD_REQUEST);
    }

    @JsonIgnore
    public HttpStatus toHttpStatus() {
        return codoToHttpStatus.get(app_rc);
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
