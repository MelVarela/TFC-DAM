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
@Table(name = "personajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CharacterEntity {
    
    @Id
    String id;

    @Column(name = "nombre")
    String name;

    String clase;

    @Column(name = "subclase")
    String subclass;

    int pg;

    @Column(name = "pgmaximos")
    int maxPg;

    @Column(name = "foto")
    String picture;
    
    @Column(name = "campanna")
    String campaign;

}
