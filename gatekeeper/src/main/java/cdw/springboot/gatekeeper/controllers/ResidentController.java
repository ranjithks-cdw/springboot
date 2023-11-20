package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.ResidentApi;
import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.GetVisitRequestResident200Response;
import cdw.springboot.gatekeeper.model.GetVisitorReqByIdResident200Response;
import cdw.springboot.gatekeeper.model.VisitRequest;
import cdw.springboot.gatekeeper.services.ResidentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Controller to manage resident endpoints
 */
@RestController
public class ResidentController implements ResidentApi {
    @Autowired
    ResidentServiceImpl residentService;
    /**
     * @param visitRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> createVisitRequest(VisitRequest visitRequest) {
        return ResponseEntity.status(201).body(residentService.createVisitRequest(visitRequest));
    }

    /**
     * @param date (required)
     * @return
     */
    @Override
    public ResponseEntity<GetVisitRequestResident200Response> getVisitRequestResident(LocalDate date) {
        return ResponseEntity.status(200).body(residentService.getVisitRequestResident(date));
    }

    /**
     * @param requestId (required)
     * @return
     */
    @Override
    public ResponseEntity<GetVisitorReqByIdResident200Response> getVisitorReqByIdResident(Integer requestId) {
        return ResponseEntity.status(200).body(residentService.getVisitorReqByIdResident(requestId));
    }

    /**
     * @param requestId    (required)
     * @param visitRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> modifyVisitRequest(Integer requestId, VisitRequest visitRequest) {
        return ResponseEntity.status(200).body(residentService.modifyVisitRequest(requestId, visitRequest));
    }

    /**
     * @param requestId (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> removeVisitRequest(Integer requestId) {
        return ResponseEntity.status(200).body(residentService.removeVisitRequest(requestId));
    }
}
