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
                auth.logout();
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
            Account = {
                authenticate: true,
                id : response.id,
                roles: response.roles,
                displayName: response.fullName,
                imageUrl: response.profileImageUrl,
                provider: undefined
            };

            if (auth.isLoggedIn()) {
                $rootScope.Account = Account;
            }

            var stateName = $cookies.get("state_name");
            if(stateName) {
                $log.info("found state to goto", stateName);
                $cookies.remove("state_name");
                $state.go(stateName)
            }

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

    auth.socialLogin = function (provider) {
        console.log(provider);
        $state.go('login', {'provider': provider, view : callbackState});
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