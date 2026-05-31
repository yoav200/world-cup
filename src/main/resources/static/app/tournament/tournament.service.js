'use strict';

angular.module('worldcup').factory('Tournament', function($http, $q) {

    var cachedConfig = null;

    var getConfig = function() {
        if (cachedConfig) {
            return $q.when(cachedConfig);
        }
        return $http.get("api/tournament/config").then(function(response) {
            cachedConfig = response.data;
            return cachedConfig;
        });
    };

    return {
        getConfig: getConfig
    };

});
