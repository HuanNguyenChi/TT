package com.vtit.project.thuctap.dto.request;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class UpdateUserRequest {
    private Long id;
    private String username;
    private String password;
    private String fullname;
    private String phone;
    private String email;
    private String address;
    private Timestamp dob;
    private String identityNumber;
    private Boolean isActive;
    private Boolean isDeleted;
    private List<Long> idsRole;
}
