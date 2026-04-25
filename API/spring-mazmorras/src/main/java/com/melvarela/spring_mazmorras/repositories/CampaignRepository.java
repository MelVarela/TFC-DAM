package com.melvarela.spring_mazmorras.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity, String>{
    
    @Query("SELECT c FROM CampaignEntity c JOIN UserRelationEntity ur ON c.id = ur.id.campanna WHERE ur.id.usuario = :play")
    List<CampaignEntity> findAllByPlayer(@Param("play") String playerId);

}
