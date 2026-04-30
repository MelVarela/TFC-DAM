package com.melvarela.spring_mazmorras.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.CreatureEntity;

@Repository
public interface CreatureRepository extends JpaRepository<CreatureEntity, String> {

    List<CreatureEntity> findByCampaign(CampaignEntity campaign);
    
}
