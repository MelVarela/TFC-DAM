package com.melvarela.spring_mazmorras.entities;

import com.melvarela.spring_mazmorras.entities.Ids.InventoryId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InventoryEntity {
    
    @EmbeddedId
    private InventoryId id;

}
