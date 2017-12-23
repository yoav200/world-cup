'use strict';

angular.module('worldcup').controller('teamsCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter) {

    $rootScope.view = { section : ''};

    $scope.teams = [];
    $scope.groups = [];
    $scope.confederation = [];


    var parentState = 'teams',
        defaultChildState = '.groups';
    // If the parent state has been transitioned to, redirect to the default child.
    if($state.current.name.substr(-parentState.length) === parentState) {
        $state.go(defaultChildState);
    }

    $http({
        method : "GET",
        url : "teams/"
    }).then(function mySuccess(response) {
        if(response && response.data) {
            $scope.teams = response.data;
            createGroups(response.data);
            createConfederation(response.data);
        }
    }, function error(error) {
        console.log(error);
    });


    var createGroups = function(teams) {
        var list = [];
        angular.forEach(teams, function(team) {
            var valObj = {};
            valObj.name = team.name;
            valObj.groupId = team.groupId;
            valObj.confederation = team.confederation;
            valObj.code = team.code;
            valObj.played=0;
            valObj.won=0;
            valObj.draw=0;
            valObj.lost=0;
            valObj.goalsFor=0;
            valObj.goalsAgainst=0;
            valObj.goalsDiff=(valObj.goalsFor - valObj.goalsAgainst);
            valObj.points=0;
            valObj.knockoutTeamCode=0;
            list.push(valObj);
        });
        $scope.groups = createMapFromList(list, 'groupId');
    };

    var createConfederation = function(teams) {
        $scope.confederation = createMapFromList(teams, 'confederation');
    };


    var createMapFromList = function(objectList, property) {
       return objectList.reduce(function(map, obj) {
            var list = map[obj[property]] ? map[obj[property]] : [];
            list.push(obj);
            map[obj[property]] = list;
            return map;
        }, {});
    };


    var init = function() {
        var viewName = '';
        switch ($state.current.name) {
            case 'teams.groups' : viewName= "Groups"; break;
            case 'teams.fifaranking' : viewName= "FIFA Ranking"; break;
            case 'teams.confederation' : viewName= "Confederation"; break;
        }

        $rootScope.view = { section : viewName};
    };

    init();
});