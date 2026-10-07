package edu.upc.campusnest.repository;

import edu.upc.campusnest.model.IncludedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncludedServiceRepository extends JpaRepository<IncludedService, Long> {
}