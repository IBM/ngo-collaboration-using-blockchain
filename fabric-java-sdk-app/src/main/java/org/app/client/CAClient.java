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

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.app.user.UserContext;
import org.app.util.Util;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuite.Factory;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

/**
 * 
 * @author bkadambi
 *
 */
public class CAClient {
	String caUrl;
	Properties caProperties;
	HFCAClient instance;
	UserContext adminContext;

	public UserContext getAdminUserContext() {
		return this.adminContext;
	}

	public void setAdminUserContext(UserContext userContext) {
		this.adminContext = userContext;
	}

	public CAClient(String caUrl, Properties caProperties)
			throws MalformedURLException, IllegalAccessException, InstantiationException, ClassNotFoundException,
			CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException {
		this.caUrl = caUrl;
		this.caProperties = caProperties;
		this.init();
	}

	public void init()
			throws MalformedURLException, IllegalAccessException, InstantiationException, ClassNotFoundException,
			CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException {
		CryptoSuite cryptoSuite = Factory.getCryptoSuite();
		this.instance = HFCAClient.createNewInstance(this.caUrl, this.caProperties);
		this.instance.setCryptoSuite(cryptoSuite);
	}

	public HFCAClient getInstance() {
		return this.instance;
	}

	public UserContext enrollAdminUser(String username, String password) throws Exception {
		UserContext userContext = Util.readUserContext(this.adminContext.getAffiliation(), username);
		if (userContext != null) {
			Logger.getLogger(CAClient.class.getName()).log(Level.WARNING,
					"CA -" + this.caUrl + " admin is already enrolled.");
			return userContext;
		} else {
			Enrollment adminEnrollment = this.instance.enroll(username, password);
			this.adminContext.setEnrollment(adminEnrollment);
			Logger.getLogger(CAClient.class.getName()).log(Level.INFO, "CA -" + this.caUrl + " Enrolled Admin.");
			return this.adminContext;
		}
	}

	public String registerUser(String username, String organization) throws Exception {
		UserContext userContext = Util.readUserContext(this.adminContext.getAffiliation(), username);
		if (userContext != null) {
			Logger.getLogger(CAClient.class.getName()).log(Level.WARNING,
					"CA -" + this.caUrl + " User " + username + " is already registered.");
			return null;
		} else {
			RegistrationRequest rr = new RegistrationRequest(username, organization);
			String enrollmentSecret = this.instance.register(rr, this.adminContext);
			Logger.getLogger(CAClient.class.getName()).log(Level.INFO,
					"CA -" + this.caUrl + " Registered User - " + username);
			return enrollmentSecret;
		}
	}

	public UserContext enrollUser(UserContext user, String secret) throws Exception {
		UserContext userContext = Util.readUserContext(this.adminContext.getAffiliation(), user.getName());
		if (userContext != null) {
			Logger.getLogger(CAClient.class.getName()).log(Level.WARNING,
					"CA -" + this.caUrl + " User " + user.getName() + " is already enrolled");
			return userContext;
		} else {
			Enrollment enrollment = this.instance.enroll(user.getName(), secret);
			user.setEnrollment(enrollment);
			Util.writeUserContext(user);
			Logger.getLogger(CAClient.class.getName()).log(Level.INFO,
					"CA -" + this.caUrl + " Enrolled User - " + user.getName());
			return user;
		}
	}
}