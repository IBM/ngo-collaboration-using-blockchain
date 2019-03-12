var express = require('express');
var router = express.Router();
var Request = require("request");
var config = require("../config");
const http = require('http');

/* GET dashboard page. */
router.get('/', function (req, res, next) {

  var paramMap = makeParamMap(req.url);
  var paramLength = Object.keys(paramMap).length;
  var uname = req.url.substring(2).split('=')[1];
  var pledgeStatus = null;


  var data = [];
  var url = config.rest_base_url + "/QueryAllNeedsServlet";
  var payload = { "uname": uname };

  Request.post({
    url: url,
    body: payload,
    json: true
  }, function (error, response, body) {
    if (error) {
      return console.dir(error);
    }
    body.forEach(element => {
      data.push(element.Record);
    });
    if (paramLength > 1) { // it is a create pledge
      paramMap['status'] = 'Pledged';
      paramMap['ngo'] = paramMap['uname'];
  
      const options = {
        hostname: config.rest_hostname,
        port: 80,
        path: '/CreatePledgeServlet',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        }
      }
  
      const req = http.request(options, (res) => {
        console.log(`statusCode: ${res.statusCode}`);
  
        res.on('data', (d) => {
          console.log("Pledge rest response data = " + JSON.stringify(d));
        });
      });
  
      req.on('error', (error) => {
        console.error(error);
      });
  
      req.write(JSON.stringify(paramMap));
      req.end();
      res.render('dashboard', { needs: data, pledgeStatus: 'Pledge creation request submitted successfully' });
    }else{
      res.render('dashboard', { needs: data, pledgeStatus: '' });
    }
  });




});

function makeParamMap(uri) {
  var paramMap = {};
  uri = uri.substring(2);
  var params = uri.split('&');
  for (var i = 0; i < params.length; i++) {
    var pair = params[i].split('=');
    if (pair[0] === 'username') {
      paramMap['uname'] = pair[1];
      delete paramMap['username'];
    } else {
      paramMap[pair[0]] = pair[1];
    }
  }
  console.log("params = " + params);
  console.log("params length = " + params.length);
  return paramMap;
}

module.exports = router;
