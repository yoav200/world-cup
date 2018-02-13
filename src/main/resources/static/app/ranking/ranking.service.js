'use strict';

angular.module('worldcup').factory('Ranking', function($http) {

    var getLeaderboard = function() {
        return $http.get("api/ranking/").then(function(response) {
            return  response.data;
        });
    };

    return {
        getLeaderboard : getLeaderboard
    };

});