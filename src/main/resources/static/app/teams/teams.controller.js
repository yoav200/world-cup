'use strict';

angular.module('worldcup').controller('teamsCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter, Teams) {

    $rootScope.view = { section : ''};

    $scope.teams = [];
    $scope.confederation = [];
    $scope.matches = [];

    var parentState = 'teams', defaultChildState = '.fifaranking';
    // If the parent state has been transitioned to, redirect to the default child.
    if($state.current.name.substr(-parentState.length) === parentState) {
        $state.go(defaultChildState);
    }

    var allTeams = function() {
        Teams.getAllTeams().then(function(response) {
            $scope.teams = response;
            createConfederation(response);
        });
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
            case 'teams.fifaranking' :
                viewName= "FIFA Ranking";
                allTeams();
                break;
            case 'teams.confederation' :
                viewName= "Confederation";
                allTeams();
                break;
        }
        $rootScope.view = { section : viewName};
    };

    init();
});