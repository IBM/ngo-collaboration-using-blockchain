var express = require('express');
var router = express.Router();
var util = require('util');
const http = require('http');
var config = require("../config");
var Request = require("request");


/* GET home page. */
router.get('/', function (req, res, next) {

  var paramMap = makeParamMap(req.url);
  var paramLength = Object.keys(paramMap).length;

  if (paramLength > 1) {
    paramMap['start_date'] = getTodayDate();
    paramMap['uname'] = paramMap.ngo;

    var url = config.rest_base_url + "/CreateNeedServlet";
    console.log("Create Need payload = " + JSON.stringify(paramMap));
      Request.post({
        url: url,
        body: paramMap,
        json: true
      }, function (error, response, body) {
        if (error) {
          return console.dir(error);
        }
        res.render('createneed', { result: body });
      });

  }else{
    res.render('createneed', { result: 'Create Need' });
  }
});



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