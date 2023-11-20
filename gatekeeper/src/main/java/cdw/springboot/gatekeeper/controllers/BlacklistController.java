package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.BlacklistApi;
import cdw.springboot.gatekeeper.model.BlacklistRequest;
import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.GetBlacklist200Response;
import cdw.springboot.gatekeeper.services.BlacklistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * To manage Blacklist endpoints of the app
 */
@RestController
public class BlacklistController implements BlacklistApi {
    @Autowired
    BlacklistServiceImpl blacklistService;
    /**
     * To black list a user
     * @param blacklistRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> blacklistUser(BlacklistRequest blacklistRequest) {
        return ResponseEntity.status(201).body(blacklistService.blacklistUser(blacklistRequest));
    }

    /**
     * Get all blacklisted users
     * @return
     */
    @Override
    public ResponseEntity<GetBlacklist200Response> getBlacklist() {
        return ResponseEntity.status(200).body(blacklistService.getBlacklist());
    }
}
