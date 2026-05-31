'use strict';

angular.module('worldcup').factory('Ranking', function($http) {

    var getLeaderboard = function(leagueId) {
        var params = {};
        if (leagueId) {
            params.leagueId = leagueId;
        }
        return $http.get("api/ranking/", {params: params}).then(function(response) {
            return response.data;
        });
    };

    return {
        getLeaderboard : getLeaderboard
    };

});