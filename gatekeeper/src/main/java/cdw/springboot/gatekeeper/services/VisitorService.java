package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.GetVisitRequestResident200Response;

import java.time.LocalDate;

public interface VisitorService {
    public GetVisitRequestResident200Response getVisitDetails(LocalDate date, String email);
}
