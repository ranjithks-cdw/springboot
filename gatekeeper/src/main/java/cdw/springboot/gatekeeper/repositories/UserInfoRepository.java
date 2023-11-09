package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByUserUserId(Integer userId);

    @Query(value = "select ui.* from user_info ui join users u on u.user_id = ui.user_id where u.is_active = 'yes' and u.is_approved = 'yes' and not exists (select b.user_id from blacklist b where u.user_id = b.user_id);", nativeQuery = true)
    List<UserInfo> findAllValidUsers();

    @Query(value = "select ui.* from user_info ui join users u on u.user_id = ui.user_id where u.is_active = 'yes' and u.is_approved = 'yes' and not exists (select b.user_id from blacklist b where ui.user_id = b.user_id) and ui.user_id = ?1", nativeQuery = true)
    Optional<UserInfo> findValidUserById(Integer id);

    @Query(value = "select ui.* from user_info ui join users u on u.user_id = ui.user_id where u.is_active = 'yes' and u.is_approved = 'yes' and not exists (select b.user_id from blacklist b where b.user_id = ui.user_id) and u.email = ?1", nativeQuery = true)
    Optional<UserInfo> findValidUsersByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update user_info ui join users u on u.user_id = ui.user_id set ui.approved_by = ?2 where ui.user_id = ?1 and u.user_id = ?1 and (u.is_approved = 'no' or u.is_approved is null)", nativeQuery = true)
    int saveApproval(Integer userId, int approvedBy);
}
