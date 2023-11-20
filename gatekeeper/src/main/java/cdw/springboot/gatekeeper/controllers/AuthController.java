package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.AuthApi;
import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.SigninRequest;
import cdw.springboot.gatekeeper.model.UserRegistrationRequest;
import cdw.springboot.gatekeeper.services.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authorizations
 */
@RestController
public class AuthController implements AuthApi {
    @Autowired
    AuthServiceImpl authService;
    @Autowired
    HttpServletRequest request;
    /**
     * To request registration of user to the app
     * @param userRegistrationRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> registerUser(UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.status(201).body(authService.registerUser(userRegistrationRequest));
    }

    /**
     * To logging into the app
     * @param signinRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> userLogin(SigninRequest signinRequest) {
        return ResponseEntity.status(200).body(authService.userLogin(signinRequest));
    }

    /**
     * Logout from the app
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> userLogout() {
        return ResponseEntity.status(200).body(authService.userLogout(request));
    }
}
