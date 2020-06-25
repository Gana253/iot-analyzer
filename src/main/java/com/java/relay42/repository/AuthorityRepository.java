package com.java.relay42.repository;


import com.java.relay42.entity.Authority;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * Spring Data R2DBC repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends CassandraRepository<Authority, String> {
}
