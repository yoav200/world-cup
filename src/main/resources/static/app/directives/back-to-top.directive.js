'use strict';

angular.module('worldcup').directive('backToTop', function () {
    return {
        restrict: 'E'
        , replace: true
        , template: '<div class="back-to-top"><i class="fa fa-chevron-up">Top</i></div>'
        , link: function ($scope, element, attrs) {
            $(window).scroll(function () {
                if ($(window).scrollTop() <= 0) {
                    $(element).fadeOut();
                } else {
                    $(element).fadeIn();
                }
            });

            $(element).on('click', function () {
                $('html, body').animate({scrollTop: 0}, 'fast');
            });

        }
    }
});