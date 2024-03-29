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

package Examples.Z_Tools_And_Others;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.exception.PeerGroupException;
import net.jxta.impl.peergroup.GenericPeerGroup;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.JxtaLoader;
import net.jxta.platform.Module;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.rendezvous.RendezVousService;

public class JXTA_Bootstrapping {
    
    public static void main(String args[]) throws Throwable {
        
        try {
            
            // Removing verbosity
            Logger.getLogger("net.jxta").setLevel(Level.SEVERE);
             
            // Retrieving the JXTA loader
            JxtaLoader TheLoader = GenericPeerGroup.getJxtaLoader();
            
            System.out.println("\nBefore starting the JXTA network:");
             
            // Trying to retrieve the RendezVous Java class
            try {
            
                Class<? extends Module> TheRDVClass = TheLoader.findClass(PeerGroup.refRendezvousSpecID);
                System.out.println(TheRDVClass.toString());
                
            } catch (ClassNotFoundException Ex) {
                
                System.out.println("Module class not found");
                
            }
            
            // Trying to retrieve the default rendezvous implementation advertisement
            ModuleImplAdvertisement TheModuleImplAdv = TheLoader.findModuleImplAdvertisement(PeerGroup.refRendezvousSpecID);
            
            if (TheModuleImplAdv == null) {
                System.out.println("Advertisement not found");
            } else {
                System.out.println(TheModuleImplAdv.toString());
            }
            

            // Creation of the network manager
            NetworkManager MyNetworkManager = new NetworkManager(
                    NetworkManager.ConfigMode.EDGE,
                    "My Network Manager instance name");
            
            // Starting JXTA and retrieving the net peer group
            PeerGroup TheNetPeerGroup = MyNetworkManager.startNetwork();
            
            System.out.println("\nAfter starting the JXTA network:");

            // Trying to retrieve the RendezVous Java class
            try {
            
                Class<? extends Module> TheRDVClass = TheLoader.findClass(PeerGroup.refRendezvousSpecID);
                System.out.println(TheRDVClass.toString());
                
            } catch (ClassNotFoundException Ex) {
                
                System.out.println("Module class not found");
                
            }
            
            // Trying to retrieve the default rendezvous implementation advertisement
            TheModuleImplAdv = TheLoader.findModuleImplAdvertisement(PeerGroup.refRendezvousSpecID);
            
            if (TheModuleImplAdv == null) {
                System.out.println("Advertisement not found");
            } else {
                System.out.println(TheModuleImplAdv.toString());
            }
            
            // Checking with the rendezvous service
            System.out.println("\nChecking with RendezVous Service:");
            
            RendezVousService TheStandardRDVService = TheNetPeerGroup.getRendezVousService();
            
            ModuleImplAdvertisement TheRDVModuleImplAdv = (ModuleImplAdvertisement) TheStandardRDVService.getImplAdvertisement();
            
            if (TheModuleImplAdv == null) {
                System.out.println("RendezVous advertisement not found");
            } else {
                System.out.println(TheModuleImplAdv.toString());
            }
            
            // Stopping JXTA
            MyNetworkManager.stopNetwork();
            
        } catch (IOException Ex) {
            
            Ex.printStackTrace();
            
        } catch (PeerGroupException Ex) {
            
            Ex.printStackTrace();
            
        }
        
    }

    public JXTA_Bootstrapping() {

    }

}
