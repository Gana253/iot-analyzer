package com.java.relay42.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.cassandra.core.mapping.Column;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base abstract class for entities which will hold definitions for created, last modified, created by,
 * last modified by attributes.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column("created_by")
    @JsonIgnore
    private String createdBy;

    @CreatedDate
    @Column("created_date")
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @Column("last_modified_by")
    @JsonIgnore
    private String lastModifiedBy;

    @LastModifiedDate
    @Column("last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();
}
