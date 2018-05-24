'use strict';

angular.module('worldcup').controller('betsQualifierCtrl', function ($rootScope, $scope, $state, $stateParams, Bets, growl, $uibModal, $log) {

    var getAllBets = function() {
        Bets.getAllBets().then(function (response) {

        });
    };

    var init = function() {
        getAllBets();
    };

    init();

});