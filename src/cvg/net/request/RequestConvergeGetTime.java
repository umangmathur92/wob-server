/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cvg.net.request;

import cvg.match.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lby.net.request.GameRequest;
import cvg.net.response.ResponseConvergeGetTime;
import shared.util.DataReader;
import shared.util.Log;

/**
 *
 * @author justinacotter
 */
public class RequestConvergeGetTime extends GameRequest {
    
    private short betTime;
    private int round;
    private HashMap<Integer, Integer> betStatusList = new HashMap<Integer, Integer>();
    
    @Override
    public void parse(DataInputStream dataInput) throws IOException {
    	Log.consoleln("Parsing RequestConvergeGetTime");
        round = (int) DataReader.readShort(dataInput);
    }

    @Override
    public void process() throws Exception {
        ResponseConvergeGetTime response = new ResponseConvergeGetTime();
        
        int player_id = client.getPlayerID();
        MCMatchManager manager = MCMatchManager.getInstance();
        MCMatch match = manager.getMatchByPlayer(player_id);
        betTime = (short) match.getTimeWindow();
        
        long startTime = match.getStartTime();
        long presentTime = System.currentTimeMillis();
        betTime = (short) ( betTime - (presentTime - startTime) / 1000);
        
        response.setTime(betTime);
        Log.consoleln("Processed RequestConvergeGetTime. betTime = " + betTime);
         
        Map<Integer, MCMatchPlayer> playersList = match.getPlayers();
        Log.println("RequestConvergeGetTime, bet status values");
        Log.println("player id: " + player_id);
        for (Map.Entry<Integer, MCMatchPlayer> entry : playersList.entrySet()) {
            Integer key = entry.getKey();
            MCMatchPlayer value = getValue(entry);
            if ((!value.getLeftGame()) & (key != player_id)) {                
                Integer betStatus = value.getBetStatus(round);
                Log.println("Original id/ Bet status: " + key + " " + betStatus);
                // MCMatchPlayer betStatus is:
                //      0->no response; 1->no bet; 2->bet
                // This response status must be:
                //      0->no bet yet; 1->bet
                if (betStatus == 2) {
                    betStatus = 1;
                } else {
                    betStatus = 0;
                }
                betStatusList.put(key, betStatus);
                Log.println("Bet status: " + betStatus);
            }
        }   
        // Make sure the size is 4. That complies with the protocol
        while (betStatusList.size() < 4) {
            betStatusList.put(-betStatusList.size(), 0);
        }
        response.setBetStatusList(betStatusList);
        Log.println("RequestConvergeGetTime finished");
        client.add(response);        
    }
    
    MCMatchPlayer getValue(Map.Entry<Integer, MCMatchPlayer> entry) {
        return entry.getValue();
    }
}
