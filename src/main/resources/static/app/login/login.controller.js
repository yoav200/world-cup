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
        console.log('error logging', error);
    };


    var init = function() {
        if($stateParams.token) {
            console.log("token in request:", $stateParams.token);
            return $http({
                url: "api/registration/confirm",
                method: "GET",
                params: {"token": $stateParams.token}
             }).then(function (response) {
                console.log(response)
                growl.success('You email has confirm, you can login.', {title: 'Success!'});
            });
        }
    };

    init();
});