package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.ResidentApi;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.services.ResidentService;
import cdw.springboot.gatekeeper.services.ResidentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResidentController implements ResidentApi {
    @Autowired
    ResidentServiceImpl residentService;
    /**
     * @param visitRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<ScheduleResponseSuccess> createVisitRequest(VisitRequest visitRequest) {
        return ResponseEntity.status(201).body(residentService.createVisitRequest(visitRequest));
    }

    /**
     * @param requestId    (required)
     * @param visitRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<UpdateSuccess> modifyVisitRequest(Integer requestId, VisitRequest visitRequest) {
        return ResponseEntity.status(200).body(residentService.modifyVisitRequest(requestId, visitRequest));
    }

    /**
     * @param requestId (required)
     * @return
     */
    @Override
    public ResponseEntity<DeleteSuccess> removeVisitRequest(Integer requestId) {
        return ResponseEntity.status(204).body(residentService.removeVisitRequest(requestId));
    }
}
