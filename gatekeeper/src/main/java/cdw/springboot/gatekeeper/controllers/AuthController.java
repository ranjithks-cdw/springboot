package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.AuthApi;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.services.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    @Autowired
    AuthServiceImpl authServiceImpl;
    @Autowired
    HttpServletRequest httpServletRequest;
    /**
     * @param userRequestRegistration (required)
     * @return
     */
    @Override
    public ResponseEntity<RequestRegistrationSuccess> registerUser(@RequestBody UserRequestRegistration userRequestRegistration) {
        return ResponseEntity.status(201).body(authServiceImpl.registerUser(userRequestRegistration));
    }

    /**
     * @param login (required)
     * @return
     */
    @Override
    public ResponseEntity<LoginSuccess> userLogin(@RequestBody Login login) {
        return ResponseEntity.status(200).body(authServiceImpl.userLogin(login));
    }


    /**
     * @return
     */
    @Override
    public ResponseEntity<LogoutSuccess> userLogout() {
        return ResponseEntity.status(200).body(authServiceImpl.userLogout(httpServletRequest));
    }
}
