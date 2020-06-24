package com.java.relay42.repository;

import com.java.relay42.entity.User;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer> {


   @AllowFiltering
    User findByUsername(String username);
}
