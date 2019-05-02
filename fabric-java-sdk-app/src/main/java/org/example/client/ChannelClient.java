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

package org.example.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeID.Builder;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

/**
 * 
 * @author bkadambi
 *
 */
public class ChannelClient {
	String name;
	Channel channel;
	FabricClient fabClient;

	public String getName() {
		return this.name;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public FabricClient getFabClient() {
		return this.fabClient;
	}

	public ChannelClient(String name, Channel channel, FabricClient fabClient) {
		this.name = name;
		this.channel = channel;
		this.fabClient = fabClient;
	}

	public Collection<ProposalResponse> queryByChainCode(String chaincodeName, String functionName, String[] args)
			throws InvalidArgumentException, ProposalException {
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
				"Querying " + functionName + " on channel " + this.channel.getName());
		QueryByChaincodeRequest request = this.fabClient.getInstance().newQueryProposalRequest();
		ChaincodeID ccid = ChaincodeID.newBuilder().setName(chaincodeName).build();
		request.setChaincodeID(ccid);
		request.setFcn(functionName);
		if (args != null) {
			request.setArgs(args);
		}

		Collection<ProposalResponse> response = this.channel.queryByChaincode(request);
		return response;
	}

	public Collection<ProposalResponse> sendTransactionProposal(TransactionProposalRequest request)
			throws ProposalException, InvalidArgumentException {
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
				"Sending transaction proposal on channel " + this.channel.getName());
		Collection<ProposalResponse> response = this.channel.sendTransactionProposal(request, this.channel.getPeers());
		Iterator var3 = response.iterator();

		while (var3.hasNext()) {
			ProposalResponse pres = (ProposalResponse) var3.next();
			String stringResponse = new String(pres.getChaincodeActionResponsePayload());
			Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
					"Transaction proposal on channel " + this.channel.getName() + " " + pres.getMessage() + " "
							+ pres.getStatus() + " with transaction id:" + pres.getTransactionID());
			Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO, stringResponse);
		}

		CompletableFuture<TransactionEvent> cf = this.channel.sendTransaction(response);
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO, cf.toString());
		return response;
	}

	public Collection<ProposalResponse> instantiateChainCode(String chaincodeName, String version, String chaincodePath,
			String language, String functionName, String[] functionArgs, String policyPath)
			throws InvalidArgumentException, ProposalException, ChaincodeEndorsementPolicyParseException, IOException {
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
				"Instantiate proposal request " + chaincodeName + " on channel " + this.channel.getName()
						+ " with Fabric client " + this.fabClient.getInstance().getUserContext().getMspId() + " "
						+ this.fabClient.getInstance().getUserContext().getName());
		InstantiateProposalRequest instantiateProposalRequest = this.fabClient.getInstance()
				.newInstantiationProposalRequest();
		instantiateProposalRequest.setProposalWaitTime(180000L);
		Builder chaincodeIDBuilder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(version)
				.setPath(chaincodePath);
		ChaincodeID ccid = chaincodeIDBuilder.build();
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
				"Instantiating Chaincode ID " + chaincodeName + " on channel " + this.channel.getName());
		instantiateProposalRequest.setChaincodeID(ccid);
		if (language.equals(Type.GO_LANG.toString())) {
			instantiateProposalRequest.setChaincodeLanguage(Type.GO_LANG);
		} else {
			instantiateProposalRequest.setChaincodeLanguage(Type.JAVA);
		}

		instantiateProposalRequest.setFcn(functionName);
		instantiateProposalRequest.setArgs(functionArgs);
		Map<String, byte[]> tm = new HashMap();
		tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(StandardCharsets.UTF_8));
		tm.put("method", "InstantiateProposalRequest".getBytes(StandardCharsets.UTF_8));
		instantiateProposalRequest.setTransientMap(tm);
		if (policyPath != null) {
			ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
			chaincodeEndorsementPolicy.fromYamlFile(new File(policyPath));
			instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
		}

		Collection<ProposalResponse> responses = this.channel.sendInstantiationProposal(instantiateProposalRequest);
		CompletableFuture<TransactionEvent> cf = this.channel.sendTransaction(responses);
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
				"Chaincode " + chaincodeName + " on channel " + this.channel.getName() + " instantiation " + cf);
		return responses;
	}

	public TransactionInfo queryByTransactionId(String txnId) throws ProposalException, InvalidArgumentException {
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
				"Querying by trasaction id " + txnId + " on channel " + this.channel.getName());
		Collection<Peer> peers = this.channel.getPeers();
		Iterator var3 = peers.iterator();
		if (var3.hasNext()) {
			Peer peer = (Peer) var3.next();
			TransactionInfo info = this.channel.queryTransactionByID(peer, txnId);
			return info;
		} else {
			return null;
		}
	}
}