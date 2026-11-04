package edu.upc.campusnest.repository;

import edu.upc.campusnest.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByApplicationIdOrderBySentAtAscIdAsc(Long applicationId);
    Optional<Message> findFirstByApplicationIdOrderBySentAtDescIdDesc(Long applicationId);
}
