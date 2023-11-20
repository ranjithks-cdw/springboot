package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.model.BlacklistRequest;
import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.GetBlacklist200Response;
import cdw.springboot.gatekeeper.services.BlacklistServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlacklistControllerTest {
    @InjectMocks
    private BlacklistController blacklistController;

    @Mock
    private BlacklistServiceImpl blacklistService;

    @Test
    public void testBlacklistUser() {
        BlacklistRequest request = new BlacklistRequest();
        GeneralSuccess successResponse = new GeneralSuccess();
        when(blacklistService.blacklistUser(request)).thenReturn(successResponse);
        ResponseEntity response = blacklistController.blacklistUser(request);
        assertEquals(successResponse, response.getBody());
    }

    @Test
    public void testGetBlacklist() {
        GetBlacklist200Response successResponse = new GetBlacklist200Response();
        when(blacklistService.getBlacklist()).thenReturn(successResponse);
        ResponseEntity response = blacklistController.getBlacklist();
        assertEquals(successResponse, response.getBody());
    }
}
