package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.*;

import java.util.List;

public interface AdminService {
    public BlacklistSuccess blacklistUser(BlacklistRequest blacklistRequest);
    public DeleteSuccess deleteUserById(Integer userId);
    public List<BlackList> getBlackList();
    public UserById getRegistrationReqById(Integer requestId);
    public List<UsersList> getRegistrationRequests();
    public UserById getUserById(Integer userId);
    public List<UsersList> getUsers();
    public ManageRegistrationResponse manageRegistrationRequest(Integer requestId, ManageRegistrationRequest manageRegistrationRequest);
    public UpdateSuccess updateUserById(Integer userId, UpdateUser updateUser);
}
