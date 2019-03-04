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

package org.app.client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.ChaincodeID.Builder;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuite.Factory;

/**
 * 
 * @author bkadambi
 *
 */
public class FabricClient {
	private HFClient instance;

	public HFClient getInstance() {
		return this.instance;
	}

	public FabricClient(User context) throws CryptoException, InvalidArgumentException, IllegalAccessException,
			InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		CryptoSuite cryptoSuite = Factory.getCryptoSuite();
		this.instance = HFClient.createNewInstance();
		this.instance.setCryptoSuite(cryptoSuite);
		this.instance.setUserContext(context);
	}

	public ChannelClient createChannelClient(String name) throws InvalidArgumentException {
		Channel channel = this.instance.newChannel(name);
		ChannelClient client = new ChannelClient(name, channel, this);
		return client;
	}

	public Collection<ProposalResponse> deployChainCode(String chainCodeName, String chaincodePath, String codepath,
			String language, String version, Collection<Peer> peers)
			throws InvalidArgumentException, IOException, ProposalException {
		InstallProposalRequest request = this.instance.newInstallProposalRequest();
		Builder chaincodeIDBuilder = ChaincodeID.newBuilder().setName(chainCodeName).setVersion(version)
				.setPath(chaincodePath);
		ChaincodeID chaincodeID = chaincodeIDBuilder.build();
		Logger.getLogger(FabricClient.class.getName()).log(Level.INFO,
				"Deploying chaincode " + chainCodeName + " using Fabric client "
						+ this.instance.getUserContext().getMspId() + " " + this.instance.getUserContext().getName());
		request.setChaincodeID(chaincodeID);
		request.setUserContext(this.instance.getUserContext());
		request.setChaincodeSourceLocation(new File(codepath));
		request.setChaincodeVersion(version);
		Collection<ProposalResponse> responses = this.instance.sendInstallProposal(request, peers);
		return responses;
	}
}