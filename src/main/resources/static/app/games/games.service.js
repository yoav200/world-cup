'use strict';

angular.module('worldcup').factory('Matches', function($http) {


    var getMatchesData = function() {
        return $http.get("api/match/data/").then(function(response) {
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
        return $http.post("api/match/groups/" + result.matchId, result).then(function(response) {
            return  response.data;
        });
    };

    return {
        getMatchesData: getMatchesData,
        getFirstStageMatches: getFirstStageMatches,
        getSecondStageMatches: getSecondStageMatches,
        updateStageMatch: updateStageMatch
    };

});