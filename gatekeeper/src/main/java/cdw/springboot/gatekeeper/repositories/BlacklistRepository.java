package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.Blacklist;
import cdw.springboot.gatekeeper.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {
    @Query(value = "SELECT if(exists (SELECT v.visitor_id as id, v.email as email from blacklist b join visitors v on v.visitor_id = b.visitor_id where v.email = ?1 UNION SELECT u.user_id as id, u.email as email from blacklist b join users u on u.user_id= b.user_id where u.email = ?1) , true, false) as existsByEmail", nativeQuery = true)
    Long existsByEmail(String email);

    boolean existsByUser(Users user);

    @Modifying
    @Query(value = "insert into blacklist (visitor_id, blacklisted_by) values(?1, ?2)", nativeQuery = true)
    int blacklistVisitor(int id, int by);

    @Modifying
    @Query(value = "insert into blacklist (user_id, blacklisted_by) values(?1, ?2)", nativeQuery = true)
    int blacklistUser(int id, int by);
}
