'use strict';

angular.module('worldcup').controller('adminCtrl', function ($rootScope, $scope, $state, $stateParams, Matches) {

    console.log("Only admin can view this page")

    $scope.matches = [];
    $scope.selected = undefined;
    $scope.matchResult = {
        matchId: undefined,
        homeTeamGoals: undefined,
        homeTeam: {},
        awayTeam: {},
        awayTeamGoals: undefined,
        winner: undefined
    };

    $scope.onMatchSelected = function(event) {
        $scope.matchResult.matchId = $scope.selected.matchId;
    };

    Matches.getAllMatches().then(function (response) {
        $scope.matches = response;
    });

    var updateMatch = function() {
        Matches.updateStageMatch($scope.matchResult).then(function (response) {
            console.log(response);
        });
    }
});