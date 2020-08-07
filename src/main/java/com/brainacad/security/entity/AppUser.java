package com.brainacad.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(unique=true, length = 36, nullable = false)
    private String userName;

    @Column(length = 128, nullable = false)
    private String encrytedPassword;

    @Column(length = 1, nullable = false)
    private boolean enabled;
}