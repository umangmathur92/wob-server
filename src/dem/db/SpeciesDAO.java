package dem.db;

// Java Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Other Imports
import shared.core.ServerResources;
import shared.model.AnimalType;
import shared.model.PlantType;
import shared.model.SpeciesType;
import shared.model.Consume;
import shared.simulation.simjob.SimTestNode;
import shared.util.Log;

/**
 * Table(s) Required: species, species_nodes, consume
 *
 * @author Gary
 */
public final class SpeciesDAO {

	private SpeciesDAO() {
	}

	public static List<SpeciesType> getSpecies() {
		List<SpeciesType> types = new ArrayList<SpeciesType>();

		String query = "" + "SELECT *, " + "GROUP_CONCAT(`node_id`, ':', `distribution`) AS node_list, "
				+ "(SELECT GROUP_CONCAT(`prey_id`) FROM `consume` WHERE `species_id` = s.`species_id`) AS prey_list, "
				+ "(SELECT GROUP_CONCAT(`species_id`) FROM `consume` WHERE `prey_id` = s.`species_id`) AS predator_list "
				+ "FROM `species` s " + "INNER JOIN `species_nodes` sn ON s.`species_id` = sn.`species_id` "
				+ "GROUP BY s.`species_id` " + "ORDER BY s.`species_id`, sn.`node_id`";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = GameDB.getConnection();
			pstmt = con.prepareStatement(query);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SpeciesType type = null;

				switch (rs.getInt("organism_type")) {
				case 0:
					type = new AnimalType(rs.getInt("species_id"));
					break;
				case 1:
					type = new PlantType(rs.getInt("species_id"));
					type.setCarryingCapacity(rs.getFloat("carrying_capacity"));
					break;
				}

				if (type == null) {
					continue;
				}

				type.setName(rs.getString("name"));
				type.setOrganismType(rs.getInt("organism_type"));
				type.setCost(rs.getInt("cost"));
				type.setDescription(rs.getString("description"));
				type.setCategory(rs.getString("category"));
				type.setBiomass(rs.getFloat("biomass"));
				type.setDietType(rs.getShort("diet_type"));
				type.setMetabolism(rs.getFloat("metabolism"));
				type.setTrophicLevel(rs.getFloat("trophic_level"));
				type.setGrowthRate(rs.getFloat("growth_rate"));
				type.setModelID(rs.getInt("model_id"));

				// Columns at smurf.sfsu.edu server
				// type.setName(rs.getString("name"));
				// type.setOrganismType(rs.getInt("organism_type"));
				// type.setCost(rs.getInt("cost"));
				// type.setDescription(rs.getString("description"));
				// type.setCategory(rs.getString("category"));
				// type.setAvgBiomass(rs.getInt("max_biomass"));
				// type.setWaterNeedFrequency(rs.getInt("water_need_frequency"));
				// type.setLightNeedFrequency(rs.getInt("light_need_frequency"));
				// type.setGrowRadius(rs.getFloat("grow_radius"));
				// type.setCarryingCapacity(rs.getFloat("carrying_capacity"));
				// type.setTrophicLevel(rs.getFloat("trophic_level"));
				// type.setGrowthRate(rs.getFloat("growth_rate"));
				// type.setModelID(rs.getInt("model_id"));
				// type.setHealChance(rs.getFloat("heal_chance"));
				// type.setGroupCapacity(rs.getInt("group_capacity"));

				// Node Distribution
				Map<Integer, Float> nodeDistribution = new HashMap<Integer, Float>();
				String[] nodeList = rs.getString("node_list").split(",");
				for (String node : nodeList) {
					String[] pair = node.split(":");
					nodeDistribution.put(Integer.parseInt(pair[0]), Float.parseFloat(pair[1]));
				}
				type.setNodeDistribution(nodeDistribution);

				// Prey List
				String[] preyStr = rs.getString("prey_list") == null ? new String[0]
						: rs.getString("prey_list").split(",");
				int[] preyList = new int[preyStr.length];
				for (int i = 0; i < preyStr.length; i++) {
					preyList[i] = Integer.parseInt(preyStr[i]);
				}
				type.setPreyIDs(preyList);

				// Predator List
				String[] predatorStr = rs.getString("predator_list") == null ? new String[0]
						: rs.getString("predator_list").split(",");
				int[] predatorList = new int[predatorStr.length];
				for (int i = 0; i < predatorStr.length; i++) {
					predatorList[i] = Integer.parseInt(predatorStr[i]);
				}
				type.setPredatorIDs(predatorList);

				types.add(type);
			}
		} catch (SQLException ex) {
			Log.println_e(ex.getMessage());
		} finally {
			GameDB.closeConnection(con, pstmt, rs);
		}

		return types;
	}

}
