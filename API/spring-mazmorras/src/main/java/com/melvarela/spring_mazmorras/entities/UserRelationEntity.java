package com.melvarela.spring_mazmorras.entities;

import com.melvarela.spring_mazmorras.entities.Ids.UserRelationId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "usuarios_campannas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRelationEntity {

    @EmbeddedId
    UserRelationId id;

    @Column(name = "horario")
    String horario;

    @Column(name = "rol")
    char rol;

    @Column(name = "aceptada")
    boolean aceptada;

    @MapsId("usuario")
    @ManyToOne
    @JoinColumn(name = "usuario")
    UserEntity usuarioEntity;

    @MapsId("campanna")
    @ManyToOne
    @JoinColumn(name = "campanna")
    CampaignEntity campannaEntity;
}
