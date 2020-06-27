package com.java.relay42.repository;


import com.java.relay42.entity.ReadingValue;
import com.java.relay42.entity.Readings;
import com.java.relay42.entity.ReadingsPrimaryKey;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data Cassandra repository for the Readings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReadingsRepository extends CassandraRepository<Readings, ReadingsPrimaryKey> {
    @AllowFiltering
    @Query(value = "select reading from readings where created_date > :start  and created_date < :end and station_id= :stationId;")
    List<ReadingValue> findAllReadingValueByKey(Instant start, Instant end, UUID stationId);

    @AllowFiltering
    @Query(value = "select Avg(reading) from readings where created_date > :start  and created_date < :end and station_id= :stationId;")
    BigInteger findAverageByKey(Instant start, Instant end, UUID stationId);

    @AllowFiltering
    @Query(value = "select Max(reading) from readings where created_date > :start  and created_date < :end and station_id= :stationId;")
    BigInteger findMaxValueByKey(Instant start, Instant end, UUID stationId);

    @AllowFiltering
    @Query(value = "select Min(reading) from readings where created_date > :start  and created_date < :end and station_id= :stationId;")
    BigInteger findMinValueByKey(Instant start, Instant end, UUID stationId);

}
