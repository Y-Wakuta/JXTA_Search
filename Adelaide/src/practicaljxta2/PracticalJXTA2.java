/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package practicaljxta2;

import Examples.Z_Tools_And_Others.Tools;
import java.io.IOException;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;

/**
 *
 * @author rocko
 */
public class PracticalJXTA2 {
    public static final String Name = "Example 100";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here     
        try {

            // Creation of the network manager
            NetworkManager MyNetworkManager = new NetworkManager(
                    NetworkManager.ConfigMode.EDGE,
                    Name);
            

            // Starting JXTA
            Tools.PopInformationMessage(Name, "Starting JXTA network");
            PeerGroup ConnectedVia = MyNetworkManager.startNetwork();
            
            // Diplaying peer group information
            Tools.PopInformationMessage(Name, "Connected via Peer Group: " + ConnectedVia.getPeerGroupName());
            
            // Stopping JXTA
            Tools.PopInformationMessage(Name, "Stopping JXTA network");
            MyNetworkManager.stopNetwork();
            
        } catch (IOException Ex) {
            
            // Raised when access to local file and directories caused an error
            Tools.PopErrorMessage(Name, Ex.toString());
            
        } catch (PeerGroupException Ex) {
            
            // Raised when the net peer group could not be created
            Tools.PopErrorMessage(Name, Ex.toString());
            
        }
    }
    
}
