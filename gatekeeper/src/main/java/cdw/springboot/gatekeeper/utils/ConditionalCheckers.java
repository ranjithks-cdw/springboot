package cdw.springboot.gatekeeper.utils;

import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.entities.UserInfo;
import cdw.springboot.gatekeeper.entities.Users;

import static cdw.springboot.gatekeeper.constants.AppConstants.SYSTEM_ADMIN_IDS;

public class ConditionalCheckers {

    public static boolean isSystemAdmin(Integer id) {
        return SYSTEM_ADMIN_IDS.contains(id);
    }

    public static boolean isUnApprovedUserRequest(UserInfo userInfo) {
        return (userInfo != null &&
                (userInfo.getUser().getIsApproved() == null ||
                userInfo.getUser().getIsApproved().equals(AppConstants.NO))
        );
    }

    public static boolean isActiveApprovedUser(UserInfo userInfo) {
        return (userInfo.getUser().getIsApproved() != null &&
                userInfo.getUser().getIsApproved().equals(AppConstants.YES) &&
                userInfo.getUser().getIsActive() != null &&
                userInfo.getUser().getIsActive().equals(AppConstants.YES)
        );
    }

    public static boolean isActiveApprovedUser(Users user) {
        return (user.getIsApproved() != null &&
                user.getIsApproved().equals(AppConstants.YES) &&
                user.getIsActive() != null &&
                user.getIsActive().equals(AppConstants.YES)
        );
    }
}
