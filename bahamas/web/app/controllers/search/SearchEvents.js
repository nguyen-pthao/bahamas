/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('searchEvents', ['$scope', 'session', '$state', 'dataSubmit', 'loadTeamAffiliation', 'loadLanguage', 'loadLSAClass', 'loadEventLocation', '$timeout', function ($scope, session, $state, dataSubmit, loadTeamAffiliation, loadLanguage, loadLSAClass, loadEventLocation, $timeout) {
        var user = session.getSession('userType');

        //FOR EVENTS


        $scope.searchEvent = {
            'event_title': '',
            'event_location': '',
            'start_date': '',
            'end_date': '',
            'team_affiliation': '',
            'if_team_other': '',
            'participant': '',
            'is_other_location': false,
            'token': ''
        };

        $scope.tempLocation = '';
        $scope.tempLocationIfOther = '';
        $scope.tempTeamIfOther = '';
        $scope.retrieveEventLocation = function () {
            loadEventLocation.retrieveEventLocation().then(function (response) {
                $scope.locationList = response.data.eventLocationList;
                $scope.locationList.unshift({eventLocation: ''});
            });
        };

        $scope.locationChanged = function () {
            if ($scope.tempLocation == 'Other') {
                $scope.searchEvent.is_other_location = true;
            } else {
                $scope.searchEvent.is_other_location = false;
            }
            ;
        };

        $scope.teamChanged = function () {
            if ($scope.searchEvent.team_affiliation != 'Other') {
                $scope.searchEvent.if_team_other = '';
            }
            ;
        };

        $scope.tempStartDate = "";
        $scope.tempEndDate = "";

        $scope.submitSearchEvent = function () {
            $scope.searchEvent.token = session.getSession('token');
            if ($scope.searchEvent.is_other_location === true) {
                $scope.searchEvent.event_location = $scope.tempLocationIfOther;
            } else {
                $scope.searchEvent.event_location = $scope.tempLocation;
            }
            ;

            $scope.searchEvent.start_date = $scope.tempStartDate.valueOf();
            $scope.searchEvent.end_date = $scope.tempEndDate.valueOf();
            var url = '/event.search';
            $scope.myPromise = dataSubmit.submitData($scope.searchEvent, url).then(function (response) {
                if (response.data.message == "success") {
                    $scope.returnEvents = response.data.event;
                } else {
                    alert('Unable to establish connection!');
                }
                ;
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