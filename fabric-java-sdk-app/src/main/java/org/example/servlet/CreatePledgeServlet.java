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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.json.JSONObject;

/**
 * Servlet implementation class CreatePledgeServlet
 */
@WebServlet("/CreatePledgeServlet")
public class CreatePledgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int pledge_id = 1;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreatePledgeServlet() {
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
			req.put("pledgeId", "P" + pledge_id);
			pledge_id = pledge_id + 1;
			createPledge(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().append("Created Pledge with ID:" + "P" + (pledge_id - 1));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static String createPledge(JSONObject req) {
		try {
			String caUrl = ConfigNetwork.CA_ORG1_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(ConfigNetwork.ADMIN);
			adminUserContext.setAffiliation(ConfigNetwork.ORG1);
			adminUserContext.setMspId(ConfigNetwork.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(ConfigNetwork.ADMIN, ConfigNetwork.ADMIN_PASSWORD);

			// Register and enroll user
			String username = req.getString("uname");
			System.out.println("Registering Username:"+username);
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

			TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
			ChaincodeID ccid = ChaincodeID.newBuilder().setName(ConfigNetwork.CHAINCODE_1_NAME).build();
			request.setChaincodeID(ccid);
			request.setFcn("createPledge");
			String[] arguments = { req.getString("pledgeId"), req.getString("needId"), req.getString("qty"),
					req.getString("ngo"), req.getString("status") };
			request.setArgs(arguments);
			request.setProposalWaitTime(1000);

			Map<String, byte[]> tm2 = new HashMap<>();
			tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
			tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
			tm2.put("result", ":)".getBytes(UTF_8));
			tm2.put("rc", (ConfigNetwork.CHANNEL_NAME + "").getBytes(UTF_8));
			tm2.put(Util.EXPECTED_EVENT_NAME, Util.EXPECTED_EVENT_DATA);
			request.setTransientMap(tm2);
			Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
			for (ProposalResponse res : responses) {
				Status status = res.getStatus();
				Logger.getLogger(CreatePledgeServlet.class.getName()).log(Level.INFO,
						"Invoked createPledge on " + ConfigNetwork.CHAINCODE_1_NAME + ". Status - " + status);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// Test code
	public static void main(String args[]) {
		JSONObject req = new JSONObject();
		req.put("uname", "usr2");
		req.put("needId", "N1");
		req.put("pledgeId", "P1");
		req.put("qty", "100");
		req.put("ngo", "NGO2");
		req.put("status", "Pledged");
		createPledge(req);
	}


}
