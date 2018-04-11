'use strict';

angular.module('worldcup').controller('adminCtrl', function ($rootScope, $scope, $state, $stateParams, growl, Matches) {

    console.log("Only admin can view this page");

    $scope.matchesData = {};

    $scope.selectedMatch = undefined;

    $scope.matchResult = {
        matchId: undefined,
        label: undefined,
        homeTeamCode: undefined,
        awayTeamCode: undefined,
        homeTeamGoals: undefined,
        awayTeamGoals: undefined,
        matchQualifier: undefined
    };

    var getMatchData = function() {
        Matches.getMatchesData().then(function (response) {
            $scope.matchesData = response;
        });
    };

    $scope.postResult = function () {
        Matches.updateStageMatch($scope.matchResult).then(function (response) {
            getMatchData();
            growl.success('Game result saved successfully.',{title: 'Game result saved!'});
        });
    };

    $scope.onMatchSelected = function (stage) {
        var list = stage === 'first' ? $scope.matchesData.firstStage : $scope.matchesData.secondStage;

        angular.forEach(list, function (match, index) {
            if ($scope.selectedMatch.matchId === match.matchId) {
                $scope.selectedMatch = match;

                $scope.matchResult = {
                    matchId: $scope.selectedMatch.matchId,
                    homeTeamCode: $scope.selectedMatch.homeTeam.code,
                    awayTeamCode: $scope.selectedMatch.awayTeam.code,
                    homeTeamGoals: $scope.selectedMatch.result ? $scope.selectedMatch.result.homeTeamGoals : undefined,
                    awayTeamGoals: $scope.selectedMatch.result ? $scope.selectedMatch.result.awayTeamGoals : undefined,
                    matchQualifier: $scope.selectedMatch.result ? $scope.selectedMatch.result.matchQualifier : undefined
                };

                console.log($scope.matchResult);
            }
        });
    };


    var init = function() {
        getMatchData();
    };

    init();


});