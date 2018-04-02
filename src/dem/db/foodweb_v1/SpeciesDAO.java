package dem.db.foodweb_v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dem.db.GameDB;
import dem.model.foodweb.Animal;
import dem.model.foodweb.Plant;
import dem.model.foodweb.Species;
import shared.util.Log;


/**
 * This retrieves particular plant or animal species from the "species" table 
 * of the WoB database.  However, it only gets a very limited subset of the 
 * attributes available for a given species in the database.
 * 
 * 
 * @author Cheryl Nielsen
 * for CSC 831 Multiplayer Game Development, San Francisco State University, Spring 2018
 */
public class SpeciesDAO 
{
	
	public SpeciesDAO() {
	}
	
	
	public static Species getSpeciesByName(String name) 
    {
    	String sqlQuery = "" + "SELECT * FROM `species` WHERE `name` = " + name;
    	Species species; 
		return species = getSpecies(sqlQuery);
    }
	
	
	public static Species getSpeciesByID(int species_id) 
    {
    	String sqlQuery = "" + "SELECT * FROM `species` WHERE `species_id` = " + species_id;
    	Species species; 
		return species = getSpecies(sqlQuery);
    }
	
	
	/**
	 * private for safety of database, to prevent SQL injection attacks
	 * 
	 * @param sqlQuery the SQL code for the database query
	 * @return the particular species that was requested by the query
	 */
	private static Species getSpecies(String sqlQuery) 
    {
    	String query = sqlQuery;

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;        
        Species species = null;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);
            
            rs = pstmt.executeQuery();

            while (rs.next()) {
            	species = null;

                switch (rs.getInt("organism_type")) {
                    case 0:
                    	species = new Animal(rs.getInt("species_id"));
                        break;
                    case 1:
                    	species = new Plant(rs.getInt("species_id"));
                        break;
                }

                if (species == null) {
                    continue;
                }

                species.setName(rs.getString("name"));
                species.setBiomassCost(rs.getInt("cost"));
                species.setDescription(rs.getString("description"));
                species.setModel_id(rs.getInt("model_id"));

            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return species;
    }
	
	
	/**
	 * 
	 * @return the number of species currently in the database
	 */
	public static int getNumberOfSpecies() 
	{
		String query = "" + "SELECT COUNT(*) FROM `species`";
		int numberOfSpecies = 0;
		
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;  

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();

            if(rs.next()) 
            {
            	
            }

        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return numberOfSpecies;
	}
	
	
	public static ArrayList<Integer> getSpeciesIDList()
	{
		ArrayList<Integer> species = new ArrayList<Integer>();
		int species_id;
    	String query = "" + "SELECT `species_id` FROM `species`";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
            	species_id = rs.getInt("species_id");
                species.add(species_id);
            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return species;
	}
	
	
	/**
	 * 
	 * @return an array list of all the species objects available from the database
	 */
	public static ArrayList<Species> getListOfSpecies() 
    {
    	ArrayList<Species> species = new ArrayList<Species>();

    	String query = "" + "SELECT * FROM `species`";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);

            rs = pstmt.executeQuery();

            while (rs.next()) {
            	Species item = null;

                switch (rs.getInt("organism_type")) {
                    case 0:
                    	item = new Animal(rs.getInt("species_id"));
                        break;
                    case 1:
                    	item = new Plant(rs.getInt("species_id"));
                        break;
                }

                if (item == null) {
                    continue;
                }

                item.setName(rs.getString("name"));
                item.setBiomassCost(rs.getInt("cost"));
                item.setDescription(rs.getString("description"));
                item.setModel_id(rs.getInt("model_id"));

                species.add(item);
            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return species;
    }

	
}
