package dem.net.request.world;

// Java Imports

import dem.core.world.WorldController;
import dem.net.request.GameRequest;

import java.io.DataInputStream;
import java.io.IOException;

// Other Imports

public class RequestWorld extends GameRequest {

    @Override
    public void parse(DataInputStream dataInput) throws IOException {
    }

    @Override
    public void process() throws Exception {
        if (client.getPlayer().getWorld() == null) {
            WorldController.enterWorld(client.getPlayer(), WorldController.getInstance().first().getID());
        }
    }
}
