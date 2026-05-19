package com.melvarela.spring_mazmorras.entities.Ids;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InventoryId implements Serializable{
    
    @Column(name = "personaje")
    private String personaje;

    @Column(name = "objeto")
    private String objeto;

}
