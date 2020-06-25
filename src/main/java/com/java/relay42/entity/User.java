package com.java.relay42.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.relay42.constants.IotConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("user")
public class User extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = -2199168069671744980L;

    @PrimaryKey
    private String id;

    @NotNull
    @Pattern(regexp = IotConstants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(min = 2, max = 10)
    @com.datastax.driver.mapping.annotations.Column(name = "lang_key")
    private String langKey;

    @JsonIgnore
    private Set<String> authorities = new HashSet<>();
}
