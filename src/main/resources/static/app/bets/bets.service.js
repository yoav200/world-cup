'use strict';

angular.module('worldcup').factory('Bets', function($http) {


    var getMatchesData = function() {
        return $http.get("api/bets/data").then(function(response) {
            return  response.data;
        });
    };

    var getAllBets = function() {
        return $http.get("api/bets/").then(function(response) {
            return  response.data;
        });
    };

    var updateBet = function(betId, userBet) {
        return $http.post("api/bets/user/" + betId, userBet).then(function(response) {
            return  response.data;
        });
    };

    return {
        getMatchesData : getMatchesData,
        getAllBets : getAllBets,
        updateBet: updateBet
    };

});