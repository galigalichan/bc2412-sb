package com.bootcamp.sb.demo_sb_bc_forum.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
@Table(name = "Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;

    @Version // This is the version field for optimistic locking
    private Integer version;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AddressEntity address;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CompanyEntity company;

}