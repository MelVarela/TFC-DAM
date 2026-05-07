package com.melvarela.spring_mazmorras.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.SuggestionEntity;

@Repository
public interface SuggestionRepository extends MongoRepository<SuggestionEntity, Integer>{
    
}
