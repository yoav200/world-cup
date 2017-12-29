'use strict';

angular.module('worldcup').factory('Teams', function($http) {

    var getAllTeams = function() {
        return $http.get("api/teams/").then(function(response) {
            return  response.data;
        });
    };

    var getGroupsStanding = function() {
        return $http.get("api/groups/").then(function(response) {
            return  response.data;
        });
    };


    return {
        getAllTeams : getAllTeams,
        getGroupsStanding : getGroupsStanding
    };


});