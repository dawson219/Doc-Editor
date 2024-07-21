package com.dawson.document.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dawson.document.entities.JWTDetails;

@Repository
public interface JWTRepository extends MongoRepository<JWTDetails, String> {

}
