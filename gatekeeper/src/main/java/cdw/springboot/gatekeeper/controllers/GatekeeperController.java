package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.GatekeeperApi;
import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.GetVisitRequestResident200Response;
import cdw.springboot.gatekeeper.model.GetVisitorReqByIdResident200Response;
import cdw.springboot.gatekeeper.model.ManageRegistrationRequest;
import cdw.springboot.gatekeeper.services.GatekeeperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Controller to manage gatekeeper endpoints
 */
@RestController
public class GatekeeperController implements GatekeeperApi {
    @Autowired
    GatekeeperServiceImpl gatekeeperService;
    /**
     * @param requestId (required)
     * @return
     */
    @Override
    public ResponseEntity<GetVisitorReqByIdResident200Response> getVisitReqByIdGatekeeper(Integer requestId) {
        return ResponseEntity.status(200).body(gatekeeperService.getVisitReqByIdGatekeeper(requestId));
    }

    /**
     * @param date (required)
     * @return
     */
    @Override
    public ResponseEntity<GetVisitRequestResident200Response> getVisitRequestGatekeeper(LocalDate date) {
        return ResponseEntity.status(200).body(gatekeeperService.getVisitRequestGatekeeper(date));
    }

    /**
     * @param requestId                 (required)
     * @param manageRegistrationRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> manageVisitRequest(Integer requestId, ManageRegistrationRequest manageRegistrationRequest) {
        return ResponseEntity.status(200).body(gatekeeperService.manageVisitRequest(requestId, manageRegistrationRequest));
    }
}
