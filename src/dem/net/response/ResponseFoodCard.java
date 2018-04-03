package dem.net.response;

import dem.net.response.GameResponse;
import dem.metadata.NetworkCode;
import dem.util.GamePacket;


public class ResponseFoodCard extends GameResponse{
    private short status;

    public ResponseFoodCard() {
        response_id = NetworkCode.APPLY_FOOD;
    }

    @Override
    public byte[] getBytes() {
        GamePacket packet = new GamePacket(response_id);
        packet.addShort16(status);

        return packet.getBytes();
    }

    public void setStatus(short status){
    	this.status = status;
    }
}
