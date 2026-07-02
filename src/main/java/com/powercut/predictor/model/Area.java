package com.powercut.predictor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "areas")
@Data
@NoArgsConstructor
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String city;
    private String pincode;
    private Double latitude;
    private Double longitude;

    @JsonIgnore
    @OneToMany(mappedBy = "area",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<OutageReport> reports;
}
