package dem.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dem.model.Animal;
import dem.model.Plant;
import dem.model.Species;
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
	
	
	// private for safety of database, to prevent SQL injection attacks
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
	
	
}
