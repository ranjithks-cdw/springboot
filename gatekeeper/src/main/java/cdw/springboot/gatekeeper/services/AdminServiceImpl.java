package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.entities.Roles;
import cdw.springboot.gatekeeper.entities.UserInfo;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.repositories.RolesRepository;
import cdw.springboot.gatekeeper.repositories.UserInfoRepository;
import cdw.springboot.gatekeeper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cdw.springboot.gatekeeper.utils.ConditionalCheckers.*;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    JwtServiceImpl jwtService;

    /**
     * @param userId
     * @return
     */
    @Override
    public GeneralSuccess deleteUserById(Integer userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND));

        if(isSystemAdmin(userId) || !isActiveApprovedUser(user)) {
            throw new GatekeeperException(AppConstants.ERROR_BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        user.setIsActive(AppConstants.NO);
        userRepository.save(user);

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(AppConstants.SUCCESS_DELETED);

        return response;
    }

    /**
     * @param requestId
     * @return
     */
    @Override
    public GetUserById200Response getRegistrationReqById(Integer requestId) {
        UserInfo userInfo = userInfoRepository.findByUserUserId(requestId).orElse(null);

        if(!isUnApprovedUserRequest(userInfo)) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        UsersList data = new UsersList();
        data.setUserId(requestId);
        data.setUserName(userInfo.getUser().getUsername());
        data.setEmail(userInfo.getUser().getEmail());
        data.setAge(userInfo.getAge());
        data.setAddress(userInfo.getAddress());
        data.setGender(userInfo.getGender());
        data.setMobileNumber(userInfo.getMobileNumber());
        data.setUserRole(userInfo.getUser().getRolesList().get(0).getRoleName());
        data.setIsApproved(userInfo.getUser().getIsApproved());

        Users approvedBy = userInfo.getApprovedBy();
        if(approvedBy != null) {
            data.approvedBy(userInfo.getApprovedBy().getEmail());
        }

        GetUserById200Response response = new GetUserById200Response();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }

    /**
     * @return
     */
    @Override
    public GetUsers200Response getRegistrationRequests() {
        List<UserInfo> allUsers = userInfoRepository.findAll();

        List<UsersList> data = allUsers.stream()
                .filter(user -> isUnApprovedUserRequest(user))
                .map(user -> {
                    UsersList userDetail = new UsersList();
                    userDetail.setUserId(user.getUser().getUserId());
                    userDetail.setUserName(user.getUser().getUsername());
                    userDetail.setEmail(user.getUser().getEmail());
                    userDetail.setAge(user.getAge());
                    userDetail.setAddress(user.getAddress());
                    userDetail.setGender(user.getGender());
                    userDetail.setMobileNumber(user.getMobileNumber());
                    userDetail.setUserRole(user.getUser().getRolesList().get(0).getRoleName());
                    userDetail.setIsApproved(user.getUser().getIsApproved());

                    Users approvedBy = user.getApprovedBy();
                    if(approvedBy != null) {
                        userDetail.approvedBy(user.getApprovedBy().getEmail());
                    }

                    return userDetail;
                })
                .collect(Collectors.toList());


        GetUsers200Response response = new GetUsers200Response();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public GetUserById200Response getUserById(Integer userId) {
        UserInfo userInfo = userInfoRepository.findByUserUserId(userId).orElse(null);

        if(!isActiveApprovedUser(userInfo) || isSystemAdmin(userId)) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        UsersList data = new UsersList();
        data.setUserId(userId);
        data.setUserName(userInfo.getUser().getUsername());
        data.setEmail(userInfo.getUser().getEmail());
        data.setAge(userInfo.getAge());
        data.setAddress(userInfo.getAddress());
        data.setGender(userInfo.getGender());
        data.setMobileNumber(userInfo.getMobileNumber());
        data.setUserRole(userInfo.getUser().getRolesList().get(0).getRoleName());
        data.setIsApproved(userInfo.getUser().getIsApproved());

        Users approvedBy = userInfo.getApprovedBy();
        if(approvedBy != null) {
            data.approvedBy(userInfo.getApprovedBy().getEmail());
        }

        GetUserById200Response response = new GetUserById200Response();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }

    /**
     * @return
     */
    @Override
    public GetUsers200Response getUsers() {
        List<UserInfo> allUsers = userInfoRepository.findAll();

        List<UsersList> data = allUsers.stream()
                .filter(user -> isActiveApprovedUser(user) && !isSystemAdmin(user.getUser().getUserId()))
                .map(user -> {
                    UsersList userDetail = new UsersList();
                    userDetail.setUserId(user.getUser().getUserId());
                    userDetail.setUserName(user.getUser().getUsername());
                    userDetail.setEmail(user.getUser().getEmail());
                    userDetail.setAge(user.getAge());
                    userDetail.setAddress(user.getAddress());
                    userDetail.setGender(user.getGender());
                    userDetail.setMobileNumber(user.getMobileNumber());
                    userDetail.setUserRole(user.getUser().getRolesList().get(0).getRoleName());
                    userDetail.setIsApproved(user.getUser().getIsApproved());

                    Users approvedBy = user.getApprovedBy();
                    if(approvedBy != null) {
                        userDetail.approvedBy(user.getApprovedBy().getEmail());
                    }

                    return userDetail;
                })
                .collect(Collectors.toList());

        GetUsers200Response response = new GetUsers200Response();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }

    /**
     * @param requestId
     * @param manageRequest
     * @return
     */
    @Override
    public GeneralSuccess manageRegistrationRequest(Integer requestId, ManageRegistrationRequest manageRequest) {
        String approverEmail = jwtService.getUserFromJwt();
        Users approvedBy = userRepository.findByEmail(approverEmail).orElse(null);

        if(approvedBy == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        UserInfo userInfo = userInfoRepository.findByUserUserId(requestId).orElse(null);

        if(!isUnApprovedUserRequest(userInfo)) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());

        userInfo.setApprovedBy(approvedBy);
        if(manageRequest.getIsApproved()) {
            userInfo.getUser().setIsApproved(AppConstants.YES);
            userInfo.getUser().setIsActive(AppConstants.YES);
            response.setData(AppConstants.SUCCESS_APPROVED);
        } else {
            userInfo.getUser().setIsApproved(AppConstants.NO);
            userInfo.getUser().setIsActive(AppConstants.NO);
            response.setData(AppConstants.SUCCESS_REJECTED);
        }

        userInfoRepository.save(userInfo);

        return response;
    }

    /**
     * @param userId
     * @param updateUserRequest
     * @return
     */
    @Override
    public GeneralSuccess updateUserById(Integer userId, UpdateUserRequest updateUserRequest) {
        UserInfo userInfo = userInfoRepository.findByUserUserId(userId).orElse(null);

        if(!isActiveApprovedUser(userInfo) && !isSystemAdmin(userId)) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        userInfo.getUser().setUserName(updateUserRequest.getUserName());
        userInfo.getUser().setEmail(updateUserRequest.getEmail());
        userInfo.setAge(updateUserRequest.getAge());
        userInfo.setAddress(updateUserRequest.getAddress());
        userInfo.setGender(updateUserRequest.getGender());
        userInfo.setMobileNumber(updateUserRequest.getMobileNumber());

        Roles role = rolesRepository.findByRoleName(updateUserRequest.getUserRole());
        if(role == null) {
            throw new GatekeeperException(AppConstants.ERROR_BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
        List<Roles> rolesList = new ArrayList<>();
        rolesList.add(role);

        userInfo.getUser().setRolesList(rolesList);

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(AppConstants.SUCCESS_UPDATED);

        return response;
    }
}
