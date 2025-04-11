package com.vtit.project.thuctap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "permission")
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(unique = true)
    private String code;
    @Column
    private String description;

    @ManyToMany(mappedBy = "permissionList", fetch = FetchType.LAZY)
    private List<Role> roleList;
}
