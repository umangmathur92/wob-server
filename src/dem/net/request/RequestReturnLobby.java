package dem.net.request;

import java.io.DataInputStream;
import java.io.IOException;


import dem.net.request.GameRequest;
import dem.net.response.ResponseReturnLobby;
import dem.util.Log;

public class RequestReturnLobby extends GameRequest {

	@Override
	public void parse(DataInputStream dataInput) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void process() throws Exception {
		// TODO Auto-generated method stub
                Log.printf("RequestReturnToLobby");
		ResponseReturnLobby response = new ResponseReturnLobby();
		short status = 0;
		response.setStatus(status);
		//client.add(response);
	
		
		/*
		 * Leaving client connected. TODO: Lobby
		// ha: clean up player info from server 
        GameServer server = GameServer.getInstance();
        
        server.removeActiveClient(client.getID());
        server.removeActivePlayer(client.getPlayer().getID());
    	*/
	
	}
	
	

}
