'use strict';

angular.module('worldcup').controller('loginCtrl', function ($scope, Auth) {

    // If already logged in, redirect to home
    if (Auth.isLoggedIn()) {
        window.location.href = "/#/";
    }

    $scope.login = function () {
        Auth.login();
    };
});