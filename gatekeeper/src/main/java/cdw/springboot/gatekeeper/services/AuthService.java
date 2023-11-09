package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.*;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public RequestRegistrationSuccess registerUser(UserRequestRegistration userRegistrationRequest);
    public LoginSuccess userLogin(Login login);
    public LogoutSuccess userLogout(HttpServletRequest request);
}
