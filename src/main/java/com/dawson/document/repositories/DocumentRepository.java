package com.dawson.document.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dawson.document.entities.DocumentData;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentData, String> {

}
