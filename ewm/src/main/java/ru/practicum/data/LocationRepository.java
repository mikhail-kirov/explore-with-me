package ru.practicum.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("select al " +
            "from Location as al " +
            "where al.name ilike ?1 " +
            "or al.description = ?1 " +
            "order by al.id asc")
    List<Location> findByNameAndDescription(String text);

    @Query("select loc " +
           "from Location as loc " +
           "where distance(?1, ?2, loc.lat, loc.lon) < ?3 " +
           "order by loc.radius asc")
    List<Location> findByNearLocations(float latitude, float longitude, float radius);

    Location findByNameAndDescriptionAndLatAndLon(String name, String description, double lat, double lon);

    List<Location> findAllByOwner(long owner);
}
