package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.Blacklist;
import cdw.springboot.gatekeeper.entities.Visitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
    boolean existsByVisitorEmail(String email);
    boolean existsByVisitor(Visitors visitor);
}
