package com.dawson.document.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dawson.document.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
}
