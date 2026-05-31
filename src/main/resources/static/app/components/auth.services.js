'use strict';

angular.module('worldcup').factory('Auth', function ($rootScope, $state, $http, $timeout, $cookies, $log) {

    var TIMEOUT_MILLIS = 1000 * 60 * 10; // 10 minutes

    var auth = {};

    var Account = {};

    /**
     * make heartbeat request to the server, every request
     */
    var heartBeat = function () {
        $http.post('api/heartbeat', Account).then(function (response) {
            if (!response.data.valid) {
                window.location.href = "/#/";
            }
            $timeout(heartBeat, TIMEOUT_MILLIS);
        });
    };

    var getAccount = function () {
        return $http.get("api/account/identity").then(function (response) {
            return response.data;
        });
    };

    /**
     *  Saves the current user in the root scope
     *  Call this in the app run() method
     */
    auth.init = function () {

        $log.info("Initiate authentication");

        getAccount().then(function (response) {
            if (response) {
                Account = {
                    authenticate: true,
                    id: response.id,
                    roles: response.roles,
                    displayName: response.fullName,
                    firstName: response.firstName,
                    lastName: response.lastName,
                    email: response.email,
                    imageUrl: response.profileImageUrl
                };

                if (auth.isLoggedIn()) {
                    $rootScope.Account = Account;
                }
            }
        }, function () {
            // Not authenticated - user will need to click Login
            $log.info("User not authenticated");
        });
        // start polling
        heartBeat();
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
            }
        });
        return found;
    };

    auth.isLoggedIn = function () {
        return (Account && Account.roles && Account.roles.length > 0);
    };

    auth.login = function () {
        window.location.href = '/oauth2/authorization/bny';
    };

    auth.currentAccount = function () {
        return Account;
    };

    auth.logout = function () {
        window.location.href = '/logout';
    };

    auth.setStateUrl = function(stateName) {
        $cookies.put("state_name", stateName);
    };

    return auth;
});