package com.vtit.project.thuctap.dto.response;

import lombok.Builder;


@Builder
public class ResponseMessage {
    private String respCode;
    private String respDesc;
    private Object data;
}
