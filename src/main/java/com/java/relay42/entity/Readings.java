package com.java.relay42.entity;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

/**
 * A Readings.
 */
@Table("readings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Readings implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private ReadingsPrimaryKey key;

    private Double reading;

    private UUID id;

}
