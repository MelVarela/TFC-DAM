package com.melvarela.spring_mazmorras.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "criaturas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatureEntity {
    
    @Id
    String id;

    @Column(name = "nombre")
    String name;

    @Column(name = "raza")
    String race;

    @Column(name = "foto")
    String picture;

    @Column(name = "campanna")
    String campaign;

}
