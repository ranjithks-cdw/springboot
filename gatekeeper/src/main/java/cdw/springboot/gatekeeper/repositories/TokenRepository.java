package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,Integer> {
    boolean existsByToken(String token);
    void removeByToken(String token);
}