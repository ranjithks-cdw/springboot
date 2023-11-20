package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.GetVisitRequestResident200Response;
import cdw.springboot.gatekeeper.model.GetVisitorReqByIdResident200Response;
import cdw.springboot.gatekeeper.model.ManageRegistrationRequest;

import java.time.LocalDate;

public interface GatekeeperService {
    public GetVisitorReqByIdResident200Response getVisitReqByIdGatekeeper(Integer requestId);
    public GetVisitRequestResident200Response getVisitRequestGatekeeper(LocalDate date);
    public GeneralSuccess manageVisitRequest(Integer requestId, ManageRegistrationRequest manageRequest);
}
