package endpoints;

import utility.ConfigurationReader;

public class Routes {
    public static String base_url = ConfigurationReader.getProperty("base_url");
    //AYS APIS AUTH ADMIN
    public static String authAdminRegister = base_url + "/api/v1/authentication/admin/register";
    public static String authAdminToken = base_url + "/api/v1/authentication/admin/token";
    public static String authAdminTokenRefresh = base_url + "/api/v1/authentication/admin/token/refresh";
    public static String authAdminTokenInvalidate = base_url + "/api/v1/authentication/admin/token/invalidate";

    //AYS APIS AUTH USER
    public static String authUserToken = base_url + "/api/v1/authentication/token";
    public static String authUserTokenRefresh = base_url + "/api/v1/authentication/token/refresh";
    public static String authUserTokenInvalidate = base_url + "/api/v1/authentication/token/invalidate";

    //AYS APIS ADMIN
    public static String postAdmins = base_url + "/api/v1/admins";
    public static String postUsers = base_url + "/api/v1/users";
    public static String postCreateUser = base_url + "/api/v1/user";
    public static String getUser = base_url + "/api/v1/user/{id}";
    public static String putUpdateUser = base_url + "/api/v1/user/{id}";
    public static String deleteUser = base_url + "/api/v1/user/{id}";

    //AYS APIS USER
    public static String putUpdateUserSupportStatus = base_url + "/api/v1/user-self/status/support";
    public static String postUserLocation = base_url + "/api/v1/user/location";
    public static String postAssignmentSearch = base_url + "/api/v1/assignment/search";
    public static String postAssignmentApprove = base_url + "/api/v1/assignment/approve";
    public static String postAssignmentReject = base_url + "/api/v1/assignment/reject";
    public static String postAssignmentStart = base_url + "/api/v1/assignment/start";
    public static String postAssignmentComplete = base_url + "/api/v1/assignment/complete";
    public static String getUserSelfInfo = base_url + "/api/v1/user-self";
    public static String getAssignmentUser = base_url + "/api/v1/assignment";
    public static String getAssignmentSummaryUser = base_url + "/api/v1/assignment/summary";
    public static String postAssignmentCancel = base_url + "/api/v1/assignment/cancel";

    //AYS APIS INSTITUTION
    public static String postListAssignments = base_url + "/api/v1/assignments";
    public static String getAssignment = base_url + "/api/v1/assignment/{id}";
    public static String postCreateAssignment = base_url + "/api/v1/assignment";
    public static String putUpdateAssignment = base_url + "/api/v1/assignment/{id}";
    public static String deleteAssignment = base_url + "/api/v1/assignment/{id}";

    //AYS APIS SUPER_ADMIN ARMS
    public static String postRegistrationApplications = base_url + "/api/v1/admin/registration-applications";
    public static String getRegistrationApplicationsWithId = base_url+ "/api/v1/admin/registration-application/{id}";
    public static String getGetRegistrationApplicationsIdSummary = base_url + "/api/v1/admin/registration-application/{id}/summary";
    public static String postRegistrationApplicationApprove=base_url+"/api/v1/admin/registration-application/{id}/approve";
    public static String postRegistrationApplication=base_url+"/api/v1/admin/registration-application";
    public static String postRegistrationApplicationReject=base_url+"/api/v1/admin/registration-application/{id}/reject";



}
