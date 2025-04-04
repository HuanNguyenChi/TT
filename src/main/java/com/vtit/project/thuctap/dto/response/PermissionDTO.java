package com.vtit.project.thuctap.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private List<Long> idsRole;

    public PermissionDTO(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }
}
