var express = require('express');
var router = express.Router();
var Request = require("request");
var config = require("../config");


/* GET dashboard page. */
router.get('/', function (req, res, next) {

  var paramMap = makeParamMap(req.url);
  var paramLength = Object.keys(paramMap).length;
  var uname = req.url.substring(2).split('=')[1];
  var pledgeStatus = null;


  var data = [];
  var todatDate = getTodayDate();
  var url = config.rest_base_url + "/QueryAllActiveNeedsServlet";
  var payload = { "uname": uname, "date": todatDate};

  Request.post({
    url: url,
    body: payload,
    json: true
  }, function (error, response, body) {
    if (error) {
      return console.dir(error);
    }
    console.log("Body = " + JSON.stringify(body));

    if (!body) { // If there are no active needs available
      console.log("Body undefined");
      res.render('dashboard', { isData: false, pledgeStatus: '' });
    } else { // If there are active needs available
      body.forEach(element => {
        // TODO remove the hardcoded block below
        data.push(element);
        // if( element.Record.expected_fulfilment_date != '01-06-1979'){
        //   } 
      });
      
      if (paramLength > 1) { // it is a create pledge
        url = config.rest_base_url + "/CreatePledgeServlet";
        var payload = { "uname": paramMap.ngo, "needId": paramMap.needId, "qty": paramMap.qty, "ngo": paramMap.ngo, "status": "Pledged" };

        Request.post({
          url: url,
          body: payload,
          json: true
        }, function (error, response, body) {
          if (error) {
            return console.dir(error);
          }
          res.render('dashboard', { needs: data, pledgeStatus: body, isData: true });
        });
      } else {
        res.render('dashboard', { needs: body, pledgeStatus: '', isData: true });
      }
    }

  });

});


function postPledge(params) {

  var url = config.rest_base_url + "/CreatePledgeServlet";
  var payload = { "uname": params.ngo, "needId": params.needId, "qty": params.qty, "ngo": params.ngo, "status": "Pledged" };

  Request.post({
    url: url,
    body: payload,
    json: true
  }, function (error, response, body) {
    if (error) {
      return console.dir(error);
    }
    return body;
  });
}

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
