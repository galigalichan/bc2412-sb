package com.bootcamp.sb.demo_bc_mtr_station.entity;

import com.bootcamp.sb.demo_bc_mtr_station.entity.code.StationCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Stations")
public class StationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Enumerated(EnumType.STRING) // Store enum as a string
    @Column(name = "station_code")
    private StationCode stationCode;

    private String description; // Add description field
    
    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private LineEntity line;

    // Add a method to get the lineId
    public Long getLineId() {
        return line != null ? line.getId() : null;
    }
}
