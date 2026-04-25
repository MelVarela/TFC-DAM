package com.melvarela.spring_mazmorras.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "objetos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObjectEntity {
    
    @Id
    String id;

    @Column(name = "nombre")
    String name;

    @Column(name = "precio")
    float price;

    @Column(name = "foto")
    String picture;

    @ManyToOne
    @JoinColumn(name = "campanna")
    CampaignEntity campaign;

}
