var express = require('express');
var router = express.Router();
var Request = require("request");
var config = require("../config");

/* GET dashboard page. */
router.get('/', function (req, res, next) {
  var uname = req.url.substring(2).split('=')[1];
  var data={};
  var url = config.rest_base_url + "/QueryAllPastNeedsServlet";
  var body = { "uname": uname, "date": getTodayDate() };

  Request.post({
    url: url,
    body: body,
    json: true
  }, function (error, response, body) {
    if (error) {
      return console.dir(error);
    }
    res.render('pastneeds', { needs: body });
  });
});

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
