package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.services.GatekeeperServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GatekeeperControllerTest {
    @InjectMocks
    private GatekeeperController gatekeeperController;

    @Mock
    private GatekeeperServiceImpl gatekeeperService;

    @Test
    public void testGetVisitReqById() {
        Integer requestId = 1;
        GetVisitorReqByIdResident200Response successResponse = new GetVisitorReqByIdResident200Response();
        when(gatekeeperService.getVisitReqByIdGatekeeper(requestId)).thenReturn(successResponse);
        ResponseEntity response = gatekeeperController.getVisitReqByIdGatekeeper(requestId);
        assertEquals(successResponse, response.getBody());
    }

    @Test
    public void testGetVisitRequests() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("2023-11-16", formatter);
        GetVisitRequestResident200Response scheduleList = new GetVisitRequestResident200Response();
        when(gatekeeperService.getVisitRequestGatekeeper(date)).thenReturn(scheduleList);
        ResponseEntity response = gatekeeperController.getVisitRequestGatekeeper(date);
        assertEquals(scheduleList, response.getBody());
    }

    @Test
    public void testApproveVisitRequest() {
        Integer requestId = 1;
        ManageRegistrationRequest manageRegistrationRequest = new ManageRegistrationRequest();
        manageRegistrationRequest.setIsApproved(true);
        GeneralSuccess successResponse = new GeneralSuccess();
        successResponse.setSuccess(true);
        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setData(AppConstants.SUCCESS_APPROVED);
        when(gatekeeperService.manageVisitRequest(requestId, manageRegistrationRequest)).thenReturn(successResponse);
        ResponseEntity response = gatekeeperController.manageVisitRequest(requestId, manageRegistrationRequest);
        assertEquals(successResponse, response.getBody());
    }

    @Test
    public void testRejectVisitRequest() {
        Integer requestId = 1;
        ManageRegistrationRequest manageRegistrationRequest = new ManageRegistrationRequest();
        manageRegistrationRequest.setIsApproved(false);
        GeneralSuccess successResponse = new GeneralSuccess();
        successResponse.setSuccess(true);
        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setData(AppConstants.SUCCESS_REJECTED);
        when(gatekeeperService.manageVisitRequest(requestId, manageRegistrationRequest)).thenReturn(successResponse);
        ResponseEntity response = gatekeeperController.manageVisitRequest(requestId, manageRegistrationRequest);
        assertEquals(successResponse, response.getBody());
    }
}
