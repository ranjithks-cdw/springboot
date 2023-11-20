package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.model.GetVisitRequestResident200Response;
import cdw.springboot.gatekeeper.model.ScheduleResponse;
import cdw.springboot.gatekeeper.services.VisitorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VisitorControllerTest {
    @InjectMocks
    private VisitorController visitorController;

    @Mock
    private VisitorServiceImpl visitorService;
    @Test
    public void testGetVisitDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("2023-11-16", formatter);
        GetVisitRequestResident200Response successResponse = new GetVisitRequestResident200Response();
        when(visitorService.getVisitDetails(date, "ram@gmail.com")).thenReturn(successResponse);
        ResponseEntity response = visitorController.getVisitDetails(date, "ram@gmail.com");
        assertEquals(successResponse, response.getBody());
    }
}
