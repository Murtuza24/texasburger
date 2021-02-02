package com.egen.texasburger.models;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Murtuza
 */


@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;

}
