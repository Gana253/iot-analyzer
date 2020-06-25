package com.java.relay42.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * A Readings.
 */
@UserDefinedType("readings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Readings_UserDefined implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    private String reading;

    @CreatedDate
    @Column("created_date")
    @JsonIgnore
    private Instant createdDate = Instant.now();

}
