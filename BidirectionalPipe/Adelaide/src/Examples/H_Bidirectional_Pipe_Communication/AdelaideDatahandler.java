package Examples.H_Bidirectional_Pipe_Communication;

import Examples.Z_Tools_And_Others.Tools;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.util.JxtaBiDiPipe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdelaideDatahandler {

    public static void CloseNetwork(JxtaBiDiPipe pipe, NetworkManager net, PeerGroup pGroup, String name){
          // Closing the bidi pipe server
        try {
            pipe.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

            // Retrieving connected peers
            Tools.popConnectedPeers(pGroup.getRendezVousService(), name);

            // Stopping the network
            Tools.PopInformationMessage(name, "Stop the JXTA network");
            net.stopNetwork();
        }
}
