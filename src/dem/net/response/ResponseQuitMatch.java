package dem.net.response;

// Other Imports
import dem.metadata.NetworkCode;
import dem.util.GamePacket;
import dem.net.response.GameResponse;

/**
 *  Sets other player's status to inactive
 */
public class ResponseQuitMatch extends GameResponse {
    private short status;

    public ResponseQuitMatch() {
        response_id = NetworkCode.QUIT_MATCH;
    }

    @Override
    public byte[] getBytes() {
        GamePacket packet = new GamePacket(response_id);
        return packet.getBytes();
    }

    public void setStatus(short status) {
        this.status = status;
    }
}
