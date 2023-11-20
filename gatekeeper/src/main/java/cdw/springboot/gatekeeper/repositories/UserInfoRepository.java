package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    @Query(value = "select ui.* from user_info ui join users u on u.user_id = ui.user_id where u.is_active = 'yes' and u.is_approved = 'yes' and u.email = ?1", nativeQuery = true)
    Optional<UserInfo> findValidUsersByEmail(String email);

    boolean existsByUserEmail(String email);

    Optional<UserInfo> findByUserUserId(Integer userId);
}
