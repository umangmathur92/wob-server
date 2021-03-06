/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rr.net.response;

import rr.metadata.Constants;
import rr.util.GamePacket;

/**
 * Send Species ID to opponent of player who made request. 
 * @author Sbc-ComEx
 */
public class ResponseRRSpecies extends GameResponse {
    private int id;

    public ResponseRRSpecies() {
         responseCode = Constants.SMSG_RRSPECIES;
    }
    
    @Override
    public byte[] constructResponseInBytes() {
         GamePacket packet = new GamePacket(responseCode);
         packet.addInt32(id);
         return packet.getBytes();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
    
}
