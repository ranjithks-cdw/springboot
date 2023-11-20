package cdw.springboot.gatekeeper.constants;

import java.util.Set;

public class AppConstants {
//    Paths
    public static final String PATH_CONTROLLERS = "execution(* cdw.springTraining.gatekeeper.controllers.*.*(..))";
    public static final String PATH_SERVICES = "execution(* cdw.springTraining.gatekeeper.services.*.*(..))";
    public static final String PATH_REPOSITORIES = "execution(* cdw.springTraining.gatekeeper.repositories.*.*(..))";

//    Date Formats
    public static final String FORMAT_DATE_TIME = "dd-MMM-yyyy hh:mm:ss aa";
    public static final String FORMAT_DATE = "yyyy-MM-dd";

//    Endpoint Patterns
    public static final String ENDPOINT_REGISTER = "/register";
    public static final String ENDPOINT_SIGNIN = "/signin";
    public static final String ENDPOINT_SIGNOUT = "/signout";
    public static final String ENDPOINT_ADMIN = "/admin/**";
    public static final String ENDPOINT_RESIDENT = "/resident/**";
    public static final String ENDPOINT_GATEKEEPER = "/gatekeeper/**";
    public static final String ENDPOINT_VISITOR = "/visitor/**";
    public static final String ENDPOINT_BLACKLIST = "/blacklist";

//    Roles
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_RESIDENT = "resident";
    public static final String ROLE_GATEKEEPER = "gatekeeper";
    public static final String ROLE_VISITOR = "visitor";

//    Error Responses
    public static final String ERROR_BAD_REQUEST = "Bad Request";
    public static final String ERROR_USER_EXISTS_ALREADY = "User Exists Already";
    public static final String ERROR_BLACKLISTED_USER = "User Blacklisted";
    public static final String ERROR_INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String ERROR_NOT_FOUND = "Resource Not Found";
    public static final String ERROR_NOT_APPROVED = "Registration request not approved";
    public static final String ERROR_NOT_AUTHORIZED = "User not authorized";

//    Success Responses
    public static final String SUCCESS_CREATED = "Successfully Created";
    public static final String SUCCESS_LOGOUT = "Successfully Signed Out";
    public static final String SUCCESS_DELETED = "Successfully Deleted";
    public static final String SUCCESS_APPROVED = "Request Approved";
    public static final String SUCCESS_REJECTED = "Request Rejected";
    public static final String SUCCESS_UPDATED = "Successfully Updated";
    public static final String SUCCESS_BLACKLIST = "Successfully Blacklisted";

//  Other Constants
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String INVALID_EMAIL = "Invalid Email";
    public static final int USER_TOKEN_EXPIRY_TIME = 30*60*1000;

    public static final Set<Integer> SYSTEM_ADMIN_IDS = Set.of(1);
}
