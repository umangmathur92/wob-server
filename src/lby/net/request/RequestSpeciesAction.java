package lby.net.request;

// Java Imports
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import shared.core.EcosystemController;
import lby.core.world.World;
import shared.db.ShopDAO;
import shared.model.Ecosystem;
import shared.model.ShopItem;
import lby.net.response.ResponseSpeciesAction;
import shared.core.ServerResources;
import shared.db.EcoSpeciesDAO;
import shared.db.SpeciesChangeListDAO;
import shared.model.Species;
import shared.model.SpeciesType;
import shared.util.DataReader;
import shared.util.Log;

public class RequestSpeciesAction extends GameRequest {

    private short action;
    private short type;
    private int species_id, startDay;
    private short index;
    private String spStr;
    private Map<Integer, Integer> speciesList;
    private List<Species> speciesListFull;

    @Override
    public void parse(DataInputStream dataInput) throws IOException {
        action = DataReader.readShort(dataInput);

        if (action == 0) {
            type = DataReader.readShort(dataInput);
        } else if (action == 1) {
            short size = DataReader.readShort(dataInput);
            speciesList = new HashMap<Integer, Integer>();
            Log.println("RequestSpeciesAction, action = 1, size = " + size);

            int species_id, biomass;

            for (int i = 0; i < size; i++) {
                species_id = DataReader.readInt(dataInput);
                biomass = DataReader.readInt(dataInput);

                speciesList.put(species_id, biomass);
                Log.println("RequestSpeciesAction, id,biomass = " + species_id + " " + biomass);
            }
        } else if (action == 3) {
            species_id = DataReader.readInt(dataInput);
            // index is used by client to know for which species it is receiving data
            index = DataReader.readShort(dataInput);
        } else if (action == 4) {
            species_id = DataReader.readInt(dataInput);            
            Log.println("RequestSpeciesAction, parse, action = 4 id = " + species_id);
        } else if (action == 7) {
            species_id = DataReader.readInt(dataInput);     
            startDay = DataReader.readInt(dataInput);
            Log.println("RequestSpeciesAction, parse, action = 7, id/day = " + species_id + " " + startDay);
        } else if (action == 8) {
            spStr = DataReader.readString(dataInput);
            Log.println("RequestSpeciesAction, parse, action = 8, species string = " + spStr);
        }
    }

    @Override
    public void process() throws Exception {
        short status = 0;

        ResponseSpeciesAction response = new ResponseSpeciesAction();
        response.setAction(action);
        response.setStatus(status);

        if (action == 0) {
            response.setType(type);

            if (type == 0) { // Get Default Species
                speciesList = new HashMap<Integer, Integer>();
                speciesList.put(13, 5000);
                speciesList.put(20, 5000);
                speciesList.put(31, 5000);

                String selectionList = "";

                int index = 0;
                for (Entry<Integer, Integer> entry : speciesList.entrySet()) {
                    selectionList += entry.getKey() + ":" + entry.getValue();

                    if (index++ < speciesList.size() - 1) {
                        selectionList += ",";
                    }
                }

                response.setSelectionList(selectionList);
            } else if (type == 1) { // Get Every Species
                String[] settings = new String[]{"30000", "2,10", "500,1000,2500"};

                List<ShopItem> shopList = ShopDAO.getItems("level:0,99");
                String selectionList = "";

                int index = 0;
                for (ShopItem item : shopList) {
                    selectionList += item.getID();

                    if (index++ < shopList.size() - 1) {
                        selectionList += ",";
                    }
                }

                response.setSettings(settings);
                response.setSelectionList(selectionList);
            }

            client.add(response);
        } else if (action == 1) { // Create Ecosystem Using Species
            // Log.println("RequestSpeciesAction, process, client.getPlayer() == null? " + (client.getPlayer() == null));
            Ecosystem ecosystem = client.getPlayer().getEcosystem();
            // Log.println("RequestSpeciesAction, process, ecosystem == null? " + (ecosystem == null));

            if (ecosystem != null) {
                EcosystemController.createEcosystem(ecosystem, speciesList);

                String selectionList = "";

                int index = 0;
                for (Entry<Integer, Integer> entry : speciesList.entrySet()) {
                    selectionList += entry.getKey() + ":" + entry.getValue();

                    if (index++ < speciesList.size() - 1) {
                        selectionList += ",";
                    }
                }
                response.setSelectionList(selectionList);
                client.add(response);
            }
        } else if (action == 2) { // Return species_id, biomass pairs for Ecosystem
            // Log.println("RequestSpeciesAction, action = 2");
            // speciesListFull = EcosystemController.getInstance().getEcosystem().getSpeciesList();
            // speciesListFull = client.getPlayer().getEcosystem().getSpeciesList();
            speciesListFull = EcoSpeciesDAO.getSpecies(client.getPlayer().getEcosystem().getID());
            int count = speciesListFull.size();  
            Log.println("RequestSpeciesAction, speciesListFull.size() = " + speciesListFull.size());
            response.setCount(count);
            for (Species species : speciesListFull) {
                int species_id = species.getID();
                int biomass = species.getTotalBiomass();
                Log.println("species_id, biomass = " + species_id + " " + biomass);
                response.addSpeciesList(species_id, biomass);
            }
            /* This is for the memory map
            for (Integer key : speciesListFull.keySet()) {
                Species species = speciesListFull.get(key);
                int species_id = species.getID();
                int biomass = species.getTotalBiomass();
                Log.println("species_id, biomass = " + species_id + " " + biomass);
                response.addSpeciesList(species_id, biomass);
            }
            */
            client.add(response);
        } else if (action == 3) { // Return biomass (not owned biomass) & cost from species DB for given species_id
            SpeciesType species = ServerResources.getSpeciesTable().getSpecies(species_id);
            response.setSpeciesId(species_id);
            response.setCost(species.getCost());
            response.setBiomass(species.getBiomass());   
            response.setIndex(index);
            client.add(response);
        } else if (action == 4) { // Return species biomass change history. Pairs of <day, biomass change>            
            response.setSpeciesId(species_id);
            response.setSpeciesHistoryList(
                    SpeciesChangeListDAO.getSpeciesHistory(client.getPlayer().getEcosystem().getID(), species_id));
            Log.println("RequestSpeciesAction, process, action = 4, size = " + response.speciesHistoryList.size());
            client.add(response);
        } else if (action == 5) {  // Return just the count of species owned
            Log.println("RequestSpeciesAction, action = 5");
            speciesListFull = EcoSpeciesDAO.getSpecies(client.getPlayer().getEcosystem().getID());
            int count = speciesListFull.size();  
            Log.println("RequestSpeciesAction, speciesListFull.size() = " + speciesListFull.size());
            response.setCount(count);
            client.add(response);
        }
        else if (action == 6) {  // Returns 3 day values; current day, first day of ecosystem, and the last simulations day
            // Generally, the first and last day values should be the same 
            Log.println("RequestSpeciesAction, action = 6");
            int [] results = new int[3];
            results = SpeciesChangeListDAO.getDayInfo(client.getPlayer().getEcosystem().getID());
            Log.println("RequestSpeciesAction, c,f,lDay = " + results[0] + " " + results[1] + " " + results[2]);
            response.setCDay(results[0]);
            response.setFDay(results[1]);
            response.setLDay(results[2]);
            client.add(response);
        } else if (action == 7) { // Return species biomass change history. Pairs of <day, biomass change>. Use startDay
            response.setSpeciesId(species_id);
            response.setSpeciesHistoryList(
                    SpeciesChangeListDAO.getSpeciesHistory(client.getPlayer().getEcosystem().getID(), species_id, startDay));
            Log.println("RequestSpeciesAction, process, action = 7, size = " + response.speciesHistoryList.size());
            client.add(response);
        } else if (action == 8) { // Generate food web graph            
            try {
                String cmd, s;
                Process p;
            
                cmd = "atn-generate-food-web.py --parent-dir /home/wob_server/src from-node-ids " + spStr;
                Log.println("Executing: " + cmd);            
                p = Runtime.getRuntime().exec(cmd); 
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((s = stdInput.readLine()) != null) {
                    Log.println("Out:" + s);
                }

                int exitVal = p.waitFor();
                Log.println("ExitValue: " + exitVal);
            } catch (Exception e) {
                Log.println("atn-generate-food-web.py exception: " + e.toString());
            }
        }
    }
}
