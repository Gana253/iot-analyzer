package com.java.relay42.entity;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

/**
 * A Readings.
 */
@Table("readings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Readings extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @PrimaryKey
    private ReadingsPrimaryKey key;

    private String reading;


}
