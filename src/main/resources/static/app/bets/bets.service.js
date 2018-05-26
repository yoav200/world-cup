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

    var deleteUserBet = function(betId) {
        return $http.delete("api/bets/user/" + betId).then(function(response) {
            return  response.data;
        });
    };

    var getOverview = function() {
        return $http.get("api/bets/user/overview").then(function(response) {
            return  response.data;
        });
    };




    var getQualifiers = function() {
        return $http.get("api/bets/user/qualifiers").then(function(response) {
            return  response.data;
        });
    };

    var setQualifiers = function(data) {
        return $http.post("api/bets/user/qualifiers", data).then(function(response) {
            return  response.data;
        });
    };

    return {
        getMatchesData : getMatchesData,
        getAllBets : getAllBets,
        updateBet: updateBet,
        deleteUserBet : deleteUserBet,
        getOverview : getOverview,
        getQualifiers : getQualifiers,
        setQualifiers : setQualifiers
    };

});