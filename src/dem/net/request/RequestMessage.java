package dem.net.request;

// Java Imports

import dem.db.LogDAO;
import dem.net.response.ResponseMessage;
import dem.util.DataReader;
import dem.util.NetworkFunctions;

import java.io.DataInputStream;
import java.io.IOException;

// Other Imports

/**
 * The RequestMessage class handles all incoming chat messages and redirect those
 * messages, if needed, to other users.
 */
public class RequestMessage extends GameRequest {

    private short type;
    private String message;

    @Override
    public void parse(DataInputStream dataInput) throws IOException {
        type = DataReader.readShort(dataInput);
        message = DataReader.readString(dataInput).trim();

        if (message.isEmpty()) {
            throw new IOException();
        }
    }

    @Override
    public void process() throws Exception {
        LogDAO.createMessage(client.getAccount().getID(), message);

        ResponseMessage response = new ResponseMessage();
        response.setMessage(message);
        response.setName(client.getPlayer().getName());
        response.setType(type);
        NetworkFunctions.sendToGlobal(response);
    }
}
