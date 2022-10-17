'use strict';

angular.module('worldcup').controller('registerCtrl', function ($rootScope, $scope, $state, $stateParams, $http, $filter, growl, Teams, Auth) {

    $scope.registerData = {
        action: "Register",
        firstName: undefined,
        lastName: undefined,
        email: undefined,
        password: undefined,
        passwordConfirm: undefined
    };

    $scope.submit = function () {
        if(Auth.isLoggedIn()) {
            // user is logged -n - update details
            return $http.put("api/account", $scope.registerData).then(function (response) {
                //console.log(response)
                growl.success('Account details updated.', {title: 'Success!'});
            });
        } else if($stateParams.token) {
            // user not logged-in and token exists - change password
            return $http.put("api/registration/change-password", $scope.registerData).then(function (response) {
                //console.log(response)
                growl.success('Account details updated.', {title: 'Success!'});
            });
        } else {
            // register new user
            return $http.post("api/registration", $scope.registerData).then(function (response) {
                //console.log(response)
                growl.success('Registration complete!. Check your Inbox for confirmation.', {title: 'Success!'});
            });
        }
    };

     $scope.sendToken =  function () {
        return $http({
            url: "api/registration/email-validation",
            method: "GET",
            params: {"email": $scope.registerData.email}
        }).then(function (response) {
            //console.log(response)
            growl.success('You email has confirm, you can login.', {title: 'Success!'});
        });
     };

    var init = function() {
        if(Auth.isLoggedIn()) {
            //console.log("user is logged:", $rootScope.Account);
            // user is logged in
            $scope.registerData = {
                action: "Update Details",
                firstName: $rootScope.Account.firstName,
                lastName: $rootScope.Account.lastName,
                email: $rootScope.Account.email,
                password: undefined,
                passwordConfirm: undefined
            };
        } else if($stateParams.token) {
            // a token was sent, check for who has this token and update data
            //console.log("token in request:", $stateParams.token);

            return $http({
                url: "api/registration/token",
                method: "GET",
                params: {"token": $stateParams.token}
            }).then(function (response) {
                //console.log(response)
                $scope.registerData = {
                    action: "Update Details",
                    firstName: response.data.firstName,
                    lastName: response.data.lastName,
                    email: response.data.email,
                    password: undefined,
                    passwordConfirm: undefined,
                    token: $stateParams.token
                };
                growl.success('Valid token found, you can update details.', {title: 'Success!'});
            });
        }
    };

    init();
});