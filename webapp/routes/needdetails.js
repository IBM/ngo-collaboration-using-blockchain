var express = require('express');
var router = express.Router();
var Request = require("request");

/* GET home page. */
router.get('/', function (req, res, next) {
    var data={};

    Request.get("http://localhost:8000/rest/needdetails", (error, response, body) => {
      if (error) {
        return console.dir(error);
      }
      data = JSON.parse(body);
      console.dir(data);
      res.render('needdetails', { pledgesdata: data });
    });
});

module.exports = router;