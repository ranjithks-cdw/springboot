package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.Visitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorsRepository extends JpaRepository<Visitors, Integer> {
    Optional<Visitors> findByEmail(String email);
}
