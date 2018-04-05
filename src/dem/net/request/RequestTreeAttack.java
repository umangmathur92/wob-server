package dem.net.request;

// Java Imports
import java.io.IOException;
import java.io.DataInputStream;
// Other Imports

import dem.core.match.*;
import dem.metadata.Constants;
import dem.metadata.NetworkCode;
import dem.net.response.ResponseTreeAttack;
import dem.net.request.GameRequest;
import dem.util.DataReader;
import dem.util.Log;

/**
 * The RequestTreeAttack class sends attack data to player2
 */

public class RequestTreeAttack extends GameRequest {
	private int playerID; // changed from match ID number
	private int attackersPosition; // attack value from player 1

	@Override
	public void parse(DataInputStream dataInput) throws IOException {
		playerID = DataReader.readInt(dataInput);
		attackersPosition = DataReader.readInt(dataInput);
	}

	@Override
	public void process() throws Exception {
		Log.printf("Request Tree Attack with playerId:%d,attackersPosition:%d", playerID, attackersPosition);
		ResponseTreeAttack response = new ResponseTreeAttack();
		MatchAction action = new MatchAction();

		action.setActionID(NetworkCode.TREE_ATTACK);
		action.setIntCount(1);
		action.setStringCount(0);
		action.addInt(attackersPosition);

		MatchManager manager = MatchManager.getInstance();
		Match match = manager.getMatchByPlayer(playerID);

		response.setStatus((short) 0);

		client.add(response);
		if (!Constants.SINGLE_PLAYER) {
			match.addMatchAction(playerID, action);
		}
	}

}
