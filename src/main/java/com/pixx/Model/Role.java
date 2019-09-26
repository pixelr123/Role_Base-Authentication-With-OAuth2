package com.pixx.Model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

@Entity
@Table(name = "ROLES")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)

    private RoleType name;

    private String description;
    @CreatedDate
    private Long createdOn;
    @LastModifiedDate
    private Long modifiedOn;
}
