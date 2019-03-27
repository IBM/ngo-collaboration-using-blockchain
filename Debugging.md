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

* If there is connection related exception in the Liberty runtime logs, please check whether the IP address of the Kubernetes network has been specified correctly in the `manifest.yml` file as described in Step 3 (Build the client application based on Fabric Java SDK).

```
org.hyperledger.fabric_ca.sdk.exception.EnrollmentException: Url:http://xxx.xx.xx.xxx:30054, Failed to enroll user admin 
	at org.hyperledger.fabric_ca.sdk.HFCAClient.enroll(HFCAClient.java:518)
...
Caused by: org.apache.http.conn.HttpHostConnectException: Connect to xxx.xx.xx.xxx:30054 [/xxx.xx.xx.xxx] failed: Operation timed out (Connection timed out)
...
Caused by: java.net.ConnectException: Operation timed out (Connection timed out)
	at java.net.PlainSocketImpl.socketConnect(Native Method)
```

* During running the web application, if you get the following error:

```
This page isn’t working
```

It means that the url of the Java SDK application might not be right. Ensure that the Java SDK application url is provided correctly in config.js file of webapp. 

```
This site can’t be reached
```
It means that Java SDK application might not be running. Ensure that the Java SDK application is up and running. 
