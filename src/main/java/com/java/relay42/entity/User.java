package com.java.relay42.entity;

import com.java.relay42.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = -2199168069671744980L;
    @ElementCollection(fetch = FetchType.EAGER)
    List<Role> roles;
    @PrimaryKey
    private UUID id;
    @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = false, nullable = false)
    private String email;
    @Size(min = 8, message = "Minimum password length: 8 characters")
    private String password;
}
