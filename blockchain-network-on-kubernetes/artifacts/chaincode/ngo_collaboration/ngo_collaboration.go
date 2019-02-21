/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * The smart contract for NGO Collaboration Solution
 */

package main

/* Imports
 * utility libraries for formatting, handling bytes, reading and writing JSON, and string manipulation
 * 2 specific Hyperledger Fabric specific libraries for Smart Contracts
 */
import (
	"bytes"
	"encoding/json"
	"fmt"
	"strconv"
	"strings"
	"unicode"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	sc "github.com/hyperledger/fabric/protos/peer"
)

// Define the Smart Contract structure
type SmartContract struct {
}

// Define the need structure. Structure tags are used by encoding/json library
type Need struct {
	NeedID string `json:"need_id"`
	CreatedBy string `json:"created_by"`
	CreatedDate string `json:"created_date"`
	NeedCategory string `json:"need_category"`
	RequiredQuantity int `json:"quantity"`
	PledgedQuantity int `json:"pledged"`
	PledgedIDs []string `json:"pledged_ids"`
	ExpectedFulfilmentDate string `json:"expected_fulfilment_date"`
	Status string `json:"need_status"`
}

type Pledge struct {
	PledgeID string `json:"pledge_id"`
	NeedID string `json:"need_id"`
	PledgedQuantity int `json:"pledged_quantity"`
	PledgedBy string `json:"pledged_by"`
	Status string `json:"status"`
}

/*
 * The Init method is called when the Smart Contract "disastermgmt" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function 
 */
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

/*
 * The Invoke method is called as a result of an application request to run the Smart Contract "disastermgmt"
 * The calling application program has also specified the particular smart contract function to be called, with arguments
 */
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {

	// Retrieve the requested Smart Contract function and arguments
	function, args := APIstub.GetFunctionAndParameters()
	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "queryNeed" {
		return s.queryNeed(APIstub, args)
	} else if function == "createNeed" {
		return s.createNeed(APIstub, args)
	} else if function == "createPledge" {
		return s.createPledge(APIstub, args)
	} else if function == "queryPledge" {
		return s.queryPledge(APIstub, args)
	} else if function == "queryAllActiveNeeds" {
		return s.queryAllActiveNeeds(APIstub, args)
	} else if function == "queryAllPastNeeds" {
	 	return s.queryAllPastNeeds(APIstub, args)
	} else if function == "queryAllPledgesForNeed" {
		return s.queryAllPledgesForNeed(APIstub, args)
	} else if function == "queryAllNeeds" {
		return s.queryAllNeeds(APIstub)
	}

	return shim.Error("Invalid Smart Contract function name.")
}

func (s *SmartContract) queryNeed(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	// if len(args) != 1 {
	// 	return shim.Error("Incorrect number of arguments. Expecting 1")
	// }

	needAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(needAsBytes)
}

func (s *SmartContract) createNeed(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	// if len(args) != 6 {
	// 	return shim.Error("Incorrect number of arguments. Expecting 6")
	// }
	var requiredQuantity int
	requiredQuantity, _ = strconv.Atoi(args[4])

	// var pledgedQuantity int
	// pledgedQuantity, _ = strconv.Atoi(args[5])

	//var need = Need{NeedID: args[0], CreatedBy: args[1], CreatedDate: args[2], NeedCategory: args[3], RequiredQuantity: requiredQuantity, PledgedQuantity: 0, PledgedIDs: ["test1"], ExpectedFulfilmentDate: args[5]}
	var need = Need{NeedID: args[0], CreatedBy: args[1], CreatedDate: args[2], NeedCategory: args[3], RequiredQuantity: requiredQuantity, PledgedQuantity: 0, ExpectedFulfilmentDate: args[5], Status: "Open"}

	needAsBytes, _ := json.Marshal(need)
	APIstub.PutState(args[0], needAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) queryPledge(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	// if len(args) != 1 {
	// 	return shim.Error("Incorrect number of arguments. Expecting 1")
	// }

	pledgeAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(pledgeAsBytes)
}

/*
 * Modify Need <-> Create Pledge
 */
func (s *SmartContract) createPledge(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	// if len(args) != 5 {
	// 	return shim.Error("Incorrect number of arguments. Expecting 5")
	// }
	var existingVal int
	var newVal int
	var pledgedQuantity int
	pledgedQuantity, _ = strconv.Atoi(args[2])

	needAsBytes, _ := APIstub.GetState(args[1])
	need := Need{}

	json.Unmarshal(needAsBytes, &need)

	existingVal = need.PledgedQuantity
	newVal = existingVal + pledgedQuantity

	if newVal > need.RequiredQuantity {
		return shim.Error("Pledged quantity is more than required. Hence could not pledge.")
	} else {
		need.PledgedQuantity = existingVal + pledgedQuantity

		var pledge = Pledge{PledgeID: args[0], NeedID: args[1], PledgedQuantity: pledgedQuantity, PledgedBy: args[3], Status: args[4]}

		pledgeAsBytes, _ := json.Marshal(pledge)
		APIstub.PutState(args[0], pledgeAsBytes)

		need.PledgedIDs = append(need.PledgedIDs, args[0])

		needAsBytes, _ = json.Marshal(need)
		APIstub.PutState(args[1], needAsBytes)

		return shim.Success(nil)
	}
}

func (s *SmartContract) queryAllActiveNeeds(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	startKey := "N1"
	endKey := "N99"

	var date1 []string
	var date2 []string
	var activeNeeds []Need

	resultsIterator, err := APIstub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	f := func(c rune) bool {
		return !unicode.IsLetter(c) && !unicode.IsNumber(c)
	}

	for resultsIterator.HasNext() {
		needDetails, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		//queryKeyAsStr := needDetails.Key
		queryValAsBytes := needDetails.Value

		var need Need
		json.Unmarshal(queryValAsBytes, &need)
		date1 = strings.FieldsFunc(need.ExpectedFulfilmentDate,f)
		date2 = strings.FieldsFunc(args[0],f)

		date1Date, _ := strconv.Atoi(date1[0])
		date1Month, _ := strconv.Atoi(date1[1])
		date1Year, _ := strconv.Atoi(date1[2])

		date2Date, _ := strconv.Atoi(date2[0])
		date2Month, _ := strconv.Atoi(date2[1])
		date2Year, _ := strconv.Atoi(date2[2])

		if date2Year > date1Year && need.Status == "Open" {
			activeNeeds = append(activeNeeds, need)
		} else if date2Year == date1Year && date2Month > date1Month && need.Status == "Open" {
			activeNeeds = append(activeNeeds, need)
		} else if date2Year == date1Year && date2Month == date1Month && date2Date <= date1Date && need.Status == "Open"{
			activeNeeds = append(activeNeeds, need)
		}
	}
	
	//change to array of bytes
	everythingAsBytes, _ := json.Marshal(activeNeeds) 
	return shim.Success(everythingAsBytes)
}

func (s *SmartContract) queryAllPastNeeds(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	startKey := "N1"
	endKey := "N99"

	var date1 []string
	var date2 []string
	var pastNeeds []Need

	resultsIterator, err := APIstub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	f := func(c rune) bool {
		return !unicode.IsLetter(c) && !unicode.IsNumber(c)
	}

	for resultsIterator.HasNext() {
		needDetails, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		//queryKeyAsStr := needDetails.Key
		queryValAsBytes := needDetails.Value

		var need Need
		json.Unmarshal(queryValAsBytes, &need)
		date1 = strings.FieldsFunc(need.ExpectedFulfilmentDate,f)
		date2 = strings.FieldsFunc(args[0],f)

		date1Date, _ := strconv.Atoi(date1[0])
		date1Month, _ := strconv.Atoi(date1[1])
		date1Year, _ := strconv.Atoi(date1[2])

		date2Date, _ := strconv.Atoi(date2[0])
		date2Month, _ := strconv.Atoi(date2[1])
		date2Year, _ := strconv.Atoi(date2[2])

		if date2Year < date1Year {
			pastNeeds = append(pastNeeds, need)
		} else if date2Year == date1Year && date2Month < date1Month {
			pastNeeds = append(pastNeeds, need)
		} else if date2Year == date1Year && date2Month == date1Month && date2Date > date1Date {
			pastNeeds = append(pastNeeds, need)
		}
	}

	//change to array of bytes
	everythingAsBytes, _ := json.Marshal(pastNeeds) 
	return shim.Success(everythingAsBytes)
}

func (s *SmartContract) queryAllNeeds(APIstub shim.ChaincodeStubInterface) sc.Response {

	startKey := "N1"
	endKey := "N99"

	resultsIterator, err := APIstub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- queryAllNeeds:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *SmartContract) queryAllPledgesForNeed(APIstub shim.ChaincodeStubInterface,  args []string) sc.Response {

	var pledges []Pledge
	var pledge1 Pledge
	needAsBytes, err := APIstub.GetState(args[0])

	if err != nil {
		return shim.Error(err.Error())
	}

	var need Need
	json.Unmarshal(needAsBytes, &need)

	var retrievedPledgedIDs []string
	retrievedPledgedIDs = need.PledgedIDs
	fmt.Printf("- retrievedPledgedIDs:\n%s\n", retrievedPledgedIDs)

	for i := 0; i < len(retrievedPledgedIDs); i++ {
		pledgedID := retrievedPledgedIDs[i]

		pledgeAsBytes, err2 := APIstub.GetState(pledgedID)

		if err2 != nil {
			return shim.Error(err.Error())
		}
		json.Unmarshal(pledgeAsBytes, &pledge1)
		pledges = append(pledges, pledge1)
	}
	fmt.Printf("- queryAllPledgesForANeed:\n%s\n", pledges)

	//change to array of bytes
	allPledgesAsBytes, _ := json.Marshal(pledges) 
	return shim.Success(allPledgesAsBytes)
}

// The main function is only relevant in unit test mode. Only included here for completeness.
func main() {

	// Create a new Smart Contract
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Printf("Error creating new Smart Contract: %s", err)
	}
}
