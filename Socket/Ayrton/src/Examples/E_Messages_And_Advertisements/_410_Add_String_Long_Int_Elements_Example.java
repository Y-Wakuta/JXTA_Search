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

package Examples.E_Messages_And_Advertisements;

import Examples.Z_Tools_And_Others.Tools;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;

public class _410_Add_String_Long_Int_Elements_Example {
    
    public static final String Name = "Example 410";

    public static void main(String[] args) {

        // Creating an empty message
        Message MyMessage = new Message();
        
        // Creating string element
        StringMessageElement MyStringMessageElement = new StringMessageElement(
                "MyStringElement", "My string message content", null);
        
        // Creating int element
        StringMessageElement MyIntMessageElement = new StringMessageElement(
                "MyIntElement", Integer.toString(33), null);
                
        // Creating long element
        StringMessageElement MyLongMessageElement = new StringMessageElement(
                "MyLongElement", Long.toString(29345677), null);

        // Adding string message elements
        MyMessage.addMessageElement("MyNameSpace", MyStringMessageElement);
        MyMessage.addMessageElement("MyNameSpace", MyIntMessageElement);
        MyMessage.addMessageElement("MyNameSpace", MyLongMessageElement);
        
        // Displaying message
        Tools.DisplayMessageContent(Name, MyMessage);
        
    }
        
}
