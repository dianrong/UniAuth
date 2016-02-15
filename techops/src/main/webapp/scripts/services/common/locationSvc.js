/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['utils/Constant'], function (Constant) {
  /**
   * A module representing a login controller.
   * @exports controllers/login
   */
  function Service() {
    return {
      getProvince: function () {
        return {
          respCode: '_200',
          result: Object.keys(Constant.CITY_GROUP)
        }
      },
      getCity: function (province) {
        return {
          respCode: '_200',
          result: Constant.CITY_GROUP[province]
        }
      }

    }
  }

  return {
    name: "LocationSvc",
    svc: ["$resource", Service]
  }


});
