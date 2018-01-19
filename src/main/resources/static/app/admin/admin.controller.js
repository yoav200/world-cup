'use strict';

angular.module('worldcup').controller('adminCtrl', function ($rootScope, $scope, $state, $stateParams, Matches) {

    console.log("Only admin can view this page");

    $scope.firstStage = [];
    $scope.secondStage = [];
    $scope.qualifiers = {};

    $scope.match = undefined;

    $scope.matchResult = {
        matchId: undefined,
        label: undefined,
        homeTeamGoals: undefined,
        awayTeamGoals: undefined
    };

    $scope.postResult = function () {
        Matches.updateStageMatch($scope.matchResult).then(function (response) {
            console.log(response);
        });
    };

    $scope.onMatchSelected = function (stage) {
        var list = stage === 'first' ? $scope.firstStage : $scope.secondStage;

        angular.forEach(list, function (match, index) {
            if ($scope.match.matchId === match.matchId) {
                $scope.match = match;
                $scope.matchResult = {
                    matchId: $scope.match.matchId,
                    homeTeamGoals: $scope.match.result ? $scope.match.result.homeTeamGoals : undefined,
                    awayTeamGoals: $scope.match.result ? $scope.match.result.awayTeamGoals : undefined
                };
            }
        });
    };

    Matches.getMatchesData().then(function (response) {
        console.log(response);
        $scope.firstStage = response.firstStage;
        $scope.secondStage = response.secondStage;
        $scope.qualifiers = response.qualifiers;
    });

});