package com.java.relay42.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

/**
 * A Sensor entity class
 */
@Table("sensor")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Sensor extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;


    private UUID stationId;


    private String stationName;


    private String sensorName;

    private String clientName;

    private String location;

    private String unit;


}
