package com.melvarela.spring_mazmorras.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.melvarela.spring_mazmorras.entities.Ids.InventoryId;
import com.melvarela.spring_mazmorras.entities.InventoryEntity;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryId>{
    
    List<InventoryEntity> findByPersonaje(String personaje);
    List<InventoryEntity> findByObjeto(String objeto);

}
