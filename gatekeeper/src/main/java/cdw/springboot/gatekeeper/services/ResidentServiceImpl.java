package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.ErrorResponseConstants;
import cdw.springboot.gatekeeper.constants.SuccessResponseConstants;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.entities.VisitRequests;
import cdw.springboot.gatekeeper.entities.Visitors;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.DeleteSuccess;
import cdw.springboot.gatekeeper.model.ScheduleResponseSuccess;
import cdw.springboot.gatekeeper.model.UpdateSuccess;
import cdw.springboot.gatekeeper.model.VisitRequest;
import cdw.springboot.gatekeeper.repositories.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class ResidentServiceImpl implements ResidentService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    VisitorsRepository visitorsRepository;
    @Autowired
    VisitRequestsRepository visitRequestsRepository;
    @Autowired
    BlacklistRepository blacklistRepository;
    @Autowired
    JwtServiceImpl jwtService;
    /**
     * @param requestId
     * @param updateVisitRequest
     * @return
     */
    @Override
    public UpdateSuccess modifyVisitRequest(Integer requestId, VisitRequest updateVisitRequest) {
        UpdateSuccess response = null;
        try {
            VisitRequests visitRequestObj = visitRequestsRepository.findValidRequestById(requestId).orElse(null);
            Visitors visitor = visitRequestObj.getVisitor();
            boolean isVisitorUpdated = false;
            if(visitRequestObj == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if(updateVisitRequest.getVisitorName() != null) {
                visitor.setVisitorName(updateVisitRequest.getVisitorName());
                isVisitorUpdated = true;
            }
            if(updateVisitRequest.getEmail() != null) {
                visitor.setEmail(updateVisitRequest.getEmail());
                isVisitorUpdated = true;
            }
            if(updateVisitRequest.getAge() != null) {
                visitor.setAge(updateVisitRequest.getAge());
                isVisitorUpdated = true;
            }
            if(updateVisitRequest.getAddress() != null) {
                visitor.setAddress(updateVisitRequest.getAddress());
                isVisitorUpdated = true;
            }
            if(updateVisitRequest.getGender() != null) {
                visitor.setGender(updateVisitRequest.getGender());
                isVisitorUpdated = true;
            }
            if(updateVisitRequest.getMobileNumber() != null) {
                visitor.setMobileNumber(updateVisitRequest.getMobileNumber());
                isVisitorUpdated = true;
            }
            if(updateVisitRequest.getRequestedDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(updateVisitRequest.getRequestedDate(), formatter);
                visitRequestObj.setDate(date);
            }
            if(isVisitorUpdated) {
                visitRequestObj.setVisitor(visitor);
                visitorsRepository.save(visitor);
            }
            visitRequestsRepository.save(visitRequestObj);
            response = new UpdateSuccess();
            response.setMessage(SuccessResponseConstants.SUCCESS_UPDATE);
        } catch (TransactionSystemException ex) {
            if (ex.getRootCause() instanceof ConstraintViolationException) {
                throw new ConstraintViolationException(((ConstraintViolationException) ex.getRootCause()).getConstraintViolations());
            }
            throw new GatekeeperException(ex.getMessage());
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
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
    public DeleteSuccess removeVisitRequest(Integer requestId) {
        DeleteSuccess response = null;
        try {
            int deletedRows = visitRequestsRepository.deleteValidRequestById(requestId);
            if(deletedRows != 1) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            response = new DeleteSuccess();
            response.setMessage(SuccessResponseConstants.SUCCESS_DELETE_RESPONSE);
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }

    /**
     * @param visitRequest
     * @return
     */
    @Override
    public ScheduleResponseSuccess createVisitRequest(VisitRequest visitRequest) {
        ScheduleResponseSuccess response = null;
        try {
            if(blacklistRepository.existsByEmail(visitRequest.getEmail()) > 0) {
                throw new GatekeeperException(ErrorResponseConstants.BLACKLISTED_USER, HttpStatus.UNAUTHORIZED);
            }
            String requesterEmail = jwtService.getUserFromJwt();
            Users requestedBy = userRepository.findByEmail(requesterEmail).orElse(null);
            if(requestedBy == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
            }
            Visitors visitor = visitorsRepository.findByEmail(visitRequest.getEmail()).orElse(null);
            if(visitor == null) {
                visitor = new Visitors();
                visitor.setVisitorName(visitRequest.getVisitorName());
                visitor.setEmail(visitRequest.getEmail());
                visitor.setAge(visitRequest.getAge());
                visitor.setAddress(visitRequest.getAddress());
                visitor.setGender(visitRequest.getGender());
                visitor.setMobileNumber(visitRequest.getMobileNumber());
                visitorsRepository.save(visitor);
            }
            VisitRequests visitRequestObj = new VisitRequests();
            visitRequestObj.setRequestedBy(requestedBy);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(visitRequest.getRequestedDate(), formatter);

            visitRequestObj.setDate(date);
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            String passkey = String.format("%06d", number);
            visitRequestObj.setPasskey(passkey);
            visitRequestObj.setVisitor(visitor);
            visitRequestsRepository.save(visitRequestObj);
            response = new ScheduleResponseSuccess();
            response.setMessage(SuccessResponseConstants.SUCCESS_VISIT_REQUEST);
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
