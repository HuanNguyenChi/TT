package com.vtit.project.thuctap.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private Timestamp dob;
    private String identityNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isActive = true;
    private Boolean isDeleted;

    public UserDTO(String fullname, String email, String phone, String address, Timestamp dob, String identityNumber, Timestamp createdAt, Timestamp updatedAt, Boolean isActive, Boolean isDeleted) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.identityNumber = identityNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }
}
