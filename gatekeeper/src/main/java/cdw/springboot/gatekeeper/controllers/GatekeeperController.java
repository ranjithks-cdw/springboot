package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.GatekeeperApi;
import cdw.springboot.gatekeeper.model.ManageRegistrationRequest;
import cdw.springboot.gatekeeper.model.ManageRegistrationResponse;
import cdw.springboot.gatekeeper.model.ScheduleResponse;
import cdw.springboot.gatekeeper.services.GatekeeperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GatekeeperController implements GatekeeperApi {
    @Autowired
    GatekeeperServiceImpl gatekeeperService;
    /**
     * @param date (required)
     * @return
     */
    @Override
    public ResponseEntity<List<ScheduleResponse>> getVisitRequests(LocalDate date) {
        return ResponseEntity.status(200).body(gatekeeperService.getVisitRequests(date));
    }

    /**
     * @param requestId (required)
     * @return
     */
    @Override
    public ResponseEntity<ScheduleResponse> getVisitorReqById(Integer requestId) {
        return ResponseEntity.status(200).body(gatekeeperService.getVisitorReqById(requestId));
    }

    /**
     * @param requestId                 (required)
     * @param manageRegistrationRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<ManageRegistrationResponse> manageVisitRequest(Integer requestId, ManageRegistrationRequest manageRegistrationRequest) {
        return ResponseEntity.status(200).body(gatekeeperService.manageVisitRequest(requestId, manageRegistrationRequest));
    }
}
