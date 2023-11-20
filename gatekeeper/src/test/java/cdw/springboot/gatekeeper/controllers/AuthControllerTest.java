package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.SigninRequest;
import cdw.springboot.gatekeeper.model.UserRegistrationRequest;
import cdw.springboot.gatekeeper.services.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthServiceImpl authService;

    @Test
    public void testUserRegistration() throws Exception {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        GeneralSuccess successResponse = new GeneralSuccess();
        when(authService.registerUser(registrationRequest)).thenReturn(successResponse);
        ResponseEntity response = authController.registerUser(registrationRequest);
        assertEquals(successResponse,response.getBody());
    }

    @Test
    public void testUserLogin() {
        SigninRequest request = new SigninRequest();
        GeneralSuccess loggingResponse = new GeneralSuccess();
        when(authService.userLogin(request)).thenReturn(loggingResponse);
        GeneralSuccess response = authController.userLogin(request).getBody();
        assertEquals(loggingResponse,response);
    }
}
