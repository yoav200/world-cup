'use strict';

angular.module('worldcup').directive('matchLinks', function () {
    return {
        restrict: 'E',
        scope: {
            match: '=data'
        },
        templateUrl: 'app/directives/view/match.links.template.html',
        link: function (scope, element, attributes) { }
    }
});
