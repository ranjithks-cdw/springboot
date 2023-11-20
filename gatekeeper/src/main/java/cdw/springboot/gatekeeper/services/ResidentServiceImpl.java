package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.entities.VisitRequests;
import cdw.springboot.gatekeeper.entities.Visitors;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.repositories.BlacklistRepository;
import cdw.springboot.gatekeeper.repositories.UserRepository;
import cdw.springboot.gatekeeper.repositories.VisitRequestsRepository;
import cdw.springboot.gatekeeper.repositories.VisitorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static cdw.springboot.gatekeeper.utils.AppUtils.generateRandomPasskey;
import static cdw.springboot.gatekeeper.utils.DateUtilities.stringToLocaleDate;

@Service
@Transactional
public class ResidentServiceImpl implements ResidentService {
    @Autowired
    VisitorsRepository visitorsRepository;
    @Autowired
    VisitRequestsRepository visitRequestsRepository;
    @Autowired
    BlacklistRepository blacklistRepository;
    @Autowired
    JwtServiceImpl jwtService;
    @Autowired
    UserRepository userRepository;
    /**
     * @param visitRequest
     * @return
     */
    @Override
    public GeneralSuccess createVisitRequest(VisitRequest visitRequest) {
        Visitors visitor = visitorsRepository.findByEmail(visitRequest.getEmail()).orElse(null);

        if(blacklistRepository.existsByVisitor(visitor)) {
            throw new GatekeeperException(AppConstants.ERROR_BLACKLISTED_USER, HttpStatus.BAD_REQUEST);
        }

        String requesterEmail = jwtService.getUserFromJwt();
        Users requestedBy = userRepository.findByEmail(requesterEmail).orElse(null);

        if(requestedBy == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        LocalDate date = stringToLocaleDate(visitRequest.getRequestedDate());
        String passkey = generateRandomPasskey();

        if(visitor == null) {
            visitor = new Visitors();
            visitor.setVisitorName(visitRequest.getVisitorName());
            visitor.setEmail(visitRequest.getEmail());
            visitor.setAge(visitRequest.getAge());
            visitor.setAddress(visitRequest.getAddress());
            visitor.setGender(visitRequest.getGender());
            visitor.setMobileNumber(visitRequest.getMobileNumber());
        }
        VisitRequests visitRequestsObj = new VisitRequests();
        visitRequestsObj.setRequestedBy(requestedBy);
        visitRequestsObj.setDate(date);
        visitRequestsObj.setPasskey(passkey);
        visitRequestsObj.setVisitor(visitor);
        visitRequestsRepository.save(visitRequestsObj);

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setData(AppConstants.SUCCESS_CREATED);

        return response;
    }

    /**
     * @param date
     * @return
     */
    @Override
    public GetVisitRequestResident200Response getVisitRequestResident(LocalDate date) {
        String requesterEmail = jwtService.getUserFromJwt();
        Users requestedBy = userRepository.findByEmail(requesterEmail).orElse(null);

        if(requestedBy == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        List<VisitRequests> allVisitRequests = visitRequestsRepository.findAllByDate(date);

        List<ScheduleResponse> data = allVisitRequests.stream()
                .filter(schedule -> requestedBy.getUserId() == schedule.getRequestedBy().getUserId())
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
     * @return
     */
    @Override
    public GetVisitorReqByIdResident200Response getVisitorReqByIdResident(Integer requestId) {
        String requesterEmail = jwtService.getUserFromJwt();
        Users requestedBy = userRepository.findByEmail(requesterEmail).orElse(null);

        if(requestedBy == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        VisitRequests visitRequest = visitRequestsRepository.findById(requestId).orElse(null);

        if(visitRequest == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(visitRequest.getRequestedBy().getUserId() != requestedBy.getUserId()) {
            throw new GatekeeperException(AppConstants.ERROR_BAD_REQUEST, HttpStatus.BAD_REQUEST);
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
     * @param requestId
     * @param updateVisitRequest
     * @return
     */
    @Override
    public GeneralSuccess modifyVisitRequest(Integer requestId, VisitRequest updateVisitRequest) {
        String requesterEmail = jwtService.getUserFromJwt();
        Users requestedBy = userRepository.findByEmail(requesterEmail).orElse(null);

        if(requestedBy == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        VisitRequests visitRequest = visitRequestsRepository.findById(requestId).orElse(null);

        if(visitRequest == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(visitRequest.getRequestedBy().getUserId() != requestedBy.getUserId() || visitRequest.getIsApproved() != null || visitRequest.getDate().isBefore(LocalDate.now())) {
            throw new GatekeeperException(AppConstants.ERROR_BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
        LocalDate date = stringToLocaleDate(updateVisitRequest.getRequestedDate());


        visitRequest.getVisitor().setVisitorName(updateVisitRequest.getVisitorName());
        visitRequest.getVisitor().setAge(updateVisitRequest.getAge());
        visitRequest.getVisitor().setEmail(updateVisitRequest.getEmail());
        visitRequest.getVisitor().setAddress(updateVisitRequest.getAddress());
        visitRequest.getVisitor().setGender(updateVisitRequest.getGender());
        visitRequest.getVisitor().setMobileNumber(updateVisitRequest.getMobileNumber());
        visitRequest.setDate(date);

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(AppConstants.SUCCESS_UPDATED);

        return response;
    }

    /**
     * @param requestId
     * @return
     */
    @Override
    public GeneralSuccess removeVisitRequest(Integer requestId) {
        String requesterEmail = jwtService.getUserFromJwt();
        Users requestedBy = userRepository.findByEmail(requesterEmail).orElse(null);

        if(requestedBy == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        VisitRequests visitRequest = visitRequestsRepository.findById(requestId).orElse(null);

        if(visitRequest == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(visitRequest.getRequestedBy().getUserId() != requestedBy.getUserId() || visitRequest.getIsApproved() != null || visitRequest.getDate().isBefore(LocalDate.now())) {
            throw new GatekeeperException(AppConstants.ERROR_BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        visitRequestsRepository.deleteById(requestId);

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(AppConstants.SUCCESS_DELETED);

        return response;
    }
}
