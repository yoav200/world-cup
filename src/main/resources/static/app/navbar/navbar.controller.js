'use strict';

angular.module('worldcup').controller('navbarController', function ($rootScope, $scope, $state, $http, $interval, $location) {


    $scope.user = {
      authenticate : false,
      displayName : undefined,
      imageUrl : undefined,
      provider : undefined
    };

    $http({
        method : "GET",
        url : "login/identity"
    }).then(function mySuccess(response) {
        console.log(response);
        if(response && response.data && response.data.key) {
            $scope.user = {
                authenticate : true,
                displayName : response.data.displayName,
                imageUrl : response.data.imageUrl,
                provider : response.data.key.providerId
            };
        }
    }, function error(error) {
        console.log(error);
    });

    $scope.logout = function() {
        window.location.href = '/signout';
    };

    $scope.socialLogin = function(provider) {
        console.log(provider);
        $state.go('login', {'provider':provider});
        //$location.url('/signup/' + provider);
        //window.location.href = '/signin/' + provider + '?code=publish_actions,user_photos,public_profile,email';
    }

});