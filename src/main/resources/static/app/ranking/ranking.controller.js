'use strict';

angular.module('worldcup').controller('LBCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter, Ranking) {

    $scope.rankingData = [];

    var init = function() {
        Ranking.getLeaderboard().then(function(response) {
            $scope.rankingData = response;
        });
    };

    init();
});