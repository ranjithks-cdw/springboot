package cdw.springboot.gatekeeper.controllers;

import cdw.springboot.gatekeeper.api.AdminApi;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.services.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController implements AdminApi {
    @Autowired
    AdminServiceImpl adminService;
    /**
     * @param blacklistRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<BlacklistSuccess> blacklistUser(BlacklistRequest blacklistRequest) {
        return ResponseEntity.status(201).body(adminService.blacklistUser(blacklistRequest));
    }

    /**
     * @param userId (required)
     * @return
     */
    @Override
    public ResponseEntity<DeleteSuccess> deleteUserById(Integer userId) {
        return ResponseEntity.status(204).body(adminService.deleteUserById(userId));
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<List<BlackList>> getBlacklist() {
        return ResponseEntity.status(200).body(adminService.getBlackList());
    }

    /**
     * @param requestId (required)
     * @return
     */
    @Override
    public ResponseEntity<UserById> getRegistrationReqById(Integer requestId) {
        return ResponseEntity.status(200).body(adminService.getRegistrationReqById(requestId));
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<List<UsersList>> getRegistrationRequests() {
        return ResponseEntity.status(200).body(adminService.getRegistrationRequests());
    }

    /**
     * @param userId (required)
     * @return
     */
    @Override
    public ResponseEntity<UserById> getUserById(Integer userId) {
        return ResponseEntity.status(200).body(adminService.getUserById(userId));
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<List<UsersList>> getUsers() {
        return ResponseEntity.status(200).body(adminService.getUsers());
    }

    /**
     * @param requestId                 (required)
     * @param manageRegistrationRequest (required)
     * @return
     */
    @Override
    public ResponseEntity<ManageRegistrationResponse> manageRegistrationRequest(Integer requestId, ManageRegistrationRequest manageRegistrationRequest) {
        return ResponseEntity.status(200).body(adminService.manageRegistrationRequest(requestId, manageRegistrationRequest));
    }

    /**
     * @param userId     (required)
     * @param updateUser (required)
     * @return
     */
    @Override
    public ResponseEntity<UpdateSuccess> updateUserById(Integer userId, UpdateUser updateUser) {
        return ResponseEntity.status(200).body(adminService.updateUserById(userId, updateUser));
    }
}