package dem.net.request;

// Java Imports

import dem.db.PlayerDAO;
import dem.model.Player;
import dem.util.DataReader;
import dem.util.Log;

import java.io.DataInputStream;
import java.io.IOException;

// Other Imports

public class RequestPlayerSelect extends GameRequest {

    private int player_id;

    @Override
    public void parse(DataInputStream dataInput) throws IOException {
        player_id = DataReader.readInt(dataInput);
        Log.println("Parse RequestPlayerSelect");
    }

    @Override
    public void process() throws Exception {
        Player player = PlayerDAO.getPlayerByAccount(client.getAccount().getID());

        if (player != null) {
            client.select(player);
        }
    }
}
