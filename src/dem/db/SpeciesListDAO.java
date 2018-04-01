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
 * This retrieves the full listing of all plant and animal species from the "species" table 
 * of the WoB database.  However, it only gets a very limited subset of the attributes available 
 * for a given species in the database.
 * 
 * 
 * @author Cheryl Nielsen
 * for CSC 831 Multiplayer Game Development, San Francisco State University, Spring 2018
 */
public final class SpeciesListDAO {

    public SpeciesListDAO() {
    }

    
    public static ArrayList<Species> getSpecies() 
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
