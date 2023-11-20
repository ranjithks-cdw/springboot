package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.AdminApi;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.services.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to manage admin endpoints
 */
@RestController
public class AdminController implements AdminApi {
    @Autowired
    AdminServiceImpl adminService;

    /**
     * Delete user by user id
     * @param userId (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> deleteUserById(Integer userId) {
        return ResponseEntity.status(200).body(adminService.deleteUserById(userId));
    }

    /**
     * Get unapproved user registration request by request id
     * @param requestId (required)
     * @return
     */
    @Override
    public ResponseEntity<GetUserById200Response> getRegistrationReqById(Integer requestId) {
        return ResponseEntity.status(200).body(adminService.getRegistrationReqById(requestId));
    }

    /**
     * Get all unapproved user registration requests
     * @return
     */
    @Override
    public ResponseEntity<GetUsers200Response> getRegistrationRequests() {
        return ResponseEntity.status(200).body(adminService.getRegistrationRequests());
    }

    /**
     * Get user details by id
     * @param userId (required)
     * @return
     */
    @Override
    public ResponseEntity<GetUserById200Response> getUserById(Integer userId) {
        return ResponseEntity.status(200).body(adminService.getUserById(userId));
    }

    /**
     * Get all valid users
     * @return
     */
    @Override
    public ResponseEntity<GetUsers200Response> getUsers() {
        return ResponseEntity.status(200).body(adminService.getUsers());
    }

    /**
     * To manage registration request (approve or reject) for user registration
     * @param requestId                 (required)
     * @param manageRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> manageRegistrationRequest(Integer requestId, ManageRegistrationRequest manageRequest) {
        return ResponseEntity.status(200).body(adminService.manageRegistrationRequest(requestId, manageRequest));
    }

    /**
     * To update user details by user id
     * @param userId            (required)
     * @param updateUserRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<GeneralSuccess> updateUserById(Integer userId, UpdateUserRequest updateUserRequest) {
        return ResponseEntity.status(200).body(adminService.updateUserById(userId, updateUserRequest));
    }
}