'use strict';

angular.module('worldcup').controller('betsCtrl', function ($rootScope, $scope, $state, $stateParams, Bets) {


    $scope.bets = [];

    Bets.getAllBets().then(function (response) {
        $scope.bets = response;
    });

});