package com.melvarela.spring_mazmorras.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.Ids.UserRelationId;
import com.melvarela.spring_mazmorras.entities.UserRelationEntity;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;

@Repository
public interface UserRelationRepository extends JpaRepository<UserRelationEntity, UserRelationId> {

    List<UserRelationEntity> findByCampannaEntity(CampaignEntity campaign);
    
}
