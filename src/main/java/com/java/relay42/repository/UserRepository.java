package com.java.relay42.repository;

import com.java.relay42.entity.User;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data Cassandra repository for the User entity.
 */
@Repository
public interface UserRepository extends CassandraRepository<User, String> {


    @AllowFiltering
    Optional<User> findOneByLogin(String login);

    @AllowFiltering
    Optional<User> findOneByEmailIgnoreCase(String email);
}

