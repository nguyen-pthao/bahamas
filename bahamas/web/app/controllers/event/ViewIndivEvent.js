/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewIndivEvent',
        ['$scope', 'session', '$state', 'filterFilter', 'ngDialog', 'dataSubmit', '$stateParams',
            function ($scope, session, $state, filterFilter, ngDialog, dataSubmit, $stateParams) {
                var user = session.getSession('userType');
                var eventId = $stateParams.eventId;
                console.log(eventId);
                $scope.backHome = function () {
                    $state.go(user);
                };

                $scope.toEvents = function () {
                    var url = user + '.viewUpcomingEvents';
                    $state.go(url);
                };

                $scope.retrieveEvent = function () {
                    $scope.toRetrieve = {
                        'token': session.getSession('token'),
                        'eventId': eventId
                    }
                    var url = '/event.retrieveindiv';
                    dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                        $scope.eventInfo = response.data;
                        var timeStart = new Date($scope.eventInfo['event_time_start']).toLocaleTimeString();
                        var timeEnd = new Date($scope.eventInfo['event_time_end']).toLocaleTimeString();
                        if (timeStart.length == 10) {
                            var meridianS = timeStart.substring(8, 10);
                            var timeS = "0" + timeStart.substring(0, 4);
                        } else if (timeStart.length == 11) {
                            var meridianS = timeStart.substring(9, 11);
                            var timeS = timeStart.substring(0, 5);
                        }
                        if (timeEnd.length == 10) {
                            var meridianE = timeEnd.substring(8, 10);
                            var timeE = "0" + timeEnd.substring(0, 4);
                        } else if (timeEnd.length == 11) {
                            var meridianE = timeEnd.substring(9, 11);
                            var timeE = timeEnd.substring(0, 5);
                        }
                        $scope.eventInfo['event_start_date'] = new Date($scope.eventInfo['event_start_date']);
                        $scope.eventInfo['event_end_date'] = new Date($scope.eventInfo['event_end_date']);
                        $scope.eventInfo['event_time_start'] = timeS + " " + meridianS;
                        $scope.eventInfo['event_time_end'] = timeE + " " + meridianE;
                        $scope.map = {center: {latitude: $scope.eventInfo['event_latitude'], longitude: $scope.eventInfo['event_longitude']}, zoom: 15, options: {scrollwheel: false}, control: {}};

                        $scope.marker = {coords: {latitude: $scope.eventInfo['event_latitude'], longitude: $scope.eventInfo['event_longitude']}, id: 1};
                    })
                }

            }]);