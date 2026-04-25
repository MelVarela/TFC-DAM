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
@Table(name = "notas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoteEntity {
    
    @Id
    String id;

    @Column(name = "nombre")
    String name;

    @Column(name = "contenido")
    String content;

    @Column(name = "dm")
    boolean isDm;

    @Column(name = "editando")
    boolean isEditing;

    @Column(name = "propietario")
    String owner;

}
