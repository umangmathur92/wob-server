package lby.net.response;

// Other Imports
import shared.metadata.NetworkCode;
import shared.util.GamePacket;
import java.util.Map;
import java.util.HashMap;
import shared.util.Log;

public class ResponseSpeciesAction extends GameResponse {

    private short action;
    private short status;
    private short type;
    private short count;
    private int species_id;
    private short index;
    private float biomass;
    private int cost;
    private int cDay, fDay, lDay;
    private String[] settings;
    private String selectionList;
    private Map<Integer, Integer> speciesList = new HashMap<Integer, Integer>();
    public Map<Integer, Integer> speciesHistoryList = new HashMap<Integer, Integer>();
    private int byteCount, startByte;
    private byte[] fileContents;

    public ResponseSpeciesAction() {
        response_id = NetworkCode.SPECIES_ACTION;
    }

    @Override
    public byte[] getBytes() {
        GamePacket packet = new GamePacket(response_id);
        packet.addShort16(action);
        packet.addShort16(status);

        if (action == 0) {
            packet.addShort16(type);

            if (type == 0) {
                packet.addString(selectionList);
            } else if (type == 1) {
                packet.addShort16((short) settings.length);

                for (String value : settings) {
                    packet.addString(value);
                }
                packet.addString(selectionList);
            }
        } else if (action == 1) {
            packet.addString(selectionList);
        } else if (action == 2) {
            packet.addShort16(count);
            for (Integer key : speciesList.keySet()) {
                packet.addInt32(key);
                packet.addInt32(speciesList.get(key));    
            }
        } else if (action == 3) {
            packet.addInt32(species_id);
            packet.addInt32(cost);
            packet.addFloat(biomass);
            packet.addShort16(index);
        } else if ((action == 4) || (action == 7)) {  // 4 returns all days, 7 returns from a starting day
            Log.println("ResponseSpeciesAction, action = 4 or 7, species, size = " + species_id + " " + speciesHistoryList.size());
            packet.addInt32(species_id);
            packet.addShort16((short) speciesHistoryList.size());
            for (Integer key : speciesHistoryList.keySet()) {
                packet.addInt32(key);
                packet.addInt32(speciesHistoryList.get(key));    
                Log.println("day, biomass change = " + key + " " + speciesHistoryList.get(key));
            }
        } else if (action == 5) {
            packet.addShort16(count);
            Log.println("ResponseSpeciesAction, action = 5, species, size = " + count);
        } else if (action == 6) {
            Log.println("ResponseSpeciesAction, action = 6, values are: " + cDay + " " + fDay + " " + lDay);
            packet.addInt32(cDay);
            packet.addInt32(fDay);
            packet.addInt32(lDay);
        } else if (action == 8) {
            Log.println("ResponseSpeciesAction, action = 8, byteCount = " + byteCount);
            packet.addInt32(byteCount);
        } else if (action == 9) {
            Log.println("ResponseSpeciesAction, action = 9, byteCount, startByte = " 
                    + byteCount + " " + startByte);
            packet.addInt32(startByte);
            packet.addInt32(byteCount);
            packet.addBytes(fileContents);
        } 
        
        return packet.getBytes();
    }

    public void setAction(short action) {
        this.action = action;
    }

    public void setStatus(short status) {
        this.status = status;
    }
    
    public void setType(short type) {
        this.type = type;
    }

    public void setSettings(String[] settings) {
        this.settings = settings;
    }

    public void setSelectionList(String selectionList) {
        this.selectionList = selectionList;
    }
    
    public void setCount(int count) {
        this.count = (short) count;
    }
    
    public void addSpeciesList(int species_id, int biomass) {
        speciesList.put(species_id, biomass);
    }
    
    public void setSpeciesHistoryList(Map<Integer, Integer> speciesHistoryList) {
        this.speciesHistoryList = speciesHistoryList;
    }
    
    public void setSpeciesId(int species_id) {
        this.species_id = species_id;
    }
    
    public void setIndex(short index) {
        this.index = index;
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }
    
    public void setBiomass(float biomass) {
        this.biomass = biomass;
    }    
    
    public void setCDay(int cDay) {
        this.cDay = cDay;
    }    
    
    public void setFDay(int fDay) {
        this.fDay = fDay;
    }    
    
    public void setLDay(int lDay) {
        this.lDay = lDay;
    }    
    
    public void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }
    
    public void setBytes(byte[] fileContents) {
        this.fileContents = fileContents;
    }
    
    public void setStartByte(int startByte) {
        this.startByte = startByte;
    }
}
