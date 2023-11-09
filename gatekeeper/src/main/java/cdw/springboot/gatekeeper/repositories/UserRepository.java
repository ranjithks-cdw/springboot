package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update users u join user_info ui on u.user_id = ui.user_id set u.is_approved = ?2, u.is_active = ?2 where ui.user_id = ?1 and u.user_id = ?1 and (u.is_approved = 'no' or u.is_approved is null)", nativeQuery = true)
    int saveApproval(Integer userId, String isApproved);

    @Transactional
    @Modifying
    @Query(value = "update users u set u.is_active = 'no' where u.user_id = ?1 and u.is_approved = 'yes' ", nativeQuery = true)
    int deleteActiveUserById(Integer userId);
}
