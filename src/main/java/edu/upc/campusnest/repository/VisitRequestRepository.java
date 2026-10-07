package edu.upc.campusnest.repository;

import edu.upc.campusnest.model.VisitRequest;
import edu.upc.campusnest.model.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface VisitRequestRepository extends JpaRepository<VisitRequest, Long> {
    List<VisitRequest> findByRoomId(Long roomId);
    boolean existsByRoomIdAndVisitDateTimeAndStatus(Long roomId, LocalDateTime visitDateTime, VisitStatus status);
}
