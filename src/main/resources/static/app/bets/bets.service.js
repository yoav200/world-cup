'use strict';

angular.module('worldcup').factory('Bets', function($http) {

    var getAllBets = function() {
        return $http.get("api/bets/").then(function(response) {
            return  response.data;
        });
    };

    var getAllUserBets = function() {
        return $http.get("api/bets/").then(function(response) {
            return  response.data;
        });
    };


    var setUserBet = function(betId, userBet) {
        return $http.post("api/bets/" + betId, userBet).then(function(response) {
            return  response.data;
        });
    };

    return {
        getAllBets: getAllBets,
        getAllUserBets: getAllUserBets,
        setUserBet: setUserBet
    };

});