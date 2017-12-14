'use strict';

angular.module('worldcup').controller('loginController', function ($rootScope, $scope, $state, $stateParams) {

    $scope.provider = $stateParams.provider;

    $scope.form = {
        'action' : 'signin/' + $scope.provider
    };

    console.log($scope.form);



});