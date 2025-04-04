package com.vtit.project.thuctap.exception;

import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.constant.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThucTapException extends RuntimeException {
    private ResponseCode responseCode;
    private ResponseObject responseObject;
    private String message;


    public ThucTapException(ResponseCode responseCode, ResponseObject responseObject) {
        super(responseCode.getMessage().replace("{entity}", responseObject.toString()));
        this.responseCode = responseCode;
        this.responseObject = responseObject;
    }
}
