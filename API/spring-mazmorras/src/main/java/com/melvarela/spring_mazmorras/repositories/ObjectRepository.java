package com.melvarela.spring_mazmorras.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.ObjectEntity;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectEntity, String> {

    List<ObjectEntity> findByCampaign(String campaignId);
    
}
