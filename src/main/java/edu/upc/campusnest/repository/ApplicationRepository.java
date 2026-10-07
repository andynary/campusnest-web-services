package edu.upc.campusnest.repository;

import edu.upc.campusnest.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByRoomId(Long roomId);
    boolean existsByRoomIdAndApplicantId(Long roomId, Long applicantId);
}
