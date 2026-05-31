'use strict';

angular.module('worldcup').controller('rankingCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter, Ranking, Leagues, Auth) {

    $scope.rankingData = [];
    $scope.leagues = [];
    $scope.selectedLeagueId = '';

    var loadRanking = function () {
        Ranking.getLeaderboard($scope.selectedLeagueId).then(function (response) {
            $scope.rankingData = response;
        });
    };

    $scope.onLeagueFilterChange = function () {
        loadRanking();
    };

    var init = function () {
        loadRanking();
        if (Auth.isLoggedIn()) {
            Leagues.getMyLeagues().then(function (leagues) {
                $scope.leagues = leagues;
            });
        }
    };

    init();
});