package ru.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "locations", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @Column(name = "radius")
    private Float radius;

    @Column(name = "owner")
    private Long owner;
}
