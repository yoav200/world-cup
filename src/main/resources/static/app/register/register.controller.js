'use strict';

angular.module('worldcup').controller('registerCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter, growl, Teams) {

    $scope.registerData = {
        firstName: undefined,
        lastName: undefined,
        email: undefined,
        password: undefined,
        passwordConfirm: undefined
    };

    $scope.register = function () {
        return $http.post("api/registration", $scope.registerData).then(function (response) {
            console.log(response)
            growl.success('You are registered. Check your Inbox for confirmation.', {title: 'Success!'});
        }, function (error) {
            //
            console.log(error)
        });
    };


    var init = function() {

    };

    init();
});