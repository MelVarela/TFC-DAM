package com.melvarela.spring_mazmorras.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.Ids.InventoryId;
import com.melvarela.spring_mazmorras.entities.InventoryEntity;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryId>{
    
    @Query("SELECT i FROM InventoryEntity i WHERE i.id.personaje = :personaje")
    List<InventoryEntity> findByPersonaje(String personaje);

    @Query("SELECT i FROM InventoryEntity i WHERE i.id.objeto = :objeto")
    List<InventoryEntity> findByObjeto(String objeto);

}
