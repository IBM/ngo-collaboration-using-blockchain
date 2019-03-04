## ** Work in Progress **

# NGOs collaboration using Blockchain - A Composite Pattern
**Demonstrate the use of Hyperledger Fabric for building a collaboration platform for NGOs.**

NGO is a non-profit organization that operates independently of any government and is a social voluntary organization of social activist, group of persons, community, volunteers and citizens who are associated for social welfare and social development. NGOs are usually funded by donations, but some avoid formal funding altogether and are run primarily by volunteers. Every NGO has its own mission and objectives.

When there is a person/organization in need, they approach NGO for help. But all the times, the requirement cannot be fulfilled by any one NGO or sometimes end user does not know which NGO to reach out for a specific requirement. In such hard times, NGOs should avoid competition and compromise and instead co-operate to achieve better results. But misunderstandings and collaboration without trust can prevent partnerships from reaching their potential.

Hence here is a blockchain based solution for NGO collaboration, in which NGOs with a diverse/same portfolios can come forward and be a part of consortium. When one approaches an NGO with some need (say books for education), that NGO can not help for this need or it can not be fulfilled by that NGO alone, then that NGO will create a new request in the network. All other NGOs whoever can contribute will update the request over the blockchain network. This network will give the holistic view of the requirement and its current status. This way a need is catered to in an efficient manner. The problems of over collection or under collection for a need is reduced. The platform brings in trust, accountability and transparency of operations between NGOs.

This composite pattern demonstrates a solution using a combination of other individual code patterns.

When the reader has completed this Code Pattern, they will understand how to:

- Setup blockchain Network on Kubernetes
- Interact with blockchain network using Fabric Java SDK
- Build a client application which will interact with blockchain network with the help of SDK

# Flow


# Watch the Video


# Pre-requisites

# Steps

Follow these steps to setup and run this code pattern. The steps are described in detail below.

1. [Get the code](#1-get-the-code)
2. [Deploy Hyperledger Fabric Network using Kubernetes on IBM Cloud](#2-deploy-hyperledger-fabric-network-using-kubernetes-on-ibm-cloud)
3. [Build the client based on Fabric Java SDK]()
4. [Deploy as REST APIs]()
5. [Build and deploy client app]()
6. [Analyze the Results]()

## 1. Get the code

- Clone the repo using the below command.
   ```
   git clone https://github.com/IBM/ngo-collaboration-using-blockchain
   ```

 - In this repository, 
    * [Network setup](https://github.com/IBM/ngo-collaboration-using-blockchain/tree/master/blockchain-network-on-kubernetes): configuration files and scripts to deploy Hyperledger Fabric network using Kubernetes on IBM Cloud.
    * [Integration code using Fabric Java SDK](https://github.com/IBM/ngo-collaboration-using-blockchain/tree/master/fabric-java-sdk-app)
    * [Client application code](https://github.com/IBM/ngo-collaboration-using-blockchain/tree/master/webapp)
    
    
## 2. Deploy Hyperledger Fabric Network using Kubernetes on IBM Cloud


# Troubleshooting

# Learn More

- [Track donations with Blockchain](https://developer.ibm.com/patterns/track-donations-blockchain/)

<!-- keep this -->
## License

[Apache 2.0](LICENSE)
