'use strict';

angular.module('worldcup').controller('adminCtrl', function ($rootScope, $scope, $state, $stateParams, Matches) {

    console.log("Only admin can view this page")

    $scope.matches = [];

    $scope.match = undefined;

    $scope.matchResult = {
        matchId: undefined,
        label: undefined,
        homeTeamGoals: undefined,
        awayTeamGoals: undefined,
        qualifier: undefined
    };

    $scope.postResult = function() {
        Matches.updateStageMatch($scope.matchResult).then(function (response) {
            console.log(response);
        });
    };

    $scope.onMatchSelected = function() {
        angular.forEach($scope.matches, function (match, index) {
            if ($scope.match.matchId == match.matchId) {
                $scope.match = match;
                $scope.matchResult = {
                    matchId: $scope.match.matchId,
                    homeTeamGoals: $scope.match.result?$scope.match.result.homeTeamGoals:undefined,
                    awayTeamGoals: $scope.match.result?$scope.match.result.awayTeamGoals:undefined,
                    qualifier: $scope.match.result?$scope.match.result.matchQualifier:undefined
                };
                return;
            }
        });
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