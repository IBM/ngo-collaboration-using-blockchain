## ** Work in Progress **

# NGOs collaboration using Blockchain - A Composite Pattern
**Demonstrate the use of Hyperledger Fabric for building a collaboration platform for NGOs.**

NGO is a non-profit organization that operates independently of any government and is a social voluntary organization of social activist, group of persons, community, volunteers and citizens who are associated for social welfare and social development. NGOs are usually funded by donations, but some avoid formal funding altogether and are run primarily by volunteers. Every NGO has its own mission and objectives.

When there is a person/organization in need, they approach NGO for help. But all the times, the requirement cannot be fulfilled by any one NGO or sometimes end user does not know which NGO to reach out for a specific requirement. In such hard times, NGOs should avoid competition and compromise and instead co-operate to achieve better results. But misunderstandings and collaboration without trust can prevent partnerships from reaching their potential.

Hence here is a blockchain based solution for NGO collaboration, in which NGOs with a diverse/same portfolios can come forward and be a part of consortium. When one approaches an NGO with some need (say books for education), that NGO can not help for this need or it can not be fulfilled by that NGO alone, then that NGO will create a new request in the network. All other NGOs whoever can contribute will update the request over the blockchain network. This network will give the holistic view of the requirement and its current status. This way a need is catered to in an efficient manner. The problems of over collection or under collection for a need is reduced. The platform brings in trust, accountability and transparency of operations between NGOs.

This composite pattern demonstrates a solution using a combination of other individual code patterns.
- [Deploy Hyperledger Fabric network on IBM Cloud](https://github.com/IBM/blockchain-network-on-kubernetes)
- [Create and deploy a blockchain network using Hyperledger Fabric SDK for Java](https://github.com/IBM/blockchain-application-using-fabric-java-sdk)

When the reader has completed this Code Pattern, they will understand how to:

- Setup blockchain Network on Kubernetes
- Interact with blockchain network using Fabric Java SDK
- Build a client application which will interact with blockchain network with the help of SDK

# Flow


# Watch the Video


# Pre-requisites

* [IBM Cloud Account](https://cloud.ibm.com)
* [Git Client](https://git-scm.com/downloads) - needed for clone commands

# Steps

Follow these steps to setup and run this code pattern. The steps are described in detail below.

1. [Get the code](#1-get-the-code)
2. [Deploy Hyperledger Fabric Network using Kubernetes on IBM Cloud](#2-deploy-hyperledger-fabric-network-using-kubernetes-on-ibm-cloud)
3. [Build the client application based on Fabric Java SDK](#3-build-the-client-application-based-on-fabric-java-sdk)
4. [Build and deploy webapp](#5-build-and-deploy-webapp)
5. [Analyze the Results](#5-analyze-the-results)

## 1. Get the code

- Clone the repo using the below command.
   ```
   git clone https://github.com/IBM/ngo-collaboration-using-blockchain
   ```

 - In this repository, 
    * [Network setup](https://github.com/IBM/ngo-collaboration-using-blockchain/tree/master/blockchain-network-on-kubernetes): configuration files and scripts to deploy Hyperledger Fabric network using Kubernetes on IBM Cloud.
    * [Client code using Fabric Java SDK](https://github.com/IBM/ngo-collaboration-using-blockchain/tree/master/fabric-java-sdk-app): application code built using Fabric Java SDK to invoke and query chaincode on the hyperledger fabric network. The operations are exposed as ReST APIs when deployed enabling other applications to consume.
    * [Web application code](https://github.com/IBM/ngo-collaboration-using-blockchain/tree/master/webapp): NodeJS based application code to render UI and integrates with the ReST APIs exposed by the client application built on Fabric Java SDK.
    
    
## 2. Deploy Hyperledger Fabric Network using Kubernetes on IBM Cloud

In this step, we will setup the Hyperledger Fabric network using Kubernetes. For detailed steps, please refer to the [Deploy blockchain network on Cloud](https://github.com/IBM/blockchain-network-on-kubernetes).

The scripts and configuration files to setup the network for this pattern can be located at `blockchain-network-on-kubernetes`. Perform the following steps to setup the network.

* Gain access of your Kubernetes cluster and ensure you are able to run `kubectl` commands properly.
* In case of IKS 1.11.x, modify the `configFiles/peersDeployment.yaml` file to point to a Docker service. Change instances of `unix:///host/var/run/docker.sock` to `tcp://docker:2375` with a text editor or use the commands below.
   ```
   ## macOS
   $ sed -i '' s#unix:///host/var/run/docker.sock#tcp://docker:2375# configFiles/peersDeployment.yaml

   ## Linux
   $ sed -i s#unix:///host/var/run/docker.sock#tcp://docker:2375# configFiles/peersDeployment.yaml
   ```
* Execute the following steps to setup the network.
   ```
   cd blockchain-network-on-kubernetes
   chmod +x setup_blockchainNetwork.sh
   ./setup_blockchainNetwork.sh
   ```
* If there is a need to delete the network, execute the following steps.
   ```
   $ chmod +x deleteNetwork.sh
   $ ./deleteNetwork.sh
   ```
   
## 3. Build the client application based on Fabric Java SDK

Here, we use the [Fabric Java SDK](https://github.com/hyperledger/fabric-sdk-java) to build a client to invoke and query chaincode on the hyperledger fabric network.

The java source code for the client can be found under `fabric-java-sdk-app` directory. The first step is to specify the public IP address of the Hyperledger deployment. The public IP address can be found on the `Kubernetes Dashboard`.

Open the file `ConfigNetwork.java` under `src/main/java/org/app/config` directory. Enter the IP address noted in the previous step for the constant string `IP` shown below, and save the file.

```
public static final String IP = "xxx.xxx.xx.xxx";
```

Next, on the command terminal go to `ngo-collaboration-using-blockchain` directory, and execute the below commands:
```
cd fabric-java-sdk-app
mvn clean install
ibmcloud cf push
```
Login to `IBM Cloud`. On the `Dashboard`, verify that an app `ngo-collaboration-java-app` is running fine. 

## 4. Build and deploy webapp

## 5. Analyze the Results

# Troubleshooting

# Learn More

- [Track donations with Blockchain](https://developer.ibm.com/patterns/track-donations-blockchain/)

<!-- keep this -->
## License

[Apache 2.0](LICENSE)
