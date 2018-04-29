package lby.net.request;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lby.core.world.World;
import lby.net.request.GameRequest;
import lby.net.response.ResponseUpdateCredits;
import shared.core.GameResources;
import shared.util.DataReader;
import shared.util.Log;


public class RequestUpdateCredits extends GameRequest
{
    private short action;
    private int credits;

    @Override
    public void parse(DataInputStream dataInput) throws IOException
    {
        action = DataReader.readShort(dataInput);
        credits = DataReader.readInt(dataInput);
        Log.println("RequestUpdateCredits parsing: action = " + action + ", credits =" + credits);
    }

    @Override
    public void process() throws Exception
    {
        World world = client.getPlayer().getWorld();

        if (world != null)
        {
            ResponseUpdateCredits response = new ResponseUpdateCredits();
            response.setAction(action);
            response.setCredits(credits);

            if(action==0)//adding credits
            {
                if(GameResources.useCredits(client.getPlayer(), -credits))
                {
                    Log.println("RequestUpdateCredits processing, action =0: adding " + credits + " credits" );
                    response.setStatus(0);
                }
                else
                {
                    Log.println("RequestUpdateCredits processing, action =0: failed to add " + credits + " credits" );
                    response.setStatus(1);
                }

            }
            else if(action==1)//subtracting credits
            {

                if(GameResources.useCredits(client.getPlayer(), credits))
                {
                    Log.println("RequestUpdateCredits processing, action =1: subtracting " + credits + " credits");
                    response.setStatus(0);
                }
                else
                {
                    Log.println("RequestUpdateCredits processing, action =1: failed to subtract " + credits + " credits" );
                    response.setStatus(1);
                }

            }


            client.add(response);


        }

    }



}
