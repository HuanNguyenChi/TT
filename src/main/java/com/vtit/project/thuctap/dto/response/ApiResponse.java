package com.vtit.project.thuctap.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.constant.enums.ResponseCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    @Builder.Default
    private int code = ResponseCode.SUCCESS.getCode();
    @Builder.Default
    private String message = ResponseCode.SUCCESS.getMessage();
    private T result;

    public void setMessage(ResponseCode responseCode, ResponseObject responseObject) {
        this.message = responseCode.getMessage().replace("{entity}", responseObject.toString());
    }
    public void setMessage(ResponseCode responseCode) {
        this.message = responseCode.getMessage();
    }
    public ApiResponse(ResponseCode responseCode, T result) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.result = result;
    }

    public ApiResponse(ResponseCode responseCode, ResponseObject responseObject, T result) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage().replace("{entity}", responseObject.toString());
        this.result = result;
    }



}
