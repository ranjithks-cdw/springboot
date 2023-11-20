package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.VisitorApi;
import cdw.springboot.gatekeeper.model.GetVisitRequestResident200Response;
import cdw.springboot.gatekeeper.services.VisitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Controller to manage visitor endpoints
 */
@RestController
public class VisitorController implements VisitorApi {
    @Autowired
    VisitorServiceImpl visitorService;
    /**
     * @param date  (required)
     * @param email (required)
     * @return
     */
    @Override
    public ResponseEntity<GetVisitRequestResident200Response> getVisitDetails(LocalDate date, String email) {
        return ResponseEntity.status(200).body(visitorService.getVisitDetails(date,email));
    }
}
