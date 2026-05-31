'use strict';

angular.module('worldcup').directive('team', function () {
    return {
        restrict: 'E',
        scope: {
            team: '=data'
        },
        template: '<span ng-if="team">'
                    + '<span title="Rank: {{team.fifaRanking}}" class="fi fi-{{team.isoCode}}" style="font-size: 1.5em; vertical-align: middle;"></span>&nbsp;&nbsp;'
                    + '{{team.name | capitalize }}'
                + '</span>',
    }
});

