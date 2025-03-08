package com.bootcamp.sb.demo_bc_mtr_station.entity;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.LineCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Lines")
public class LineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Enumerated(EnumType.STRING) // Store enum as a string
    @Column(name = "line_code")
    private LineCode lineCode;

    private String description; // Add description field

}
