package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.constants.ErrorResponseConstants;
import cdw.springboot.gatekeeper.entities.VisitRequests;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
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
    VisitRequestsRepository visitRequestsRepository;

    @Autowired
    BlacklistRepository blacklistRepository;

    /**
     * @param date
     * @param email
     * @return
     */
    @Override
    public List<ScheduleResponse> getPasskey(LocalDate date, String email) {
        List<ScheduleResponse> response = null;
        try {
            if(blacklistRepository.existsByEmail(email) > 0) {
                throw new GatekeeperException(ErrorResponseConstants.BLACKLISTED_USER, HttpStatus.FORBIDDEN);
            }
            List<VisitRequests> visitRequests = visitRequestsRepository.findAllByDate(date).orElse(null);
            response = visitRequests.stream()
                    .filter(req -> req.getVisitor().getEmail().equals(email))
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
}
