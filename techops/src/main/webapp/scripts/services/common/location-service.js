/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['../../utils/constant'], function (constant) {
  /**
   * A module representing a login controller.
   * @exports controllers/login
   */
  function Service() {
    return {
      getProvince: function () {
        return {
          respCode: '_200',
          result: Object.keys(constant.CITY_GROUP)
        }
      },
      getCity: function (province) {
        return {
          respCode: '_200',
          result: constant.CITY_GROUP[province]
        }
      }

    }
  }

  return {
    name: "LocationService",
    svc: ["$resource", Service]
  }


});
