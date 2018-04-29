package lby.net.response;

import shared.metadata.NetworkCode;
import lby.net.response.GameResponse;
import shared.util.GamePacket;
import shared.util.Log;

public class ResponseUpdateCredits extends GameResponse
{
    private short action;
    private short status;
    private int credits;


    public ResponseUpdateCredits()
    {
        response_id = NetworkCode.UPDATE_CREDITS;
    }

    @Override
    public byte[] getBytes()
    {
        GamePacket packet = new GamePacket(response_id);
        packet.addShort16(action);
        packet.addShort16(status);
        packet.addInt32(credits);
        Log.println("ResponseUpdateCredits.getBytes: action= " + action + ", status= " + status + ", credits = " + credits);


        return packet.getBytes();
    }

    public void setAction(short action)
    {
        this.action = action;
    }

    public void setStatus(short status)
    {
        this.status = status;
    }

    public void setCredits(int credits)
    {
        this.credits = credits;
    }
}
