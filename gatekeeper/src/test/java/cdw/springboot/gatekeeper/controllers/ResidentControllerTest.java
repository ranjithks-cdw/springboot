package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.services.ResidentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResidentControllerTest {
    @InjectMocks
    private ResidentController residentController;

    @Mock
    private ResidentServiceImpl residentService;

    @Test
    public void testCreateVisitRequest() {
        VisitRequest request = new VisitRequest();
        GeneralSuccess scheduleResponseSuccess = new GeneralSuccess();
        when(residentService.createVisitRequest(request)).thenReturn(scheduleResponseSuccess);
        ResponseEntity response = residentController.createVisitRequest(request);
        assertEquals(scheduleResponseSuccess, response.getBody());
    }

    @Test
    public void testModifyVisitRequest() {
        Integer requestId = 1;
        VisitRequest request = new VisitRequest();
        GeneralSuccess updateSuccessResponse = new GeneralSuccess();
        when(residentService.modifyVisitRequest(requestId, request)).thenReturn(updateSuccessResponse);
        ResponseEntity response = residentController.modifyVisitRequest(requestId, request);
        assertEquals(updateSuccessResponse, response.getBody());
    }

    @Test
    public void testRemoveVisitRequest() {
        Integer userId = 1;
        GeneralSuccess deleteResponse = new GeneralSuccess();
        when(residentService.removeVisitRequest(userId)).thenReturn(deleteResponse);
        ResponseEntity response = residentController.removeVisitRequest(userId);
        assertEquals(deleteResponse, response.getBody());
    }

    @Test
    public void testGetVisitReqById() {
        Integer requestId = 1;
        GetVisitorReqByIdResident200Response successResponse = new GetVisitorReqByIdResident200Response();
        when(residentService.getVisitorReqByIdResident(requestId)).thenReturn(successResponse);
        ResponseEntity response = residentController.getVisitorReqByIdResident(requestId);
        assertEquals(successResponse, response.getBody());
    }

    @Test
    public void testGetVisitRequests() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("2023-11-16", formatter);
        GetVisitRequestResident200Response scheduleList = new GetVisitRequestResident200Response();
        when(residentService.getVisitRequestResident(date)).thenReturn(scheduleList);
        ResponseEntity response = residentController.getVisitRequestResident(date);
        assertEquals(scheduleList, response.getBody());
    }
}
