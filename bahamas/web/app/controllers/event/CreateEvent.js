/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('createEvent',
        ['$scope', 'session', '$state', 'localStorageService', '$http', 'loadEventLocation', 'loadEventClass', '$timeout',
            function ($scope, session, $state, localStorageService, $http, loadEventLocation, loadEventClass, $timeout) {
                var user = session.getSession('userType');
                $scope.backHome = function () {
                    $state.go(user);
                };
                
                $scope.regex = '\\d+';
                
                $scope.loadEventLocationList = function(){
                    loadEventLocation.retrieveEventLocation().then(function(response){
                        $scope.eventLocationList = response.data.eventLocationList;
                        var other;
                        for (var obj in $scope.eventLocationList) {
                            if ($scope.eventLocationList[obj].eventLocation == 'Others') {
                                other = $scope.eventLocationList.splice(obj, 1);
                            }
                        }
                        $scope.eventLocationList.push(other[0]);
                    })
                }
                
                $scope.loadEventClassList = function(){
                    loadEventClass.retrieveEventClass().then(function(response){
                        $scope.eventClassList = response.data.eventClassList;
                        var other;
                        for (var obj in $scope.eventClassList) {
                            if ($scope.eventClassList[obj].eventClass == 'Others') {
                                other = $scope.eventClassList.splice(obj, 1);
                            }
                        }
                        $scope.eventClassList.push(other[0]);
                    })
                }
                
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
                    minDate: new Date(),
                    startingDay: 1
                };

                $scope.open = function () {
                    $timeout(function () {
                        $scope.opened = true;
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
                
                $scope.newEvent = {
                    'event_title' : '',
                    'event_date': '',
                    'event_time_start': '',
                    'event_time_end': '',
                    'send_reminder': false,
                    'event_description': '',
                    'minimum_participation': '',
                    'event_class': '',
                    'event_location': '',
                    'explain_if_others': ''
                }
                
                $scope.createEvent = function(){
                    if($scope.newEvent['event_date'] == null){
                        $scope.newEvent['event_date'] = ''
                    }else if($scope.newEvent['event_date'] != null || $scope.newEvent['event_date'] != ''){
                        $scope.newEvent['event_date'] = $scope.newEvent['event_date'].valueOf() + "";
                    }
                    if($scope.newEvent['event_time_start'] == null){
                        $scope.newEvent['event_time_start'] = ''
                    }else if($scope.newEvent['event_time_start'] != null || $scope.newEvent['event_time_start'] != ''){
                        $scope.newEvent['event_time_start'] = $scope.newEvent['event_time_start'].valueOf() + "";
                    }
                    if($scope.newEvent['event_time_end'] == null){
                        $scope.newEvent['event_time_end'] = ''
                    }else if($scope.newEvent['event_time_end'] != null || $scope.newEvent['event_time_end'] != ''){
                        $scope.newEvent['event_time_end'] = $scope.newEvent['event_time_end'].valueOf() + "";
                    }
                    console.log($scope.newEvent);
                }

            }]);