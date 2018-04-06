package conf;

/**
 * Created by Jens on 17/10/16.
 */
public class Configuration {
    public static int lobbyPortNumber = 9255;
    public static int SeaDividedPortNumber = 9258;
    public static int cowportNumber = 9260;
    public static int COSPortNumber =  9254;
    public static int RRPortNumber = 9253;

    //Credentials to connect to database hosted on thecity.sfsu.edu
    //public static String DBURL = "thecity.sfsu.edu";
    //public static String DBName =  "csc63101";
    //public static String DBUsername = "csc63101";
    //public static String DBPassword = "csc631_01";

    //Credentials to connect to database hosted on Azure VM created in Spring 2018
    public static String DBURL = "worldofbalance.westus.cloudapp.azure.com"; //same as 13.93.192.67
    public static String DBName =  "csc63101";
    public static String DBUsername = "wob_user";
    public static String DBPassword = "Wob_spring2018";

}
