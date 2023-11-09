package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.constants.ErrorResponseConstants;
import cdw.springboot.gatekeeper.constants.SuccessResponseConstants;
import cdw.springboot.gatekeeper.entities.Blacklist;
import cdw.springboot.gatekeeper.entities.UserInfo;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.repositories.BlacklistRepository;
import cdw.springboot.gatekeeper.repositories.RolesRepository;
import cdw.springboot.gatekeeper.repositories.UserInfoRepository;
import cdw.springboot.gatekeeper.repositories.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private JwtServiceImpl jwtService;

    /**
     * @param blacklistRequest
     * @return
     */
    @Override
    public BlacklistSuccess blacklistUser(BlacklistRequest blacklistRequest) {
        BlacklistSuccess response = null;
        try {
            response = new BlacklistSuccess();
            String approverEmail = jwtService.getUserFromJwt();
            Users approvedBy = userRepository.findByEmail(approverEmail).orElse(null);
            if(approvedBy == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
            }
            int addedRows = 0;
            if(blacklistRequest.getUserRole().equals(AppConstants.ROLE_VISITOR)) {
                addedRows = blacklistRepository.blacklistVisitor(blacklistRequest.getUserId(), approvedBy.getUserId());
            } else if(approvedBy.getRolesList().get(0).getRoleName().equals(AppConstants.ROLE_ADMIN)) {
                addedRows = blacklistRepository.blacklistUser(blacklistRequest.getUserId(), approvedBy.getUserId());
            }
            if(addedRows != 1) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            response.setMessage(SuccessResponseConstants.SUCCESS_BLACKLIST);
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(ErrorResponseConstants.BAD_REQUEST);
        }
        return response;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public DeleteSuccess deleteUserById(Integer userId) {
        DeleteSuccess response = null;
        try {
            response = new DeleteSuccess();
            int deletedRows = userRepository.deleteActiveUserById(userId);
            if(deletedRows != 1) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            response.setMessage(SuccessResponseConstants.SUCCESS_DELETE_RESPONSE);
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(response.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    /**
     * @return
     */
    @Override
    public List<BlackList> getBlackList() {
        List<BlackList> blackList = null;
        try {
            List<Blacklist> blackListedUsers = blacklistRepository.findAll();
            blackList = blackListedUsers.stream()
                    .map(user -> {
                        BlackList b = new BlackList();
                        if(user.getUser() != null) {
                            b.setUserName(user.getUser().getUsername());
                            b.setEmail(user.getUser().getEmail());
                            b.setUserId(user.getUser().getUserId());
                            b.setUserRole(user.getUser().getRolesList().get(0).getRoleName());
                        }
                        if(user.getVisitor() != null) {
                            b.setUserName(user.getVisitor().getVisitorName());
                            b.setEmail(user.getVisitor().getEmail());
                            b.setUserId(user.getVisitor().getVisitorId());
                            b.setUserRole(AppConstants.ROLE_VISITOR);
                        }
                        b.setBlacklistedBy(user.getBlacklistedBy().getUsername());
                        return b;
                    })
                    .collect(Collectors.toList());
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return blackList;
    }

    /**
     * @param requestId
     * @return
     */
    @Override
    public UserById getRegistrationReqById(Integer requestId) {
        UserById response = null;
        try {
            Optional<UserInfo> userInfo = userInfoRepository.findByUserUserId(requestId);
            if(!userInfo.isPresent() || (userInfo.get().getUser().getIsApproved() != null && userInfo.get().getUser().getIsApproved().equals(AppConstants.YES))) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            response = new UserById();
            response.setUserId(userInfo.get().getUser().getUserId());
            response.setUserName(userInfo.get().getUser().getUsername());
            response.setEmail(userInfo.get().getUser().getEmail());
            response.setAge(userInfo.get().getAge());
            response.setAddress(userInfo.get().getAddress());
            response.setUserRole(userInfo.get().getUser().getRolesList().get(0).getRoleName());
            response.setGender(userInfo.get().getGender());
            response.setMobileNumber(userInfo.get().getMobileNumber());
            response.setIsApproved(userInfo.get().getUser().getIsApproved());
            String approvedBy = null;
            if(userInfo.get().getApprovedBy() != null) {
                approvedBy = userInfo.get().getApprovedBy().getUsername();
            }
            response.setApprovedBy(approvedBy);
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }

    /**
     * @return
     */
    @Override
    public List<UsersList> getRegistrationRequests() {
        List<UsersList> registrationRequests = null;
        try {
            List<UserInfo> userInfo = userInfoRepository.findAll();
            registrationRequests = userInfo.stream()
                    .filter(user -> user.getUser().getIsApproved() == null || user.getUser().getIsApproved().equals(AppConstants.NO))
                    .map(user -> {
                        UsersList usersListResponse = new UsersList();
                        usersListResponse.setUserId(user.getUser().getUserId());
                        usersListResponse.setUserName(user.getUser().getUsername());
                        usersListResponse.setEmail(user.getUser().getEmail());
                        usersListResponse.setAge(user.getAge());
                        usersListResponse.setAddress(user.getAddress());
                        usersListResponse.setUserRole(user.getUser().getRolesList().get(0).getRoleName());
                        usersListResponse.setMobileNumber(user.getMobileNumber());
                        usersListResponse.setGender(user.getGender());
                        usersListResponse.setIsApproved(user.getUser().getIsApproved());
                        String approvedBy = null;
                        if(user.getApprovedBy() != null) {
                            approvedBy = user.getApprovedBy().getUsername();
                        }
                        usersListResponse.setApprovedBy(approvedBy);
                        return usersListResponse;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return registrationRequests;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public UserById getUserById(Integer userId) {
        UserById response = null;
        try {
            UserInfo userInfo = userInfoRepository.findValidUserById(userId).orElse(null);
            if(userInfo == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            response = new UserById();
            response.setUserId(userInfo.getUser().getUserId());
            response.setUserName(userInfo.getUser().getUsername());
            response.setEmail(userInfo.getUser().getEmail());
            response.setAge(userInfo.getAge());
            response.setAddress(userInfo.getAddress());
            response.setUserRole(userInfo.getUser().getRolesList().get(0).getRoleName());
            response.setGender(userInfo.getGender());
            response.setMobileNumber(userInfo.getMobileNumber());
            response.setIsApproved(userInfo.getUser().getIsApproved());
            String approvedBy = null;
            if(response.getApprovedBy() != null) {
                approvedBy = userInfo.getApprovedBy().getUsername();
            }
            response.setApprovedBy(approvedBy);
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }

    /**
     * @return
     */
    @Override
    public List<UsersList> getUsers() {
        List<UsersList> response = null;
        try {
            List<UserInfo> userInfo = userInfoRepository.findAllValidUsers();
            response = userInfo.stream()
                    .map(user -> {
                        UsersList details = new UsersList();
                        details.setUserId(user.getUser().getUserId());
                        details.setApprovedBy(user.getApprovedBy() == null ? null: user.getApprovedBy().getEmail());
                        details.setUserRole(user.getUser().getRolesList().get(0).getRoleName());
                        details.setGender(user.getGender());
                        details.setAge(user.getAge());
                        details.setUserName(user.getUser().getUsername());
                        details.setMobileNumber(user.getMobileNumber());
                        details.setEmail(user.getUser().getEmail());
                        details.setAddress(user.getAddress());
                        details.setIsApproved(user.getUser().getIsApproved());
                        return details;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }

    /**
     * @param requestId
     * @param manageRegistrationRequest
     * @return
     */
    @Override
    @Transactional
    public ManageRegistrationResponse manageRegistrationRequest(Integer requestId, ManageRegistrationRequest manageRegistrationRequest) {
        ManageRegistrationResponse response = null;
        try {
            response = new ManageRegistrationResponse();
            String approverEmail = jwtService.getUserFromJwt();
            Users approvedBy = userRepository.findByEmail(approverEmail).orElse(null);
            if(approvedBy == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
            }

            int updatedRows = 0;
            if(manageRegistrationRequest.getCanApprove()) {
                updatedRows += userInfoRepository.saveApproval(requestId, approvedBy.getUserId());
                updatedRows += userRepository.saveApproval(requestId, AppConstants.YES);
                response.setMessage(SuccessResponseConstants.SUCCESS_REG_REQ_APPROVED);
            } else {
                updatedRows += userRepository.saveApproval(requestId, AppConstants.NO);
                updatedRows++;
                response.setMessage(SuccessResponseConstants.SUCCESS_REG_REQ_REJECTED);
            }

            if(updatedRows != 2) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }

        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }

    /**
     * @param userId
     * @param updateUser
     * @return
     */
    @Transactional
    @Override
    public UpdateSuccess updateUserById(Integer userId, UpdateUser updateUser) {
        UpdateSuccess response = null;
        try {
            UserInfo user = userInfoRepository.findValidUserById(userId).orElse(null);
            response = new UpdateSuccess();
            if(user == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if(updateUser.getUserName() != null) {
                user.getUser().setUserName(updateUser.getUserName());
            }
            if(updateUser.getEmail() != null) {
                user.getUser().setEmail(updateUser.getEmail());
            }
            if(updateUser.getAge() != null) {
                user.setAge(updateUser.getAge());
            }
            if(updateUser.getAddress() != null) {
                user.setAddress(updateUser.getAddress());
            }
            if(updateUser.getGender() != null) {
                user.setGender(updateUser.getGender());
            }
            if(updateUser.getMobileNumber() != null) {
                user.setMobileNumber(updateUser.getMobileNumber());
            }
            if(updateUser.getUserRole() != null) {
                user.getUser().getRolesList().remove(0);
                user.getUser().getRolesList().add(rolesRepository.findByRoleName(updateUser.getUserRole()));
            }
            userInfoRepository.save(user);
            response.setMessage(SuccessResponseConstants.SUCCESS_UPDATE);
        } catch (TransactionSystemException ex) {
            if (ex.getRootCause() instanceof ConstraintViolationException) {
                throw new ConstraintViolationException(((ConstraintViolationException) ex.getRootCause()).getConstraintViolations());
            }
            throw new GatekeeperException(ex.getMessage());
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }
}
