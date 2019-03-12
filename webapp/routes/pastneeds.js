var express = require('express');
var router = express.Router();
var Request = require("request");
var config = require("../config");

/* GET dashboard page. */
router.get('/', function (req, res, next) {
  var uname = req.url.substring(2).split('=')[1];

  var data={};
  /*
  var data = [{ "id": 4, "created_by": "User1", "created_date": "20-Oct-2018", "category": "Cat1", "quantity_required": 200, "quantity_remaining": 10, "expected_date_of_fullfillment": "30-Nov-2018" },
  { "id": 5, "created_by": "User2", "created_date": "11-Oct-2018", "category": "Cat2", "quantity_required": 150, "quantity_remaining": 0, "expected_date_of_fullfillment": "25-Nov-2018" },
  { "id": 6, "created_by": "User1", "created_date": "18-Oct-2018", "category": "Cat3", "quantity_required": 300, "quantity_remaining": 50, "expected_date_of_fullfillment": "28-Nov-2018" }
  ];
  */
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

  var url = config.rest_base_url + "/QueryAllPastNeedsServlet";
  //var body = { "uname": uname, "date": "26-02-2019" };
  var body = { "uname": uname, "date": getTodayDate() };

  Request.post({
    url: url,
    body: body,
    json: true
  }, function (error, response, body) {
    if (error) {
      return console.dir(error);
    }
    console.log("Past needs body = " + body);
    /*
    console.log(typeof bobody);
    body.forEach(element => {
      data.push(element.Record);
    });
    */
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
