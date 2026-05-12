package com.melvarela.spring_mazmorras.rest.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    String email;
    String name;
    String password;
    String profilePicture;

    @Override
    public String toString(){
        return "{email=" + this.email +",name=" + this.name + ",profilePicture=" + this.profilePicture + "}";
    }
}
