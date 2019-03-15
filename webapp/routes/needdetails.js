var express = require('express');
var router = express.Router();
var Request = require("request");
var config = require("../config");


/* GET home page. */
router.get('/', function (req, res, next) {

  var paramMap = makeParamMap(req.url);
  var paramLength = Object.keys(paramMap).length;
  var uname = req.url.substring(2).split('=')[1];

  var url = config.rest_base_url + "/QueryAllPledgesForNeedServlet";
  var payload = { "uname": uname, "needId": paramMap["needid"] };
  console.log("payload = " + payload);

  Request.post({
    url: url,
    body: payload,
    json: true
  }, function (error, response, body) {
    if (error) {
      return console.dir(error);
    }
    res.render('needdetails', { pledgesdata: body});
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