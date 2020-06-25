package com.java.relay42.entity;

import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@PrimaryKeyClass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ReadingsPrimaryKey implements Serializable {

    private static final long serialVersionUID = -5336708854394928751L;


    @PrimaryKeyColumn(name = "station_id", type = PrimaryKeyType.PARTITIONED)
    private UUID stationId;

    @PrimaryKeyColumn(name = "id")
    private UUID id;


}
