var express = require('express');
var router = express.Router();
var util = require('util');
const http = require('http');
var config = require("../config");


/* GET home page. */
router.get('/', function (req, res, next) {

  var paramMap = makeParamMap(req.url);
  var paramLength = Object.keys(paramMap).length;

  if (paramLength > 1) {
    paramMap['needId'] = 'N3'; // TODO change this when interface changes
    paramMap['start_date'] = getTodayDate(); // TODO check if this is right

    var postresult = postCreateNeed(paramMap);
    res.render('createneed', { result: postresult });
  }else{
    res.render('createneed', { result: 'Create Need' });
  }
});

function postCreateNeed(data) {

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

  req.write(JSON.stringify(data));
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