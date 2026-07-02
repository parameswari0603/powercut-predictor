package com.powercut.predictor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String role = "ROLE_USER";
    private boolean alertsEnabled = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "area_id",
            referencedColumnName = "id")
    private Area area;
}
