'use strict';

angular.module('worldcup').controller('navbarController', function ($rootScope, $scope, $state, $http, $interval, $location) {

    $rootScope.globals = {
        currentUser: {
            authenticate: false,
            role: undefined,
            displayName: undefined,
            imageUrl: undefined,
            provider: undefined
        }
    };


    var getIdentity = function () {
        return $http.get("api/account/identity").then(function (response) {
            if (response && response.data) {

                $rootScope.globals = {
                    currentUser: {
                        authenticate: true,
                        role: undefined,
                        displayName: response.data.fullName,
                        imageUrl: response.data.profileImageUrl,
                        provider: undefined
                    }
                };
            }
        });
    };


    $scope.logout = function () {
        window.location.href = '/signout';
    };

    $scope.socialLogin = function (provider) {
        console.log(provider);
        $state.go('login', {'provider': provider});
    };

    getIdentity();
});