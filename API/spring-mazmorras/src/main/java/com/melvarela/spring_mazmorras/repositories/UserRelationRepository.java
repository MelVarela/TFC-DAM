package com.melvarela.spring_mazmorras.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.CampaignEntity;
import com.melvarela.spring_mazmorras.entities.Ids.UserRelationId;
import com.melvarela.spring_mazmorras.entities.UserRelationEntity;

@Repository
public interface UserRelationRepository extends JpaRepository<UserRelationEntity, UserRelationId> {

    List<UserRelationEntity> findByCampannaEntity(CampaignEntity campaign);
    
    @Query("SELECT r FROM UserRelationEntity r WHERE r.aceptada = false AND r.usuarioEntity.email = :userId")
    List<UserRelationEntity> findByPlayerPending(String userId);
    
}
