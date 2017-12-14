'use strict';

angular.module('worldcup').factory('Users', function ($resource) {
    return $resource('/api/connect/facebook', {}, {
        'fetchProfile': {url: 'login/identity', method: 'GET'},
        'getFriends': {url: 'api/connect/facebook/friends', method: 'GET', isArray: true}
    });
});