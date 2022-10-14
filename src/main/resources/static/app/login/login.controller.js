'use strict';

angular.module('worldcup').controller('loginCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter, Teams, Auth, growl) {

    $scope.credentials = {
        username: undefined,
        password: undefined
    };

    var login = function (username, password) {
        return $http({
            method: 'POST',
            url: "login",
            data: "username=" + username + "&password=" + password,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
    };

    $scope.login = function () {
        login($scope.credentials.username, $scope.credentials.password)
            .success($scope.onLoginSuccess)
            .error($scope.onLoginError);
    };

    $scope.onLoginSuccess = function () {
        Auth.init();
        window.location.href = "/#/";
    };

    $scope.onLoginError = function (error) {
        //
    };


    var init = function() {

    };

    init();
});