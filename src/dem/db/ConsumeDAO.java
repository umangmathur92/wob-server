package dem.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dem.model.Animal;
import dem.model.Plant;
import dem.model.Species;
import shared.util.Log;

/**
 * This retrieves the full set or a subset of the preditor and prey relationships 
 * from the "consume" table of the WoB database.  
 * 
 * @author Cheryl Nielsen
 * for CSC 831 Multiplayer Game Development, San Francisco State University, Spring 2018
 */
public class ConsumeDAO 
{
	
	public ConsumeDAO() {
	}
	
	
	/**
	 * Returns the list of species that are prey (food) for the given species.
	 * 
	 */
	public static ArrayList<Integer> getPreyList(int species_id) 
    {
    	String sqlQuery = "" + "SELECT * FROM `consume` WHERE `species_id` = " + species_id;
    	ArrayList<Integer> prey; 
		return prey = getListIDs(sqlQuery, true);
    }
	
	
	/**
	 * Returns the list of species that are predators (will eat) the given species.
	 * 
	 */
	public static ArrayList<Integer> getPredatorList(int species_id) 
    {
    	String sqlQuery = "" + "SELECT * FROM `consume` WHERE `prey_id` = " + species_id;
    	ArrayList<Integer> predators; 
		return predators = getListIDs(sqlQuery, false);
    }
	
	
	// private for safety of database, to prevent SQL injection attacks
	private static ArrayList<Integer> getListIDs(String sqlQuery, boolean getPrey) 
    {
		ArrayList<Integer> preyList = new ArrayList<Integer>();
    	String query = sqlQuery;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;        
        int species = -1;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);            
            rs = pstmt.executeQuery();

            while (rs.next()) 
            {            	
               if(getPrey)
               {
            	   species = rs.getInt("prey_id");
               }
               else // get the predators
               {
            	   species = rs.getInt("species_id");
               }
               
               preyList.add(species);
            }
            
            
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return preyList;
    }
	
}
