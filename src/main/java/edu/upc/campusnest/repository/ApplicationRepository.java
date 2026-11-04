package edu.upc.campusnest.repository;

import edu.upc.campusnest.model.Application;
import edu.upc.campusnest.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByRoomId(Long roomId);
    boolean existsByRoomIdAndApplicantId(Long roomId, Long applicantId);
    List<Application> findByApplicantEmail(String email);
    List<Application> findByStatusAndApplicantEmail(ApplicationStatus status, String email);
    List<Application> findByStatusAndRoom_Property_Owner_Email(ApplicationStatus status, String email);
}
