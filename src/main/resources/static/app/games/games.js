'use strict';

angular.module('worldcup').factory('Matches', function($http) {

    var getFirstStageMatches = function() {
        return $http.get("match/groups").then(function(response) {
            return  response.data;
        });
    };

    var getSecondStageMatches = function() {
        return $http.get("match/knockout").then(function(response) {
            return  response.data;
        });
    };

    return {
        getFirstStageMatches: getFirstStageMatches,
        getSecondStageMatches: getSecondStageMatches
    };


});