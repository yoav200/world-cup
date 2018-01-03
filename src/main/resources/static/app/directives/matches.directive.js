'use strict';

angular.module('worldcup').directive('matches', function () {
    return {
        scope: {
            matches: '=data'
        },
        templateUrl: 'app/games/view/games.template.html',
        link: function (scope, element, attributes) {

        }
    }
});

