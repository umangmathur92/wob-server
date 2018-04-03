package dem.db;

// Java Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Other Imports
import dem.core.ServerResources;
import dem.model.Species;
import dem.model.SpeciesGroup;
import dem.util.Vector3;
import dem.model.SpeciesType;
import dem.util.Log;

/**
 * Table(s) Required: eco_species
 * 
 * @author Gary
 */
public final class EcoSpeciesDAO {

    private EcoSpeciesDAO() {
    }

    public static int createSpecies(int eco_id, int species_id, int biomass) {
        int group_id = -1;

        String query = "INSERT INTO `eco_species` (`eco_id`, `species_id`, `biomass`) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, eco_id);
            pstmt.setInt(2, species_id);
            pstmt.setInt(3, biomass);
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                group_id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return group_id;
    }

    
    
    public static List<Species> getSpecies(int eco_id) {
        List<Species> speciesList = new ArrayList<Species>();

        String query = "SELECT * FROM `eco_species` WHERE `eco_id` = ? ORDER BY `species_id`";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, eco_id);

            rs = pstmt.executeQuery();

            int prev_id = -1;

            Species species = null;

            while (rs.next()) {
                int species_id = rs.getInt("species_id");

                if (species_id != prev_id) {
                    SpeciesType speciesType = ServerResources.getSpeciesTable().getSpecies(species_id);

                    if (speciesType != null) {
                        species = new Species(species_id, speciesType);
                        speciesList.add(species);
                    } else {
                        continue;
                    }
                }

                int group_id = rs.getInt("group_id"), biomass = rs.getInt("biomass");
                species.add(new SpeciesGroup(species, group_id, biomass, new Vector3<Integer>(rs.getInt("pos_x"), rs.getInt("pos_y"), rs.getInt("pos_z"))));           
            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt, rs);
        }

        return speciesList;
    }
    
 
    public static boolean removeSpecies(int group_id) {
        boolean status = false;

        String query = "DELETE FROM `eco_species` WHERE `group_id` = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = GameDB.getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, group_id);

            status = pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        } finally {
            GameDB.closeConnection(con, pstmt);
        }

        return status;
    }

 
}
