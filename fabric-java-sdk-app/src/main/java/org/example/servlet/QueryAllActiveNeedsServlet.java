/****************************************************** 
 *  Copyright 2019 IBM Corporation 
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License.
 */ 

package org.example.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.client.CAClient;
import org.example.client.ChannelClient;
import org.example.client.FabricClient;
import org.example.config.ConfigNetwork;
import org.example.user.UserContext;
import org.example.util.Util;
import org.hyperledger.fabric.sdk.Channel;

import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.json.JSONObject;

/**
 * Servlet implementation class QueryAllActiveNeedsServlet
 */
@WebServlet("/QueryAllActiveNeedsServlet")
public class QueryAllActiveNeedsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryAllActiveNeedsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
			}
			Logger.getLogger(getServletName()).log(Level.INFO, "Received configuration - " + sb.toString());
			JSONObject req = new JSONObject(sb.toString());
			String res = queryAllActiveNeeds(req);
			response.getWriter().append(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static String queryAllActiveNeeds(JSONObject req)
	{
		String stringResponse = "";
		try {
        	String caUrl = ConfigNetwork.CA_ORG1_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(org.example.config.ConfigNetwork.ADMIN);
			adminUserContext.setAffiliation(ConfigNetwork.ORG1);
			adminUserContext.setMspId(ConfigNetwork.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(ConfigNetwork.ADMIN, ConfigNetwork.ADMIN_PASSWORD);
			
			// Register and enroll user
			String username = req.getString("uname");
			UserContext uContext = new UserContext();
			uContext.setName(username);
			uContext.setAffiliation(ConfigNetwork.ORG1);
			uContext.setMspId(ConfigNetwork.ORG1_MSP);
			String secret = caClient.registerUser(username, ConfigNetwork.ORG1);
			uContext = caClient.enrollUser(uContext, secret);

			FabricClient fabClient = new FabricClient(uContext);
			
			ChannelClient channelClient = fabClient.createChannelClient(ConfigNetwork.CHANNEL_NAME);
			Channel channel = channelClient.getChannel();
			Peer peer = fabClient.getInstance().newPeer(ConfigNetwork.ORG1_PEER_1, ConfigNetwork.ORG1_PEER_1_URL);
			Orderer orderer = fabClient.getInstance().newOrderer(ConfigNetwork.ORDERER_NAME, ConfigNetwork.ORDERER_URL);
			channel.addPeer(peer);
			channel.addOrderer(orderer);
			channel.initialize();

			Logger.getLogger(QueryAllActiveNeedsServlet.class.getName()).log(Level.INFO, "Querying for all active needs ...");
			String[] args = {req.getString("date")};
			Collection<ProposalResponse>  responsesQuery = channelClient.queryByChainCode(ConfigNetwork.CHAINCODE_1_NAME, "queryAllActiveNeeds", args);
			for (ProposalResponse pres : responsesQuery) {
				stringResponse = new String(pres.getChaincodeActionResponsePayload());
				Logger.getLogger(QueryAllActiveNeedsServlet.class.getName()).log(Level.INFO, stringResponse);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringResponse;
	}

	//Test code
	public static void main(String[] args) {
		JSONObject req = new JSONObject();
		req.put("uname", "usr2");
		req.put("date", "19-2-2019");
		queryAllActiveNeeds(req);
	}
}
