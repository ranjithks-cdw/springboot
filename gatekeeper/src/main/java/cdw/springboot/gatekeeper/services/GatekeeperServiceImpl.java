package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.constants.ErrorResponseConstants;
import cdw.springboot.gatekeeper.constants.SuccessResponseConstants;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.entities.VisitRequests;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.ManageRegistrationRequest;
import cdw.springboot.gatekeeper.model.ManageRegistrationResponse;
import cdw.springboot.gatekeeper.model.ScheduleResponse;
import cdw.springboot.gatekeeper.repositories.UserRepository;
import cdw.springboot.gatekeeper.repositories.VisitRequestsRepository;
import cdw.springboot.gatekeeper.repositories.VisitorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatekeeperServiceImpl implements GatekeeperService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    VisitorsRepository visitorsRepository;
    @Autowired
    VisitRequestsRepository visitRequestsRepository;
    @Autowired
    private JwtServiceImpl jwtService;
    /**
     * @param date
     * @return
     */
    @Override
    public List<ScheduleResponse> getVisitRequests(LocalDate date) {
        List<ScheduleResponse> response = null;
        try {
            List<VisitRequests> visitRequests = visitRequestsRepository.findAllByDate(date).orElse(null);
            response = visitRequests.stream()
                    .map(req -> {
                        ScheduleResponse message = new ScheduleResponse();
                        message.setRequestId(req.getRequestId());
                        message.setVisitorId(req.getVisitor().getVisitorId());
                        message.setVisitorName(req.getVisitor().getVisitorName());
                        message.setRequestedDate(req.getDate());
                        message.setMobileNumber(req.getVisitor().getMobileNumber());
                        message.setGender(req.getVisitor().getGender());
                        message.setEmail(req.getVisitor().getEmail());
                        message.setPasskey(req.getPasskey());
                        message.setAddress(req.getVisitor().getAddress());
                        message.setRequestedBy(req.getRequestedBy().getEmail());
                        return message;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }

    /**
     * @param requestId
     * @return
     */
    @Override
    public ScheduleResponse getVisitorReqById(Integer requestId) {
        ScheduleResponse response = null;
        try {
            VisitRequests visitRequests = visitRequestsRepository.findById(requestId).orElse(null);
            if(visitRequests == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            response = new ScheduleResponse();
            response.setRequestId(visitRequests.getRequestId());
            response.setVisitorId(visitRequests.getVisitor().getVisitorId());
            response.setVisitorName(visitRequests.getVisitor().getVisitorName());
            response.setRequestedDate(visitRequests.getDate());
            response.setMobileNumber(visitRequests.getVisitor().getMobileNumber());
            response.setGender(visitRequests.getVisitor().getGender());
            response.setEmail(visitRequests.getVisitor().getEmail());
            response.setPasskey(visitRequests.getPasskey());
            response.setAddress(visitRequests.getVisitor().getAddress());
            response.setRequestedBy(visitRequests.getRequestedBy().getEmail());
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }

    /**
     * @param requestId
     * @param manageRegistrationRequest
     * @return
     */
    @Override
    public ManageRegistrationResponse manageVisitRequest(Integer requestId, ManageRegistrationRequest manageRegistrationRequest) {
        ManageRegistrationResponse response = null;
        try {
            response = new ManageRegistrationResponse();

            String approverEmail = jwtService.getUserFromJwt();
            Users approvedBy = userRepository.findByEmail(approverEmail).orElse(null);
            if(approvedBy == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
            }

            int updatedRows = 0;
            if(manageRegistrationRequest.getCanApprove()) {
                updatedRows += visitRequestsRepository.saveApproval(requestId, AppConstants.YES, approvedBy.getUserId());
                response.setMessage(SuccessResponseConstants.SUCCESS_REG_REQ_APPROVED);
            } else {
                updatedRows += visitRequestsRepository.saveApproval(requestId, AppConstants.NO, approvedBy.getUserId());
                response.setMessage(SuccessResponseConstants.SUCCESS_REG_REQ_REJECTED);
            }

            if(updatedRows != 1) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }
}
