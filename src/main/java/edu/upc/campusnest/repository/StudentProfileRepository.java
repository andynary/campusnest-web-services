package edu.upc.campusnest.repository;

import edu.upc.campusnest.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    // para que el servicio pueda encontrar el perfil por el ID del usuario
    Optional<StudentProfile> findByUserId(Long userId);

}