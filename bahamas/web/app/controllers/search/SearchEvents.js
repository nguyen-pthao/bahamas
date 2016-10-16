/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('searchEvents', ['$scope', 'session', '$state', 'dataSubmit', 'loadTeamAffiliation', 'loadLanguage', 'loadLSAClass', 'loadEventLocation', '$timeout', 'ngDialog', function ($scope, session, $state, dataSubmit, loadTeamAffiliation, loadLanguage, loadLSAClass, loadEventLocation, $timeout, ngDialog) {
        var user = session.getSession('userType');

        //FOR EVENTS
        $scope.searchEvent = {
            'event_title': '',
            'event_location': '',
            'start_date': '',
            'if_location_other': '',
            'end_date': '',
            'team_affiliation': '',
            'participant': '',
            'token': ''
        };

        $scope.retrieveEventLocation = function () {
            loadEventLocation.retrieveEventLocation().then(function (response) {
                $scope.locationList = response.data.eventLocationList;
                $scope.locationList.unshift({eventLocation: ''});
            });
        };
        
        $scope.retrieveTeamList = function () {
            loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                $scope.teamAffiliationList = response.data.teamAffiliationList;
                $scope.teamAffiliationList.unshift({teamAffiliation: ''})
            });
        };

        $scope.tempStartDate = "";
        $scope.tempEndDate = "";

        $scope.submitSearchEvent = function () {
            $scope.searchEvent.token = session.getSession('token');
            $scope.searchEvent.start_date = $scope.tempStartDate.valueOf();
            $scope.searchEvent.end_date = $scope.tempEndDate.valueOf();
            if ($scope.searchEvent.event_location != "Other") {
                $scope.searchEvent.if_location_other = "";
            }
            ;
            var url = '/event.search';
            $scope.myPromise = dataSubmit.submitData($scope.searchEvent, url).then(function (response) {
                if (response.data.message == "success") {
                    $scope.returnEvents = response.data.event;
                } else {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/searchFailed.html',
                        className: 'ngdialog-theme-default'
                    });
                };
            });
        };

        $scope.goToEvent = function ($event, re) {
            var url = user + '.viewIndivEvent';
            session.setSession('eventIdToDisplay', re['eventid']);
            $state.go(url);
        };

        //-----for the datepicker-----
        $scope.today = function () {
            $scope.dt = new Date();
        };
        $scope.today();

        $scope.clear = function () {
            $scope.dt = null;
        };

        $scope.inlineOptions = {
            customClass: getDayClass,
            showWeeks: true
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            formatMonth: 'MMM',
            formatDay: 'dd',
            startingDay: 1
        };

        $scope.openStart = function () {
            $timeout(function () {
                $scope.openedStart = true;
            })
        }

        $scope.openEnd = function () {
            $timeout(function () {
                $scope.openedEnd = true;
            })
        }

        function getDayClass(data) {
            var date = data.date,
                    mode = data.mode;
            if (mode === 'day') {
                var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

                for (var i = 0; i < $scope.events.length; i++) {
                    var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

                    if (dayToCheck === currentDay) {
                        return $scope.events[i].status;
                    }
                }
            }

            return '';
        }
        $scope.format = 'dd MMM yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];
        //----- end of datepicker settings-----
    }]);