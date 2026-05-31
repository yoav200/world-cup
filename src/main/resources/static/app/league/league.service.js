'use strict';

angular.module('worldcup').factory('Leagues', function($http) {

    var getMyLeagues = function() {
        return $http.get('api/leagues').then(function(response) {
            return response.data;
        });
    };

    var getCreatedLeagues = function() {
        return $http.get('api/leagues/created').then(function(response) {
            return response.data;
        });
    };

    var createLeague = function(name) {
        return $http.post('api/leagues', {name: name}).then(function(response) {
            return response.data;
        });
    };

    var updateLeague = function(leagueId, name) {
        return $http.put('api/leagues/' + leagueId, {name: name}).then(function(response) {
            return response.data;
        });
    };

    var deleteLeague = function(leagueId) {
        return $http.delete('api/leagues/' + leagueId);
    };

    var getMembers = function(leagueId) {
        return $http.get('api/leagues/' + leagueId + '/members').then(function(response) {
            return response.data;
        });
    };

    var addMember = function(leagueId, email) {
        return $http.post('api/leagues/' + leagueId + '/members', {email: email}).then(function(response) {
            return response.data;
        });
    };

    var removeMember = function(leagueId, membershipId) {
        return $http.delete('api/leagues/' + leagueId + '/members/' + membershipId);
    };

    return {
        getMyLeagues: getMyLeagues,
        getCreatedLeagues: getCreatedLeagues,
        createLeague: createLeague,
        updateLeague: updateLeague,
        deleteLeague: deleteLeague,
        getMembers: getMembers,
        addMember: addMember,
        removeMember: removeMember
    };
});
