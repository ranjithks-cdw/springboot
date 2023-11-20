package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.entities.VisitRequests;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.GetVisitRequestResident200Response;
import cdw.springboot.gatekeeper.model.ScheduleResponse;
import cdw.springboot.gatekeeper.repositories.BlacklistRepository;
import cdw.springboot.gatekeeper.repositories.VisitRequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitorServiceImpl implements VisitorService {
    @Autowired
    BlacklistRepository blacklistRepository;

    @Autowired
    VisitRequestsRepository visitRequestsRepository;
    /**
     * @param date
     * @param email
     * @return
     */
    @Override
    public GetVisitRequestResident200Response getVisitDetails(LocalDate date, String email) {
        if(blacklistRepository.existsByVisitorEmail(email)) {
            throw new GatekeeperException(AppConstants.ERROR_BLACKLISTED_USER, HttpStatus.UNAUTHORIZED);
        }
        List<VisitRequests> allVisitRequests = visitRequestsRepository.findAllByDate(date);

        List<ScheduleResponse> data = allVisitRequests.stream()
                .filter(schedule -> schedule.getVisitor().getEmail().equals(email))
                .map(schedule -> {
                    ScheduleResponse response = new ScheduleResponse();
                    response.setRequestId(schedule.getRequestId());
                    response.setVisitorId(schedule.getVisitor().getVisitorId());
                    response.setVisitorName(schedule.getVisitor().getVisitorName());
                    response.setRequestedDate(schedule.getDate());
                    response.setMobileNumber(schedule.getVisitor().getMobileNumber());
                    response.setGender(schedule.getVisitor().getGender());
                    response.setEmail(schedule.getVisitor().getEmail());
                    response.setPasskey(schedule.getPasskey());
                    response.setAddress(schedule.getVisitor().getAddress());
                    response.setIsApproved(schedule.getIsApproved());
                    response.setRequestedBy(schedule.getRequestedBy().getEmail());

                    Users approvedBy = schedule.getApprovedBy();
                    if(approvedBy != null) {
                        response.setApprovedBy(approvedBy.getEmail());
                    }

                    return response;
                })
                .collect(Collectors.toList());

        GetVisitRequestResident200Response response = new GetVisitRequestResident200Response();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }
}
