'use strict';

angular.module('worldcup').controller('betsCtrl', function ($rootScope, $scope, $state, $stateParams, $http) {


    $http({
        method : "GET",
        url : "api/bets/mine"
    }).then(function mySuccess(response) {
        if(response && response.data) {
           console.log(response.data);
        }
    }, function error(error) {
        console.log(error);
    });


});