'use strict';

angular.module('worldcup').factory('Bets', function($http) {


    var getGroupsStanding = function() {
        return $http.get("api/bets/groups/").then(function(response) {
            return  response.data;
        });
    };


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
        getGroupsStanding : getGroupsStanding,
        getMatchesData : getMatchesData,
        getAllBets : getAllBets,
        updateBet: updateBet
    };

});