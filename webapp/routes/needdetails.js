var express = require('express');
var router = express.Router();
var Request = require("request");

/* GET home page. */
router.get('/', function (req, res, next) {
    //var data={};
    var data=[
      { "pledgeid": 1, "qty": 10, "user": "user1" },
      { "pledgeid": 2, "qty": 20, "user": "user2" },
      { "pledgeid": 3, "qty": 22, "user": "user3" }
  ];
/*
    Request.get("http://localhost:8000/rest/needdetails", (error, response, body) => {
      if (error) {
        return console.dir(error);
      }
      data = JSON.parse(body);
      console.dir(data);
      res.render('needdetails', { pledgesdata: data });
    });
    */
   res.render('needdetails', { pledgesdata: data });
});

module.exports = router;