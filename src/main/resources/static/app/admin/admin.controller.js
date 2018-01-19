'use strict';

angular.module('worldcup').controller('adminCtrl', function ($rootScope, $scope, $state, $stateParams, Matches) {

    console.log("Only admin can view this page");

    $scope.matchesData = {};

    $scope.selectedMatch = undefined;

    $scope.matchResult = {
        matchId: undefined,
        label: undefined,
        homeTeamGoals: undefined,
        awayTeamGoals: undefined,
        qualifier: undefined
    };

    Matches.getMatchesData().then(function (response) {
        $scope.matchesData = response;
    });

    $scope.postResult = function () {
        Matches.updateStageMatch($scope.matchResult).then(function (response) {
            console.log(response);
        });
    };

    $scope.onMatchSelected = function (stage) {
        var list = stage === 'first' ? $scope.matchesData.firstStage : $scope.matchesData.secondStage;

        angular.forEach(list, function (match, index) {
            if ($scope.selectedMatch.matchId === match.matchId) {
                $scope.selectedMatch = match;
                $scope.matchResult = {
                    matchId: $scope.selectedMatch.matchId,
                    homeTeamGoals: $scope.selectedMatch.result ? $scope.selectedMatch.result.homeTeamGoals : undefined,
                    awayTeamGoals: $scope.selectedMatch.result ? $scope.selectedMatch.result.awayTeamGoals : undefined
                };
            }
        });
    };



});