'use strict';

angular.module('worldcup').factory('Auth', function ($rootScope, $state, $http) {

    var auth = {};

    var Account = {};

    var getAccount = function() {
        return $http.get("api/account/identity").then(function(response) {
            return  response.data;
        });
    };

    /**
     *  Saves the current user in the root scope
     *  Call this in the app run() method
     */
    auth.init = function () {
        getAccount().then(function(response) {
            Account = {
                authenticate: true,
                roles: response.roles,
                displayName: response.fullName,
                imageUrl: response.profileImageUrl,
                provider: undefined
            };

            if (auth.isLoggedIn()) {
                $rootScope.Account = Account;
            }
        });
    };

    auth.checkPermissionForView = function (view) {
        if (!view.permissions || !view.permissions.length) {
            return true;
        }
        return userHasPermissionForView(view);
    };


    var userHasPermissionForView = function (view) {
        if (!auth.isLoggedIn()) {
            return false;
        }

        if (!view.permissions || !view.permissions.length) {
            return true;
        }

        return auth.userHasPermission(view.permissions);
    };


    auth.userHasPermission = function (permissions) {
        if (!auth.isLoggedIn()) {
            return false;
        }

        var found = false;

        angular.forEach(permissions, function (permission, index) {
            if (Account.roles.indexOf(permission) >= 0) {
                found = true;
                return;
            }
        });

        return found;
    };


    auth.isLoggedIn = function () {
        return (Account && Account.roles && Account.roles.length);
    };

    auth.socialLogin = function (provider) {
        console.log(provider);
        $state.go('login', {'provider': provider});
    };

    auth.currentAccount = function () {
        return Account;
    };

    auth.logout = function () {
        window.location.href = '/signout';
    };

    return auth;
});