package com.vtit.project.thuctap.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class CreateUserRequest {
    @Size(min = 6, message = "USERNAME_INVALID")
    private String username;
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private String fullname;
    private String phone;
    private String email;
    private String address;
    private Timestamp dob;
    private String identityNumber;
}
