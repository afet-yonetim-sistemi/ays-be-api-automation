package endpoints;

public class Routes {
    public static String base_url="http://ec2-18-159-211-214.eu-central-1.compute.amazonaws.com";
    //AYS APIS AUTH ADMIN
    public static String authAdminRegister=base_url+"/api/v1/authentication/admin/register";
    public static String authAdminToken=base_url+"/api/v1/authentication/admin/token";
    public static String authAdminTokenRefresh=base_url+"/api/v1/authentication/admin/token/refresh";
    public static String authAdminTokenInvalidate=base_url+"/api/v1/authentication/admin/token/invalidate";

    //AYS APIS AUTH USER
    public static String authUserToken=base_url+"/api/v1/authentication/token";
    public static String authUserTokenRefresh=base_url+"/api/v1/authentication/token/refresh";
    public static String authUserTokenInvalidate=base_url+"/api/v1/authentication/token/invalidate";

    //AYS APIS ADMIN
    public static String postAdmins=base_url+"/api/v1/admins";
    public static String postUsers=base_url+"/api/v1/users";
    public static String postCreateUser=base_url+"/api/v1/user";
    public static String getUser=base_url+"/api/v1/user/{id}";
    public static String putUpdateUser=base_url+"/api/v1/user/{id}";
    public static String deleteUser=base_url+"/api/v1/user/{id}";

    //AYS APIS USER (not ready yet)
    public static String putUpdateUserStatus=base_url+"/api/v1/user-self/status/support";
    public static String postUserLocation=base_url+"/api/v1/user/location";

}
