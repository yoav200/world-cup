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
        url : "account/identity"
    }).then(function mySuccess(response) {
        if(response && response.data) {
            $scope.user = {
                authenticate : true,
                displayName : response.data.fullName,
                imageUrl : response.data.profileImageUrl
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