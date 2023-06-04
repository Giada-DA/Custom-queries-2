package co.develhope.customqueries2.repositories;

import co.develhope.customqueries2.entities.Flight;
import co.develhope.customqueries2.entities.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository <Flight, Long> {

    Page<Flight> findByStatus(FlightStatus status, Pageable pageable);

    @Query("SELECT f FROM Flight f WHERE f.status = :status1 OR f.status = :status2")
    Page<Flight> findByStatus(@Param("status1") FlightStatus status1, @Param("status2") FlightStatus status2, Pageable pageable);

}
