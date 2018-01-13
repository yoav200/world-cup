'use strict';

angular.module('worldcup').directive('matches', function () {
    return {
        scope: {
            matches: '=data'
        },
        templateUrl: 'app/directives/view/match.template.html',
        link: function (scope, element, attributes) {

        }
    }
});

