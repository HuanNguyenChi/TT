package com.vtit.project.thuctap.dto.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ListUserRequest {
    private String fullname;
    private String phone;
    private String identityNumber;
    private String address;
    private Boolean isActive=true;
    private Boolean isDeleted;
}
