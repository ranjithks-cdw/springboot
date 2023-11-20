package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.VisitRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRequestsRepository extends JpaRepository<VisitRequests, Integer> {
    List<VisitRequests> findAllByDate(LocalDate date);
}
