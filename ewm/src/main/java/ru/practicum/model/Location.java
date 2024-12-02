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

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return (name.equals(location.name) && description.equals(location.description) &&
                lat.equals(location.lat) && lon.equals(location.lon) && owner.equals(location.owner));
    }
}
