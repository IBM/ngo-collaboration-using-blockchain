var express = require('express');
var router = express.Router();
var Request = require("request");

/* GET dashboard page. */
router.get('/', function (req, res, next) {
  //var data={};
  var data= [{"id":4, "created_by": "User1", "created_date": "20-Oct-2018", "category": "Cat1", "quantity_required": 200, "quantity_remaining": 10, "expected_date_of_fullfillment": "30-Nov-2018"},
  {"id":5, "created_by": "User2", "created_date": "11-Oct-2018", "category": "Cat2", "quantity_required": 150, "quantity_remaining": 0, "expected_date_of_fullfillment": "25-Nov-2018"},
  {"id":6, "created_by": "User1", "created_date": "18-Oct-2018", "category": "Cat3", "quantity_required": 300, "quantity_remaining": 50, "expected_date_of_fullfillment": "28-Nov-2018"}
  ];
/*
  Request.get("http://localhost:8000/rest/allneeds", (error, response, body) => {
    if (error) {
      return console.dir(error);
    }
    data = JSON.parse(body);
    console.dir(data);
    res.render('dashboard', { needs: data });
  });
  */
 res.render('pastneeds', { needs: data });
});

module.exports = router;
