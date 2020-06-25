package com.java.relay42.repository;


import com.java.relay42.entity.Readings;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data Cassandra repository for the Readings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReadingsRepository extends CassandraRepository<Readings, UUID> {
}
