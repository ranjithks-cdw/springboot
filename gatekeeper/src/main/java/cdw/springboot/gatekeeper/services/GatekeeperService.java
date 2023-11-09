package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.ManageRegistrationRequest;
import cdw.springboot.gatekeeper.model.ManageRegistrationResponse;
import cdw.springboot.gatekeeper.model.ScheduleResponse;

import java.time.LocalDate;
import java.util.List;

public interface GatekeeperService {
    public List<ScheduleResponse> getVisitRequests(LocalDate date);
    public ScheduleResponse getVisitorReqById(Integer requestId);
    public ManageRegistrationResponse manageVisitRequest(Integer requestId, ManageRegistrationRequest manageRegistrationRequest);
}
