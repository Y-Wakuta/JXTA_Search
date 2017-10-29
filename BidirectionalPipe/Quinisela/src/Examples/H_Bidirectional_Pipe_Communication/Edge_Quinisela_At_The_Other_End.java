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

package H_Bidirectional_Pipe_Communication;

import Z_Tools_And_Others.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.util.JxtaBiDiPipe;

public class Edge_Quinisela_At_The_Other_End implements PipeMsgListener {

    public static final String Name = "Edge Quinisela, at the other end";
    public static final int TcpPort = 9725;
    public static final PeerID PID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, Name.getBytes());
    public static final File ConfigurationFile = new File("." + System.getProperty("file.separator") + Name);

    private static  PipeMsgListener _myListener =new Edge_Quinisela_At_The_Other_End();
    private static PeerGroup _netPeerGroup = null;
    private static NetworkManager _myNetworkManager = null;
    private static JxtaBiDiPipe _myBiDiPipe = null;

    public void pipeMsgEvent(PipeMsgEvent PME) {
        try{
            JDBC jdbc = new JDBC();

            // We received a message
            Message ReceivedMessage = PME.getMessage();
            String TheText = ReceivedMessage.getMessageElement("DummyNameSpace", "Query").toString();
            if(TheText == null){
                String[] texts = TheText.split("\n");
                for (String text:texts) {
                    System.out.println(text);
                }
                return;
            }else {
                TheText = ReceivedMessage.getMessageElement("DummyNameSpace","Query").toString();
                // Notifying the user
                String result= jdbc.Deserialize(jdbc.ExecSql(TheText));

                Message MyMessage = new Message();
                StringMessageElement MyStringMessageElement = new StringMessageElement("QueryResult", result, null);

                MyMessage.addMessageElement("DummyNameSpace", MyStringMessageElement);

                _myBiDiPipe.sendMessage(MyMessage);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        try {

            // Removing any existing configuration?
            Tools.CheckForExistingConfigurationDeletion(Name, ConfigurationFile);

            // Creation of network manager
            _myNetworkManager = new NetworkManager(NetworkManager.ConfigMode.EDGE,
                    Name, ConfigurationFile.toURI());

            // Retrieving the network configurator
            NetworkConfigurator MyNetworkConfigurator = _myNetworkManager.getConfigurator();

            // Checking if RendezVous_Adelaide_At_One_End should be a seed
            MyNetworkConfigurator.clearRendezvousSeeds();
            String TheSeed = "tcp://" + InetAddress.getLocalHost().getHostAddress() + ":" + RendezVous_Adelaide_At_One_End.TcpPort;
            Tools.CheckForRendezVousSeedAddition(Name, TheSeed, MyNetworkConfigurator);

            // Setting Configuration
            MyNetworkConfigurator.setTcpPort(TcpPort);
            MyNetworkConfigurator.setTcpEnabled(true);
            MyNetworkConfigurator.setTcpIncoming(true);
            MyNetworkConfigurator.setTcpOutgoing(true);

            // Setting the Peer ID
            Tools.PopInformationMessage(Name, "Setting the peer ID to :\n\n" + PID.toString());
            MyNetworkConfigurator.setPeerID(PID);

            // Starting the JXTA network
            Tools.PopInformationMessage(Name, "Start the JXTA network and to wait for a rendezvous connection with\n"
                    + RendezVous_Adelaide_At_One_End.Name + " for maximum 2 minutes");
            _netPeerGroup = _myNetworkManager.startNetwork();
            InitNetwork(_myNetworkManager, _netPeerGroup);
            // Preparing the listener and Creating the BiDiPipe
            _myBiDiPipe = new JxtaBiDiPipe(_netPeerGroup, RendezVous_Adelaide_At_One_End.GetPipeAdvertisement(), 30000, _myListener);
            InputStreamReader isr = new InputStreamReader(System.in);

            if(_myBiDiPipe.isBound())
                Tools.PopInformationMessage(Name, "Bidirectional pipe created!");
            while (_myBiDiPipe.isBound()) {
                System.out.print("input query:");
                BufferedReader bfr = new BufferedReader(isr);
                String query = bfr.readLine();
                Message MyMessage = new Message();
                StringMessageElement MyStringMessageElement = new StringMessageElement("Query", query, null);

                MyMessage.addMessageElement("DummyNameSpace", MyStringMessageElement);

                _myBiDiPipe.sendMessage(MyMessage);

                // Sleeping for 10 seconds
                Tools.GoToSleep(15000);
            }

            QuiniselaDataHandler.CloseNetwork(_myBiDiPipe, _myNetworkManager,Name);

        } catch (IOException Ex) {

            // Raised when access to local file and directories caused an error
            Tools.PopErrorMessage(Name, Ex.toString());
        }catch (PeerGroupException e){
            System.out.println(e.getMessage());
        }
    }

    public static void InitNetwork(NetworkManager net,PeerGroup peerGroup){
        // Disabling any rendezvous autostart
        peerGroup.getRendezVousService().setAutoStart(false);

        if (net.waitForRendezvousConnection(120000)) {
            Tools.popConnectedRendezvous(peerGroup.getRendezVousService(),Name);
        } else {
            Tools.PopInformationMessage(Name, "Did not connect to a rendezvous");
        }

    }
}