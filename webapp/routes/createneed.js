var express = require('express');
var router = express.Router();
var util = require('util');
const http = require('http');
var config = require("../config");


/* GET home page. */
router.get('/', function (req, res, next) {
  //console.log("CreateNeed Req = " + JSON.stringify(req));
  //console.log("URL = " + util.inspect(req.url))
  var paramMap = makeParamMap(req.url);
  console.log("1");
  var paramLength = Object.keys(paramMap).length;
  console.log("2" + paramLength);

  if (paramLength > 1) {
    paramMap['needId'] = 'N3'; // TODO change this when interface changes
    paramMap['start_date'] = getTodayDate(); // TODO check if this is right

    var postresult = postCreateNeed(paramMap);
    res.render('createneed', { result: postresult });
  }else{
    res.render('createneed', { result: 'Create Need' });
  }
});

function postCreateNeed(dataTemp) {

  var data = JSON.stringify(dataTemp);
  //var data = dataTemp;
  //var data = '{"ngo":"u","item":"Food","qty":"10000","end_date":"10-01-2021","needId":"N3","start_date":"08-03-2019"}';
  const options = {
    hostname: config.rest_hostname,
    port: 80,
    path: '/CreateNeedServlet',
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  }

  const req = http.request(options, (res) => {
    console.log(`statusCode: ${res.statusCode}`);

    res.on('data', (d) => {
      console.log("create need response data = " + d);
    });
  });

  req.on('error', (error) => {
    console.error(error);
  });

  req.write(data);
  req.end();
  return "Need creation request submitted successfully";
}

function makeParamMap(uri) {
  var paramMap = {};
  uri = uri.substring(2);
  var params = uri.split('&');
  console.log("params = " + params);
  console.log("params length = " + params.length);
  for (var i = 0; i < params.length; i++) {
    var pair = params[i].split('=');
    if (pair[0] === 'username') {
      paramMap['ngo'] = pair[1];
      delete paramMap['username'];
    } else {
      paramMap[pair[0]] = pair[1];
    }
  }
  return paramMap;
}

function getTodayDate() {
  var today = new Date();
  var dd = today.getDate();
  var mm = today.getMonth() + 1; //January is 0!
  var yyyy = today.getFullYear();

  if (dd < 10) {
    dd = '0' + dd
  }

  if (mm < 10) {
    mm = '0' + mm
  }

  today = dd + '-' + mm + '-' + yyyy;
  return today;

}

module.exports = router;