/*
 * Copyright (c) 2010 DawningStreams, Inc.  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without 
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *  
 *  2. Redistributions in binary form must reproduce the above copyright notice, 
 *     this list of conditions and the following disclaimer in the documentation 
 *     and/or other materials provided with the distribution.
 *  
 *  3. The end-user documentation included with the redistribution, if any, must 
 *     include the following acknowledgment: "This product includes software 
 *     developed by DawningStreams, Inc." 
 *     Alternately, this acknowledgment may appear in the software itself, if 
 *     and wherever such third-party acknowledgments normally appear.
 *  
 *  4. The name "DawningStreams,Inc." must not be used to endorse or promote
 *     products derived from this software without prior written permission.
 *     For written permission, please contact DawningStreams,Inc. at 
 *     http://www.dawningstreams.com.
 *  
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 *  DAWNINGSTREAMS, INC OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  DawningStreams is a registered trademark of DawningStreams, Inc. in the United 
 *  States and other countries.
 *  
 */

package Examples.B_Exploring_Connectivity_Issues;

import Examples.Z_Tools_And_Others.Tools;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

public class Edge_Chihiro {
    
    public static final String Name = "Edge Chihiro";
    public static final int TcpPort = 9715;
    public static final PeerID PID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, Name.getBytes());
    public static final File ConfigurationFile = new File("." + System.getProperty("file.separator") + Name);
    
    public static void main(String[] args) {
        
        try {
            
            // Removing any existing configuration?
            Tools.CheckForExistingConfigurationDeletion(Name, ConfigurationFile);
            
            // Creation of the network manager
            NetworkManager MyNetworkManager = new NetworkManager(NetworkManager.ConfigMode.EDGE,
                    Name, ConfigurationFile.toURI());

            // Retrieving the network configurator
            NetworkConfigurator MyNetworkConfigurator = MyNetworkManager.getConfigurator();
            
            // Adding Jack and Aminah as RendezVous seeds
            MyNetworkConfigurator.clearRendezvousSeeds();
            
            String TheJackSeed = "tcp://" + InetAddress.getLocalHost().getHostAddress() + ":" + RendezVous_Jack.TcpPort;
            URI LocalSeedingRendezVousURI = URI.create(TheJackSeed);
            MyNetworkConfigurator.addSeedRendezvous(LocalSeedingRendezVousURI);

            String TheAminahSeed = "tcp://" + InetAddress.getLocalHost().getHostAddress() + ":" + RendezVous_Aminah.TcpPort;
            URI LocalSeedingRendezVousURI2 = URI.create(TheAminahSeed);
            MyNetworkConfigurator.addSeedRendezvous(LocalSeedingRendezVousURI2);

            // Setting Configuration
            MyNetworkConfigurator.setTcpPort(TcpPort);
            MyNetworkConfigurator.setTcpEnabled(true);
            MyNetworkConfigurator.setTcpIncoming(true);
            MyNetworkConfigurator.setTcpOutgoing(true);
            MyNetworkConfigurator.setUseMulticast(false);

            // Setting the Peer ID
            Tools.PopInformationMessage(Name, "Setting the peer ID to :\n\n" + PID.toString());
            MyNetworkConfigurator.setPeerID(PID);

            // Starting the JXTA network
            Tools.PopInformationMessage(Name, "Start the JXTA network and wait for a\n"
                    + "rendezvous connection for maximum 2 minutes");
            PeerGroup NetPeerGroup = MyNetworkManager.startNetwork();
            
            // Disabling any rendezvous autostart
            NetPeerGroup.getRendezVousService().setAutoStart(false);
            
            if (MyNetworkManager.waitForRendezvousConnection(120000)) {
                
                Tools.popConnectedRendezvous(NetPeerGroup.getRendezVousService(),Name);
                
            } else {
                
                Tools.PopInformationMessage(Name, "Did not connected to a rendezvous");

            }
            
            // Waiting for Jack to shutdown
            Tools.PopInformationMessage(Name, "Waiting for rendezvous to shut down");
            
            // Challenging existence of rendezvous
            Tools.PopInformationMessage(Name, "Waiting for cache to refresh for 30 seconds");
            
            // Going to sleep for some time
            Tools.GoToSleep(30000);

            // Waiting for rendezvous connection again
            Tools.PopInformationMessage(Name, "Waiting for rendezvous connection again");
            
            if (MyNetworkManager.waitForRendezvousConnection(120000)) {
                
                Tools.popConnectedRendezvous(NetPeerGroup.getRendezVousService(),Name);
                
            } else {
                
                Tools.PopInformationMessage(Name, "Did not connect to a rendezvous");

            }
            
            // Stopping the network
            Tools.PopInformationMessage(Name, "Stop the JXTA network");
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