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

package org.example.user;

import java.util.Properties;

import org.example.client.CAClient;
import org.example.config.ConfigNetwork;
import org.example.util.Util;

public class RegisterEnrollUser {
	public static void main(String[] args) {
		try {
			Util.cleanUp();
			String caUrl = ConfigNetwork.CA_ORG1_URL;
			CAClient caClient = new CAClient(caUrl, (Properties) null);
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName("admin");
			adminUserContext.setAffiliation("org1");
			adminUserContext.setMspId("Org1MSP");
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser("admin", "adminpw");
			UserContext userContext = new UserContext();
			String name = "user" + System.currentTimeMillis();
			userContext.setName(name);
			userContext.setAffiliation("org1");
			userContext.setMspId("Org1MSP");
			String eSecret = caClient.registerUser(name, "org1");
			caClient.enrollUser(userContext, eSecret);
		} catch (Exception var7) {
			var7.printStackTrace();
		}

	}
}