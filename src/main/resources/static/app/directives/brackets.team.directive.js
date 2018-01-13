'use strict';

angular.module('worldcup').directive('bracketsTeam', function () {
    return {
        scope: {
            match: '=data'
        },
        templateUrl: 'app/directives/view/brackets.team.template.html',
        link: function (scope, element, attributes) {

        }
    }
});

