var express = require('express');
var router = express.Router();
var Request = require("request");

/* GET dashboard page. */
router.get('/', function (req, res, next) {
  //var data={};
  var data= [{"id":1, "created_by": "User1", "created_date": "20-Dec-2018", "category": "Cat1", "quantity_required": 100, "quantity_remaining": 50, "expected_date_of_fullfillment": "30-Jan-2019"},
  {"id":2, "created_by": "User2", "created_date": "15-Dec-2018", "category": "Cat2", "quantity_required": 500, "quantity_remaining": 100, "expected_date_of_fullfillment": "15-Jan-2019"},
  {"id":3, "created_by": "User1", "created_date": "10-Dec-2018", "category": "Cat3", "quantity_required": 300, "quantity_remaining": 250, "expected_date_of_fullfillment": "20-Jan-2019"}
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
 res.render('dashboard', { needs: data });
});

module.exports = router;
