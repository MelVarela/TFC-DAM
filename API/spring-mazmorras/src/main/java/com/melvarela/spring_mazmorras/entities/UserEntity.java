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
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {
    
    @Id
    @Column(name = "correo")
    String email;

    @Column(name = "nombre")
    String name;

    @Column(name = "contrasenna")
    String password;

    @Column(name = "foto_perfil")
    String profilePicture;

}
