package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.entities.VisitRequests;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.repositories.UserRepository;
import cdw.springboot.gatekeeper.repositories.VisitRequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatekeeperServiceImpl implements GatekeeperService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    VisitRequestsRepository visitRequestsRepository;

    @Autowired
    JwtServiceImpl jwtService;

    /**
     * @param requestId
     * @return
     */
    @Override
    public GetVisitorReqByIdResident200Response getVisitReqByIdGatekeeper(Integer requestId) {
        VisitRequests visitRequest = visitRequestsRepository.findById(requestId).orElse(null);

        if(visitRequest == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        ScheduleResponse data = new ScheduleResponse();
        data.setRequestId(visitRequest.getRequestId());
        data.setVisitorId(visitRequest.getVisitor().getVisitorId());
        data.setVisitorName(visitRequest.getVisitor().getVisitorName());
        data.setRequestedDate(visitRequest.getDate());
        data.setMobileNumber(visitRequest.getVisitor().getMobileNumber());
        data.setGender(visitRequest.getVisitor().getGender());
        data.setEmail(visitRequest.getVisitor().getEmail());
        data.setPasskey(visitRequest.getPasskey());
        data.setAddress(visitRequest.getVisitor().getAddress());
        data.setIsApproved(visitRequest.getIsApproved());
        data.setRequestedBy(visitRequest.getRequestedBy().getEmail());

        Users approvedBy = visitRequest.getApprovedBy();
        if(approvedBy != null) {
            data.setApprovedBy(approvedBy.getEmail());
        }

        GetVisitorReqByIdResident200Response response = new GetVisitorReqByIdResident200Response();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }

    /**
     * @param date
     * @return
     */
    @Override
    public GetVisitRequestResident200Response getVisitRequestGatekeeper(LocalDate date) {
        List<VisitRequests> allVisitRequests = visitRequestsRepository.findAllByDate(date);

        List<ScheduleResponse> data = allVisitRequests.stream()
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

    /**
     * @param requestId
     * @param manageRequest
     * @return
     */
    @Override
    public GeneralSuccess manageVisitRequest(Integer requestId, ManageRegistrationRequest manageRequest) {
        String approverEmail = jwtService.getUserFromJwt();
        Users approvedBy = userRepository.findByEmail(approverEmail).orElse(null);

        if(approvedBy == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        VisitRequests visitRequest = visitRequestsRepository.findById(requestId).orElse(null);

        if(visitRequest == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(visitRequest.getIsApproved() != null || visitRequest.getDate().isBefore(LocalDate.now())) {
            throw new GatekeeperException(AppConstants.ERROR_BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());

        if(manageRequest.getIsApproved()) {
            visitRequest.setIsApproved(AppConstants.YES);
            visitRequest.setEntryTime(LocalDateTime.now());
            response.setData(AppConstants.SUCCESS_APPROVED);
        } else {
            visitRequest.setIsApproved(AppConstants.NO);
            response.setData(AppConstants.SUCCESS_REJECTED);
        }
        visitRequest.setApprovedBy(approvedBy);
        visitRequestsRepository.save(visitRequest);

        return response;
    }
}
