'use strict';

angular.module('worldcup').controller('adminCtrl', function ($rootScope, $scope, $state, $stateParams, growl, Matches) {

    $scope.matchesData = {};

    $scope.selected = {
        match: undefined
    };
    
    $scope.selected.match = undefined;

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
            if ($scope.selected.match.matchId === match.matchId) {
                //$scope.selected.match = match;

                $scope.matchResult = {
                    matchId: $scope.selected.match.matchId,
                    homeTeamCode: $scope.selected.match.homeTeam.code,
                    awayTeamCode: $scope.selected.match.awayTeam.code,
                    homeTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.homeTeamGoals : undefined,
                    awayTeamGoals: $scope.selected.match.result ? $scope.selected.match.result.awayTeamGoals : undefined,
                    matchQualifier: $scope.selected.match.result ? $scope.selected.match.result.matchQualifier : undefined
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