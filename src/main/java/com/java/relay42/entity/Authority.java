package com.java.relay42.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.domain.Persistable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * An authority (a security role) used by Spring Security.
 */
@Table("authority")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Authority implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    private String name;

    @Override
    public String getId() {
        return name;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
