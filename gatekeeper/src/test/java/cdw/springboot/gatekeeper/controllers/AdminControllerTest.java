package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.services.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminServiceImpl adminService;

//    @Test
//    public void testBlacklistUser() {
//        BlacklistRequest blacklistRequest = new BlacklistRequest(1, "visitor");
//        BlacklistSuccess blacklistResponse = new BlacklistSuccess();
//        blacklistResponse.setMessage(SuccessResponseConstants.SUCCESS_BLACKLIST);
//        when(adminService.blacklistUser(blacklistRequest)).thenReturn(blacklistResponse);
//        ResponseEntity response = adminController.blacklistUser(blacklistRequest);
//        assertEquals(201, response.getStatusCode());
//        assertEquals(SuccessResponseConstants.SUCCESS_BLACKLIST, response.getBody());
//    }

    @Test
    public void testDeleteUserById() {
        Integer userId = 1;
        GeneralSuccess successResponse = new GeneralSuccess();
        successResponse.setSuccess(true);
        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setData(AppConstants.SUCCESS_DELETED);
        when(adminService.deleteUserById(userId)).thenReturn(successResponse);
        ResponseEntity response = adminController.deleteUserById(userId);
        assertEquals(successResponse, response.getBody());
    }

//    @Test
//    public void testGetBlacklist() {
//        List<BlackList> blackList = new ArrayList<>();
//        when(adminService.getBlackList()).thenReturn(blackList);
//        ResponseEntity response = adminController.getBlacklist();
//        assertEquals(200, response.getStatusCode());
//        assertEquals(blackList, response.getBody());
//    }

    @Test
    public void testGetRegistrationReqById() {
        Integer requestId = 1;
        GetUserById200Response successResponse = new GetUserById200Response();
        when(adminService.getRegistrationReqById(requestId)).thenReturn(successResponse);
        ResponseEntity response = adminController.getRegistrationReqById(requestId);
        assertEquals(successResponse, response.getBody());
    }

    @Test
    public void testGetRegistrationRequests() {
        GetUsers200Response usersList = new GetUsers200Response();
        when(adminService.getRegistrationRequests()).thenReturn(usersList);
        ResponseEntity response = adminController.getRegistrationRequests();
        assertEquals(usersList, response.getBody());
    }

    @Test
    public void testGetUserById() {
        Integer userId = 1;
        GetUserById200Response userById = new GetUserById200Response();
        when(adminService.getUserById(userId)).thenReturn(userById);
        ResponseEntity response = adminController.getUserById(userId);
        assertEquals(userById, response.getBody());
    }

    @Test
    public void testGetUsers() {
        GetUsers200Response usersList = new GetUsers200Response();
        when(adminService.getUsers()).thenReturn(usersList);
        ResponseEntity response = adminController.getUsers();
        assertEquals(usersList, response.getBody());
    }

    @Test
    public void testApproveRegistrationRequest() {
        Integer requestId = 1;
        ManageRegistrationRequest manageRegistrationRequest = new ManageRegistrationRequest();
        manageRegistrationRequest.setIsApproved(true);
        GeneralSuccess successResponse = new GeneralSuccess();
        successResponse.setSuccess(true);
        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setData(AppConstants.SUCCESS_APPROVED);
        when(adminService.manageRegistrationRequest(requestId, manageRegistrationRequest)).thenReturn(successResponse);
        ResponseEntity response = adminController.manageRegistrationRequest(requestId, manageRegistrationRequest);
        assertEquals(successResponse, response.getBody());
    }

    @Test
    public void testRejectRegistrationRequest() {
        Integer requestId = 1;
        ManageRegistrationRequest manageRegistrationRequest = new ManageRegistrationRequest();
        manageRegistrationRequest.setIsApproved(false);
        GeneralSuccess successResponse = new GeneralSuccess();
        successResponse.setSuccess(true);
        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setData(AppConstants.SUCCESS_REJECTED);
        when(adminService.manageRegistrationRequest(requestId, manageRegistrationRequest)).thenReturn(successResponse);
        ResponseEntity response = adminController.manageRegistrationRequest(requestId, manageRegistrationRequest);
        assertEquals(successResponse, response.getBody());
    }

    @Test
    public void testUpdateUserById() {
        Integer userId = 1;
        UpdateUserRequest request = new UpdateUserRequest();
        GeneralSuccess successResponse = new GeneralSuccess();
        successResponse.setSuccess(true);
        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setData(AppConstants.SUCCESS_UPDATED);
        when(adminService.updateUserById(userId, request)).thenReturn(successResponse);
        ResponseEntity response = adminController.updateUserById(userId, request);
        assertEquals(successResponse, response.getBody());
    }
}
