'use strict';

angular.module('worldcup').controller('navbarController', function ($rootScope, $scope, $state, $http, Auth) {

    $scope.logout = Auth.logout;

    $scope.socialLogin = Auth.socialLogin;

});