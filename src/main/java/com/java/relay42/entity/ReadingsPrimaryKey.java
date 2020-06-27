package com.java.relay42.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Primary for Readings Table
 */
@PrimaryKeyClass
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ReadingsPrimaryKey implements Serializable {

    private static final long serialVersionUID = -5336708854394928751L;


    @PrimaryKeyColumn(name = "created_date", type = PrimaryKeyType.CLUSTERED)
    private Instant createdDate = Instant.now();

    @PrimaryKeyColumn(name = "station_id", type = PrimaryKeyType.PARTITIONED)
    private UUID stationId;

    public ReadingsPrimaryKey(UUID stationId) {
        this.stationId = stationId;
    }
}
