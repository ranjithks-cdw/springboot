package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.*;

public interface AdminService {
    public GeneralSuccess deleteUserById(Integer userId);
    public GetUserById200Response getRegistrationReqById(Integer requestId);
    public GetUsers200Response getRegistrationRequests();
    public GetUserById200Response getUserById(Integer userId);
    public GetUsers200Response getUsers();
    public GeneralSuccess manageRegistrationRequest(Integer requestId, ManageRegistrationRequest manageRequest);
    public GeneralSuccess updateUserById(Integer userId, UpdateUserRequest updateUserRequest);
}
