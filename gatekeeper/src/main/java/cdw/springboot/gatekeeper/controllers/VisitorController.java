package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.VisitorApi;
import cdw.springboot.gatekeeper.model.ScheduleResponse;
import cdw.springboot.gatekeeper.services.VisitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity<List<ScheduleResponse>> getPasskey(LocalDate date, String email) {
        return ResponseEntity.status(200).body(visitorService.getPasskey(date, email));
    }
}
