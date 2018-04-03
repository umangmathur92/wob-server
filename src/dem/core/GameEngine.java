package dem.core;

// Java Imports
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dem.metadata.Constants;
import dem.model.*;
import lby.net.response.ResponseSpeciesCreate;
import dem.util.EventListener;
import dem.util.EventType;
import dem.util.Log;
import dem.util.NetworkFunctions;
import dem.util.Vector3;
import lby.core.Lobby;
import lby.core.world.World;
import dem.db.EcoSpeciesDAO;
import dem.db.SpeciesChangeListDAO;
import dem.db.StatsDAO;

/**
 * The GameEngine class is used to control the in-game time as well as
 * performing certain actions at specific time intervals for its assigned World.
 * Actions such as performing predictions and species interpolation. Other
 * methods contained in this class decides how an organism of a particular
 * species gets created and handled.
 */
public class GameEngine {

    private Lobby lobby;
    private final World world;
    private final Ecosystem ecosystem;
    private boolean isActive;
    private final ExecutorService predictionThreadPool = Executors.newCachedThreadPool();
    private final Queue<PredictionRunnable> waitList = new LinkedList<PredictionRunnable>();


    public GameEngine(Lobby lobby, World world, Ecosystem ecosystem) {
        this.lobby = lobby;
        this.world = world;
        this.ecosystem = ecosystem;
        this.ecosystem.setGameEngine(this);

    }


    public World getWorld() {
        return world;
    }

    public Ecosystem getEcosystem() {
        return ecosystem;
    }
    

    public void start() {
        isActive = true;
    }

    
    public HashMap<Integer, Integer> getSpeciesIdFromNodeIds(Map<Integer, Integer> nodeIdSpeciesList){
    	HashMap<Integer, Integer> speciesIdList = new HashMap<Integer, Integer>();
    	for (Entry<Integer, Integer> entry : nodeIdSpeciesList.entrySet()) {
	    	int node_id = entry.getKey(), biomass = entry.getValue();
	        SpeciesType speciesType = ServerResources.getSpeciesTable().getSpeciesTypeByNodeID(node_id);
	        int species_id = speciesType.getID();
	        speciesIdList.put(species_id, biomass);
    	}
    	return speciesIdList;
    }
    
    public void createSpeciesByPurchase(Player player, Map<Integer, Integer> speciesList, Ecosystem ecosystem) {
        for (Entry<Integer, Integer> entry : speciesList.entrySet()) {
            int species_id = entry.getKey(), biomass = entry.getValue();
            SpeciesType speciesType = ServerResources.getSpeciesTable().getSpecies(species_id);

            for (int node_id : speciesType.getNodeList()) {
            	ecosystem.setNewSpeciesNode(node_id, biomass);
            }

            Species species = null;

            int day = SpeciesChangeListDAO.getDay();
            if (ecosystem.containsSpecies(species_id)) {
                species = ecosystem.getSpecies(species_id);
                int eco_id = ecosystem.getID();
                int biomassPrev = EcoSpeciesDAO.getSpeciesBiomass(eco_id, species_id);

                // (biomassPrev + biomass)/size
                for (SpeciesGroup group : species.getGroups().values()) {
                    group.setBiomass((biomassPrev + biomass) / species.getGroups().size());
                    EcoSpeciesDAO.updateBiomass(eco_id, group.getID(), species_id, (biomassPrev + biomass) / species.getGroups().size()); 
                    if(!Constants.DEBUG_MODE){
	                    ResponseSpeciesCreate response = new ResponseSpeciesCreate(Constants.CREATE_STATUS_DEFAULT, ecosystem.getID(), group);
	                    NetworkFunctions.sendToLobby(response, lobby.getID());
                    }
                }
                SpeciesChangeListDAO.createEntry(eco_id, species_id, biomass, day);                
            } else {
                    int group_id = EcoSpeciesDAO.createSpecies(ecosystem.getID(), species_id, biomass);
                    SpeciesChangeListDAO.createEntry(ecosystem.getID(), species_id, biomass, day); 
                    species = new Species(species_id, speciesType);
                    SpeciesGroup group = new SpeciesGroup(species, group_id, biomass, Vector3.zero);
                    species.add(group);
                    if(!Constants.DEBUG_MODE){
	                    ResponseSpeciesCreate response = new ResponseSpeciesCreate(Constants.CREATE_STATUS_DEFAULT, ecosystem.getID(), group);
	                    NetworkFunctions.sendToLobby(response, lobby.getID());
                    }
            }

            ecosystem.addSpecies(species);

            // Logging Purposes
            int player_id = player.getID(), zone_id = ecosystem.getID();

            try {
                StatsDAO.createStat(species_id, getCurrentMonth(), "Purchase", biomass, player_id, zone_id);
            } catch (SQLException ex) {
                Log.println_e(ex.getMessage());
            }
        }
    }
    
    public void removeSpeciesFromZone(Player player, Map<Integer, Integer> speciesListForRemoval, Ecosystem ecosystem){
        for (Entry<Integer, Integer> entry : speciesListForRemoval.entrySet()) {
            int species_id = entry.getKey(), biomass = entry.getValue();

            SpeciesType speciesType = ServerResources.getSpeciesTable().getSpecies(species_id);

            for (int node_id : speciesType.getNodeList()) {
            	ecosystem.removeNode(node_id);
            }
            
            Species species = null;

            if (ecosystem.containsSpecies(species_id)) {
                species = ecosystem.getSpecies(species_id);

                for (SpeciesGroup group : species.getGroups().values()) {

                    EcoSpeciesDAO.removeSpecies(group.getID());
                    group.setBiomass(0);
                    if(!Constants.DEBUG_MODE){
	                    ResponseSpeciesCreate response = new ResponseSpeciesCreate(Constants.REMOVE_STATUS_DEFAULT, ecosystem.getID(), group);
	                    NetworkFunctions.sendToLobby(response, lobby.getID());
                    }
                }
                
            } 
            ecosystem.removeSpecies(species_id);
            ecosystem.removeEntry(species_id);

            // Logging Purposes
            int player_id = player.getID(), zone_id = ecosystem.getID();

            try {
                StatsDAO.createStat(species_id, getCurrentMonth(), "Remove", biomass, player_id, zone_id);
            } catch (SQLException ex) {
                Log.println_e(ex.getMessage());
            }
        }    	
    }
 
	
	public HashMap<Integer, Integer> getBiomassOfSpeciesInEcosystem(){	
		HashMap<Integer, Integer> map = EcoSpeciesDAO.getSpeciesWithNodeIdAndBiomass(ecosystem.getID());	
		return map;
	}
}
