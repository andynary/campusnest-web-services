package edu.upc.campusnest.repository;

import edu.upc.campusnest.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByOwnerEmail(String email);
}
