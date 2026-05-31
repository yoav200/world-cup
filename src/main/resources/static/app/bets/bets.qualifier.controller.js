'use strict';


angular.module('worldcup').controller('betsQualifierCtrl', function ($rootScope, $scope, $interval, Bets, Teams, Tournament, growl) {

    // ==============   for countdown =====================

    $scope.locked = {
        isLocked: false,
        lockTime: undefined
    };

    // credit to: https://stackoverflow.com/questions/38108013/getting-time-left-countdown-html-javascript-angularjs
    $scope.CountDown = {
        getTimeRemaining: function (endtime) {
            var t = Date.parse(endtime) - Date.parse(new Date());
            var seconds = Math.floor((t / 1000) % 60);
            var minutes = Math.floor((t / 1000 / 60) % 60);
            var hours = Math.floor((t / (1000 * 60 * 60)) % 24);
            var days = Math.floor(t / (1000 * 60 * 60 * 24));
            return {
                'total': t,
                'days': days,
                'hours': hours,
                'minutes': minutes,
                'seconds': seconds
            };
        },

        initializeClock: function (endtime) {
            function updateClock() {
                var t = $scope.CountDown.getTimeRemaining(endtime);
                // expose to scope
                $scope.CountDown.days = t.days;
                $scope.CountDown.hours = ('0' + t.hours).slice(-2);
                $scope.CountDown.minutes = ('0' + t.minutes).slice(-2);
                $scope.CountDown.seconds = ('0' + t.seconds).slice(-2);

                if (t.total <= 0) {
                    $interval.cancel(timeInterval);
                }
            }

            updateClock();
            var timeInterval = $interval(updateClock, 1000);
        }
    };

    // ====================================================

    // model that is passed to server (populated dynamically from tournament config)
    $scope.qualifiers = {};
    // holds the dropdown options for each qualifier code
    $scope.teamsForSelect = {};
    // groups list from config
    $scope.groups = [];
    // ordered list of knockout stages to display (excludes ROUND_OF_32 which is shown as group qualifiers)
    $scope.knockoutStages = [];
    // qualifier entries grouped by stage
    $scope.qualifiersByStage = {};
    // group letter → { winner: code, runnerUp: code }
    $scope.groupQualifiers = {};
    // third place qualifier entries
    $scope.thirdPlaceCodes = [];

    // stage display labels
    $scope.stageLabels = {
        'ROUND_OF_16': 'Round of 32 Winners',
        'QUARTER_FINAL': 'Round of 16 Winners',
        'SEMI_FINAL': 'Quarter-final Winners',
        'FINAL': 'Final',
        'THIRD_PLACE_WINNER': 'Third Place Winner',
        'WINNER': 'Tournament Winner'
    };

    // private state
    var downstreamMap = {};   // code → [downstream codes that reference it in feedsFrom]
    var codeEntryMap = {};    // code → entry from tournament config
    var teamsByGroup = {};    // group letter → [teams]

    // ============== Label generation ==============

    var generateLabel = function (code) {
        if (code === 'WINNER_THIRD_PLACE') return 'Third Place Winner';
        if (code === 'WINNER_FINAL') return 'Tournament Winner';
        if (code.indexOf('WINNER_GROUP_') === 0) return 'Winner';
        if (code.indexOf('RUNNER_UP_GROUP_') === 0) return 'Runner-up';
        if (code.indexOf('THIRD_PLACE_') === 0) {
            return '3rd Place (' + code.replace('THIRD_PLACE_', '').split('').join('/') + ')';
        }
        if (code.indexOf('LOSER_') === 0) return code.replace('LOSER_', 'Loser ');
        if (code.indexOf('WINNER_') === 0) return code.replace('WINNER_', '') + ' Winner';
        return code.replace(/_/g, ' ');
    };

    var generateDescription = function (entry) {
        if (!entry.feedsFrom || entry.feedsFrom.length !== 2) return '';
        return generateLabel(entry.feedsFrom[0]) + ' vs ' + generateLabel(entry.feedsFrom[1]);
    };

    // ============== Build from tournament config ==============

    var buildFromConfig = function (config) {
        $scope.groups = config.groups;
        var qualifierCodes = config.qualifierCodes;
        var allStages = [];

        for (var stageName in qualifierCodes) {
            if (!qualifierCodes.hasOwnProperty(stageName)) continue;
            allStages.push(stageName);
            $scope.qualifiersByStage[stageName] = [];

            var codes = qualifierCodes[stageName];
            for (var i = 0; i < codes.length; i++) {
                var entry = codes[i];

                // initialize qualifier model and dropdown
                $scope.qualifiers[entry.code] = undefined;
                $scope.teamsForSelect[entry.code] = [];
                codeEntryMap[entry.code] = entry;

                // build downstream map from feedsFrom
                if (entry.feedsFrom) {
                    for (var j = 0; j < entry.feedsFrom.length; j++) {
                        var parent = entry.feedsFrom[j];
                        if (!downstreamMap[parent]) downstreamMap[parent] = [];
                        downstreamMap[parent].push(entry.code);
                    }
                }

                var isLoser = entry.code.indexOf('LOSER_') === 0;

                var uiEntry = {
                    code: entry.code,
                    label: generateLabel(entry.code),
                    description: generateDescription(entry),
                    group: entry.group,
                    type: entry.type,
                    feedsFrom: entry.feedsFrom,
                    isLoser: isLoser
                };

                $scope.qualifiersByStage[stageName].push(uiEntry);

                // organize group qualifiers for the grid layout
                if (stageName === 'ROUND_OF_32') {
                    if (entry.group && entry.code.indexOf('WINNER_GROUP_') === 0) {
                        if (!$scope.groupQualifiers[entry.group]) $scope.groupQualifiers[entry.group] = {};
                        $scope.groupQualifiers[entry.group].winner = entry.code;
                    } else if (entry.group && entry.code.indexOf('RUNNER_UP_GROUP_') === 0) {
                        if (!$scope.groupQualifiers[entry.group]) $scope.groupQualifiers[entry.group] = {};
                        $scope.groupQualifiers[entry.group].runnerUp = entry.code;
                    } else if (entry.code.indexOf('THIRD_PLACE_') === 0) {
                        $scope.thirdPlaceCodes.push(uiEntry);
                    }
                }
            }
        }

        // knockout stages = everything after ROUND_OF_32, excluding THIRD_PLACE (auto-set losers)
        $scope.knockoutStages = allStages.filter(function (s) {
            return s !== 'ROUND_OF_32' && s !== 'THIRD_PLACE';
        });
    };

    // ============== Populate group teams ==============

    var populateGroupTeams = function (teams) {
        teamsByGroup = {};
        angular.forEach(teams, function (team) {
            if (!teamsByGroup[team.groupId]) teamsByGroup[team.groupId] = [];
            teamsByGroup[team.groupId].push(team);
        });

        // set dropdown options for group qualifier codes
        for (var code in codeEntryMap) {
            if (!codeEntryMap.hasOwnProperty(code)) continue;
            var entry = codeEntryMap[code];
            if (entry.type === 'GROUP_QUALIFIER') {
                if (entry.group) {
                    $scope.teamsForSelect[code] = teamsByGroup[entry.group] || [];
                } else if (code.indexOf('THIRD_PLACE_') === 0) {
                    var groupLetters = code.replace('THIRD_PLACE_', '').split('');
                    var allTeams = [];
                    for (var i = 0; i < groupLetters.length; i++) {
                        allTeams = allTeams.concat(teamsByGroup[groupLetters[i]] || []);
                    }
                    $scope.teamsForSelect[code] = allTeams;
                }
            }
        }
    };

    // ============== Selection change handling ==============

    $scope.selectionChanged = function (code) {
        setFlagToSelect(code);
        updateDownstream(code);
        autoSetLosers();
    };

    var updateDownstream = function (code) {
        var downstream = downstreamMap[code] || [];
        for (var i = 0; i < downstream.length; i++) {
            var targetCode = downstream[i];
            var entry = codeEntryMap[targetCode];
            if (!entry || !entry.feedsFrom || entry.feedsFrom.length !== 2) continue;

            var p1 = $scope.qualifiers[entry.feedsFrom[0]];
            var p2 = $scope.qualifiers[entry.feedsFrom[1]];
            if (p1 && p2) {
                $scope.teamsForSelect[targetCode] = [p1, p2];
            }
        }
    };

    var autoSetLosers = function () {
        for (var code in codeEntryMap) {
            if (!codeEntryMap.hasOwnProperty(code) || code.indexOf('LOSER_') !== 0) continue;

            var winnerCode = code.replace('LOSER_', 'WINNER_');
            if ($scope.qualifiers[winnerCode] &&
                $scope.teamsForSelect[code] && $scope.teamsForSelect[code].length === 2) {
                var loser = $scope.teamsForSelect[code].filter(function (team) {
                    return team.name !== $scope.qualifiers[winnerCode].name;
                });
                if (loser.length > 0) {
                    $scope.qualifiers[code] = loser[0];
                    setFlagToSelect(code);
                    updateDownstream(code);
                }
            }
        }
    };

    // ============== Helper to check if stage has only non-selectable entries ==============

    $scope.hasOnlyLosers = function (stage) {
        var entries = $scope.qualifiersByStage[stage] || [];
        for (var i = 0; i < entries.length; i++) {
            if (!entries[i].isLoser) return false;
        }
        return true;
    };

    // ============== Save qualifiers ==============

    $scope.saveQualifiers = function () {
        var qualifiersData = [];
        for (var key in $scope.qualifiers) {
            if ($scope.qualifiers.hasOwnProperty(key) && $scope.qualifiers[key]) {
                qualifiersData.push({
                    knockoutTeamCode: key,
                    team: $scope.qualifiers[key],
                    stageId: undefined
                });
            }
        }
        Bets.setQualifiers({qualifiersList: qualifiersData}).then(function () {
            growl.success('You\'re bet saved successfully.', {title: 'Success!'});
            getQualifiers();
        });
    };

    $scope.teamsForStage = function (code) {
        return $scope.teamsForSelect[code] || [];
    };

    // ==============  private functions ==================

    // nifty!
    var setFlagToSelect = function (code) {
        var el = angular.element('#' + code);
        // remove any previous flag icon
        el.parent().find('.fi').remove();
        if ($scope.qualifiers[code]) {
            var team = $scope.qualifiers[code];
            // insert a flag-icons <span> before the select element
            var flagSpan = angular.element('<span class="fi fi-' + team.isoCode + '" style="font-size:1.2em; vertical-align:middle; margin-right:6px;"></span>');
            el.before(flagSpan);
        }
    };

    var getQualifiers = function () {
        Bets.getQualifiers().then(function (response) {
            var now = new Date();
            var lockTime = new Date(response.lockTime);

            $scope.locked = {
                isLocked: (now > lockTime),
                lockTime: lockTime
            };
            // initiate count down
            $scope.CountDown.initializeClock($scope.locked.lockTime);

            angular.forEach(response.qualifiersList, function (qualifiers, index) {
                $scope.qualifiers[qualifiers.knockoutTeamCode] = qualifiers.team;
                $scope.selectionChanged(qualifiers.knockoutTeamCode);
            });
        });
    };

    // ============== Init ==============

    var init = function () {
        Tournament.getConfig().then(function (config) {
            buildFromConfig(config);
            Teams.getAllTeams().then(function (teams) {
                populateGroupTeams(teams);
                getQualifiers();
            });
        });
    };

    init();

});
