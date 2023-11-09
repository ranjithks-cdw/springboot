package cdw.springboot.gatekeeper.repositories;

import cdw.springboot.gatekeeper.entities.VisitRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRequestsRepository extends JpaRepository<VisitRequests, Integer> {
    @Query(value = "select vr.* from visit_requests vr join visitors v on v.visitor_id = vr.visitor_id join users u on u.user_id = vr.requested_by where vr.request_id = ?1 and (vr.is_approved = 'no' or vr.is_approved is null) and not exists (select b.user_id from blacklist b where b.user_id = u.user_id) and not exists (select b.visitor_id from blacklist b where b.visitor_id = v.visitor_id) and requested_date >= current_date()", nativeQuery = true)
    Optional<VisitRequests> findValidRequestById(Integer id);

    Optional<List<VisitRequests>> findAllByDate(LocalDate date);

    @Transactional
    @Modifying
    @Query(value = "delete vr from visit_requests vr join visitors v on v.visitor_id = vr.visitor_id join users u on u.user_id = vr.requested_by where vr.request_id = ?1 and (vr.is_approved = 'no' or vr.is_approved is null) and not exists (select b.user_id from blacklist b where b.user_id = u.user_id) and not exists (select b.visitor_id from blacklist b where b.visitor_id = v.visitor_id) and requested_date >= current_date()", nativeQuery = true)
    int deleteValidRequestById(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update visit_requests vr join visitors v on v.visitor_id = vr.visitor_id join users u on u.user_id = vr.requested_by set vr.is_approved = ?2, approved_by = ?3, vr.entry_time = current_timestamp() where vr.request_id = ?1 and (vr.is_approved = 'no' or vr.is_approved is null) and not exists (select b.user_id from blacklist b where b.user_id = u.user_id) and not exists (select b.visitor_id from blacklist b where b.visitor_id = v.visitor_id) and not exists (select b.user_id from blacklist b where b.user_id = ?3) and requested_date = current_date()", nativeQuery = true)
    int saveApproval(Integer id, String isApproved, int approvedBy);
}
