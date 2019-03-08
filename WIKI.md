# Short Title

Facilitate collaboration among Non-Governmental Organizations using Blockchain



# Long Title

Facilitate collaboration among Non-Governmental Organizations to manage supply and demand of goods and materials using Blockchain


# Author


* [Shikha Maheshwari](https://www.linkedin.com/in/shikha-maheshwari) 
* [Muralidhar Chavan](https://www.linkedin.com/in/muralidhar-chavan-3335b638/) 
* [Balaji Kadambi](https://www.linkedin.com/in/balaji-kadambi-1519223/)


# URLs

### Github repo

> "Get the code": 
* https://github.com/IBM/ngo-collaboration-using-blockchain

### Other URLs

* Demo URL

TODO

# Summary

This code pattern demonstrates the use of Hyperledger Fabric Network for facilitating the collaboration between Non-Governmental Organizations(NGO) to manage the supply and demand of goods to be distributed to the needy. The supply and demand mismatch leads to over-supply or under-supply of goods and materials. During critical times, there is no system of trust to get correct information on the supply and demand requirement of goods. This is a common problem faced by NGOs. The Hyperledger network has been used in this pattern to bring about a transparent way of collaboration between NGOs to efficiently distribute goods and materials during normal situations as well as critical situations.

# Technologies

* [Blockchain](https://en.wikipedia.org/wiki/Blockchain): A blockchain is a digitized, decentralized, public ledger of all transactions in a network.

* [Java](https://en.wikipedia.org/wiki/Java_(programming_language)): Java is a general-purpose computer-programming language that is concurrent, class-based and object-oriented.

* [NodeJS](https://nodejs.org/en/): Node.js® is a JavaScript runtime built on Chrome's V8 JavaScript engine.


# Description

Non-Governmental Organization(NGO) is a non-profit organization that operates independently of any government, and is a social voluntary organization of social activist, group of persons, community, volunteers and citizens who are associated for social welfare and social development. NGOs are usually funded by donations, but some avoid formal funding altogether and are run primarily by volunteers. Every NGO has its own mission and objectives.

When there is a person/organization in need, they approach NGO for help. But all the times, the requirement cannot be fulfilled by any one NGO or sometimes end user does not know which NGO to reach out for a specific requirement. In such hard times, NGOs should avoid competition and compromise, and instead co-operate to achieve better results. But misunderstandings and collaboration without trust can prevent partnerships from reaching their potential.

Hence here is a blockchain based solution for NGO collaboration, in which NGOs with a diverse/same portfolios can come forward and be a part of consortium. When one approaches an NGO with some need (say books for education), that NGO can not help for this need or it can not be fulfilled by that NGO alone, then that NGO will create a new request in the network. All other NGOs whoever can contribute will update the request over the blockchain network. This network will give the holistic view of the requirement and its current status. This way a need is catered to in an efficient manner. The problems of over collection or under collection for a need is reduced. The platform brings in trust, accountability and transparency of operations between NGOs.

This composite pattern demonstrates a solution using a combination of other individual code patterns.
- [Deploy Hyperledger Fabric network on IBM Cloud](https://github.com/IBM/blockchain-network-on-kubernetes)
- [Create and deploy a blockchain network using Hyperledger Fabric SDK for Java](https://github.com/IBM/blockchain-application-using-fabric-java-sdk)

# Flow

![Architecture](https://github.com/IBM/ngo-collaboration-using-blockchain/blob/master/images/architecture.png)


# Instructions

> Find the detailed steps for this pattern in the [readme file](https://github.com/IBM/ngo-collaboration-using-blockchain/blob/master/README.md) 

The steps will show you how to:

1. Get the code
2. Deploy Hyperledger Fabric Network using Kubernetes on IBM Cloud
3. Build the client application based on Fabric Java SDK
4. Build and deploy webapp
5. Analyze the Results

# Components and services

* [Hyperledger Fabric](https://hyperledger-fabric.readthedocs.io/): Hyperledger Fabric is a platform for distributed ledger solutions underpinned by a modular architecture delivering high degrees of confidentiality, resiliency, flexibility and scalability.

* [Hyperledger Fabric Java SDK](https://github.com/hyperledger/fabric-sdk-java)

* [IBM Cloud Kubernetes Service](https://cloud.ibm.com/containers-kubernetes/catalog/cluster): IBM Kubernetes Service enables the orchestration of intelligent scheduling, self-healing, and horizontal scaling.

# Runtimes

* [Liberty for Java](https://console.bluemix.net/catalog/starters/liberty-for-java): Develop, deploy, and scale Java web apps with ease. IBM WebSphere Liberty Profile is a highly composable, ultra-fast, ultra-light profile of IBM WebSphere Application Server designed for the cloud.

* [SDK for Node.js](https://console.bluemix.net/catalog/starters/sdk-for-nodejs):Develop, deploy, and scale server-side JavaScript® apps with ease. The IBM SDK for Node.js™ provides enhanced performance, security, and serviceability.

# Related IBM Developer content

* [Deploy Hyperledger Fabric network on IBM Cloud](https://github.com/IBM/blockchain-network-on-kubernetes)
* [Create and deploy a blockchain network using Hyperledger Fabric SDK for Java](https://github.com/IBM/blockchain-application-using-fabric-java-sdk)
* [Track donations with Blockchain](https://developer.ibm.com/patterns/track-donations-blockchain/)

# Related links

* N/A

# Announcement

## Facilitate collaboration between organizations using Hyperledger 
Collaboration enables organizations to work together to achieve a common business purpose. In some scenarios of collaboration, there is no regulation or agreements to enforce the collaboration but there is still a need to ensure transparency and trust among the organizations. An example for this scenario are Non-Governmental Organizations. During times of need, all NGOs are interested in ensuring the goods, materials or services reach the people in need.


In this blog post we will:
* Describe what the new code pattern does.
* Provide a brief overview of Hyperledger.

This code pattern demonstrates the use of Blockchain to facilitate the collaboration between NGOs. A Hyperledger network is used to transparently share the details of demand(need) and supply(pledged) of goods and materials between NGOs. A client application built on Fabric Java SDK is used to invoke and query chaincode on Hyperledger network. The operations are exposed as ReST apis to enable integration with different User Interface clients. A web UI application is built on Node.JS for the end user.

Hyperledger is an open source collaborative effort created to advance cross-industry blockchain technologies for business use. This global collaboration is hosted by The Linux Foundation. Please refer to the article [Blockchain basics: Hyperledger Fabric](https://developer.ibm.com/articles/cl-blockchain-hyperledger-fabric-hyperledger-composer-compared/) for additional details.

This code pattern is not just applicable to collaboration between NGOs but also for collaboration between organizations with similar requirements.




