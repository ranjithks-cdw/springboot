package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.GetVisitRequestResident200Response;
import cdw.springboot.gatekeeper.model.GetVisitorReqByIdResident200Response;
import cdw.springboot.gatekeeper.model.VisitRequest;

import java.time.LocalDate;

public interface ResidentService {
    public GeneralSuccess createVisitRequest(VisitRequest visitRequest);
    public GetVisitRequestResident200Response getVisitRequestResident(LocalDate date);
    public GetVisitorReqByIdResident200Response getVisitorReqByIdResident(Integer requestId);
    public GeneralSuccess modifyVisitRequest(Integer requestId, VisitRequest updateVisitRequest);
    public GeneralSuccess removeVisitRequest(Integer requestId);
}
