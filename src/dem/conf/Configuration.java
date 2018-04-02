package dem.conf;

/**
 * Created by Jens on 17/10/16.
 * Modified by Cheryl Nielsen 4/2/2018 for local PC database info and new DEM port number.
 */
public class Configuration {
    public static int lobbyPortNumber = 9255;
    public static int SeaDividedPortNumber = 9258;
    public static int cowportNumber = 9260;
    public static int COSPortNumber =  9254;
    public static int RRPortNumber = 9253;
    public static int DemPortNumber = 9300;

    public static String DBURL = "localhost";
    //public static String DBURL = "thecity.sfsu.edu";
    public static String DBName =  "csc63101";
    public static String DBUsername = "csc63101";
    public static String DBPassword = "csc631_01";

    /*
    public static String DBName =  "wob";
    public static String DBUsername = "wob";
    public static String DBPassword = "wob";
    */
}
