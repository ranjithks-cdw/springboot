package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.SigninRequest;
import cdw.springboot.gatekeeper.model.UserRegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public GeneralSuccess registerUser(UserRegistrationRequest userRegistrationRequest);
    public GeneralSuccess userLogin(SigninRequest login);
    public GeneralSuccess userLogout(HttpServletRequest request);
}
