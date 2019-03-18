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

package org.app.config;

/**
 * 
 * @author bkadambi
 *
 */
public class ConfigNetwork {
	
	public static final String IP = System.getenv("KUBERNETES_IP_ADDRESS"); // IP address of the Hyperledger Fabric Network Deployment on Kubernetes
	
	public static final String ORG1_MSP = "Org1MSP";

	public static final String ORG1 = "org1";

	public static final String ORG2_MSP = "Org2MSP";

	public static final String ORG2 = "org2";

	public static final String ADMIN = "admin";

	public static final String ADMIN_PASSWORD = "adminpw";
	
	public static final String CA_ORG1_URL = "http://" + IP + ":30054";
	
	public static final String CA_ORG2_URL = "http://" + IP + ":30064";
	
	public static final String ORDERER_URL = "grpc://" + IP +  ":31010";
	
	public static final String ORDERER_NAME = "blockchain-orderer";
	
	public static final String CHANNEL_NAME = "channel1";
	
	public static final String ORG1_PEER_2 = "blockchain-org1peer2";
	
	public static final String ORG1_PEER_2_URL = "grpc://"+ IP +":30210";
	
	public static final String ORG1_PEER_1 = "blockchain-org1peer1";
	
	public static final String ORG1_PEER_1_URL = "grpc://"+ IP + ":30110";
	
    public static final String ORG2_PEER_2 = "blockchain-org2peer2";
	
	public static final String ORG2_PEER_2_URL = "grpc://" + IP + ":30410";
	
	public static final String ORG2_PEER_1 = "blockchain-org2peer1";
	
	public static final String ORG2_PEER_1_URL = "grpc://" + IP + ":30310";
	
	public static final String CHAINCODE_ROOT_DIR = "chaincode";
	
	public static final String CHAINCODE_1_NAME = "ngo";
		
	public static final String CHAINCODE_1_VERSION = "1.0";
	
}
