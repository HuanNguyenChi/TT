package com.vtit.project.thuctap.dto.request;

import lombok.Data;

@Data
public class ListPermissionRequest {
    private String name;
    private String code;
    private String description;
}
