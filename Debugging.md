* During setup of Blockchain network on Kubernetes, if you get following error:

  ```
  Error: could not assemble transaction, err proposal response was not successful, error code 500, msg error starting container: error starting container: cannot connect to Docker endpoint 
  ```
  
  It means you are using IKS 1.11.x or more but have not modified `blockchain-network-on-kubernetes/configFiles/peersDeployment.yaml` as mentioned in Step 2. Please perform the step as explained, network will be setup successfully.
  
* While running `kubectl` commands, if you get error as:

  ```
  The connection to the server localhost:8080 was refused - did you specify the right host or port?
  ```
  It means environment is not set properly. Export KUBECONFIG path, it will work. To get more details on KUBECONFIG value, refer to the `Access` tab of your cluster in IBM Cloud Dashboard.

* Incorrect IP address of kubernetes network in Java SDK app
* Incorrect url of Java SDK application in webapp
* Java app is not running
