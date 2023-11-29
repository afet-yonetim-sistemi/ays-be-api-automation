package utility;

public class HowToUseConfigurationFile {
    public static void main(String[] args) {
        /** Configuration Reader needs to be updated up to the configuration file path */
        String secretKey=ConfigurationReader.getProperty("secret_key");
        String password=ConfigurationReader.getProperty("your_pass");
        System.out.println(secretKey);
        System.out.println(password);
    }
}


