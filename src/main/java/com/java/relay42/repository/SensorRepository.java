package com.java.relay42.repository;


import com.java.relay42.entity.Sensor;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data Cassandra repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceRepository extends CassandraRepository<Sensor, UUID> {
}
