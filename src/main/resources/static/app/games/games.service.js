'use strict';

angular.module('worldcup').factory('Matches', function($http) {

    var getAllMatches = function() {
        return $http.get("api/match/").then(function(response) {
            return  response.data;
        });
    };

    var getFirstStageMatches = function() {
        return $http.get("api/match/groups").then(function(response) {
            return  response.data;
        });
    };

    var getSecondStageMatches = function() {
        return $http.get("api/match/knockout").then(function(response) {
            return  response.data;
        });
    };

    var updateStageMatch = function(result) {
        return $http.post("api/match/knockout", result).then(function(response) {
            return  response.data;
        });
    };

    return {
        getAllMatches: getAllMatches,
        getFirstStageMatches: getFirstStageMatches,
        getSecondStageMatches: getSecondStageMatches,
        updateStageMatch: updateStageMatch
    };


});