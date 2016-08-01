/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('editEvent',
        ['$scope', 'session', '$state', 'localStorageService', '$http', 'loadEventLocation', 'loadEventClass', '$timeout', 'ngDialog', 'dataSubmit', 'loadEventStatus', 'loadTeamAffiliation',
            function ($scope, session, $state, localStorageService, $http, loadEventLocation, loadEventClass, $timeout, ngDialog, dataSubmit, loadEventStatus, loadTeamAffiliation) {
                var user = session.getSession('userType');
                $scope.backHome = function () {
                    $state.go(user);
                };

                $scope.toEvents = function () {
                    var url = user + '.viewUpcomingEvents';
                    $state.go(url);
                };

                var eventId = localStorageService.get('eventId');

                $scope.retrieveEvent = function () {
                    $scope.toRetrieve = {
                        'token': session.getSession('token'),
                        'eventId': eventId
                    }
                    var url = '/event.retrieveindiv';
                    $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                        $scope.eventInfo = response.data;
                        $scope.editEvent = angular.copy($scope.eventInfo);
                        $scope.editEvent['event_start_date'] = new Date($scope.eventInfo['event_start_date']);
                        $scope.editEvent['event_end_date'] = new Date($scope.eventInfo['event_end_date']);
                        if($scope.eventInfo['send_reminder']=='true'){
                            $scope.editEvent['send_reminder'] = true;
                        }else{
                            $scope.editEvent['send_reminder'] = false;
                        }
                        $scope.roleArray = $scope.eventInfo['event_role'].slice(1);
                        console.log($scope.editEvent);
                        $scope.showGmap = true;
                        if ($scope.eventInfo['event_lat'] == '' || $scope.eventInfo['event_lng'] == '') {
                            $scope.showGmap = false;
                        } else {
                            $scope.map = {center: {latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']}, zoom: 15, options: {scrollwheel: false}, control: {}};
                            $scope.marker = {coords: {latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']}, id: 1};
                            $scope.$watch('showGmap', function () {
                                if ($scope.showGmap == true) {
                                    $timeout(function () {
                                        $scope.marker = {coords: {latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']}, id: 1};
                                        $scope.map.zoom = 15;
                                    }, 0);
                                }
                            })
                            $scope.searchbox = {template: './style/ngTemplate/searchbox.tpl.html', events: {places_changed: function (searchBox) {
//                                        $scope.editEvent['event_lat'] = searchBox.getPlaces()[0].geometry.location.lat();
//                                        $scope.editEvent['event_lng'] = searchBox.getPlaces()[0].geometry.location.lng();
                                        $scope.marker = {coords: {latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()}};
                                        $scope.map.zoom = 15;
                                        $scope.map.control.refresh({latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()});
                                    }}, options: {}};
                        }
                        $scope.roles = $scope.eventInfo['event_role'];
                        $scope.affiliation = $scope.eventInfo['event_team_affiliation'];
                        $scope.teamA = $scope.eventInfo['event_team_affiliation']['teams_affiliated'];
                    })
                }
                
                $scope.loadEventStatusList = function () {
                    loadEventStatus.retrieveEventStatus().then(function (response) {
                        $scope.eventStatusList = response.data.eventStatusList;
                    })
                }

                $scope.loadEventLocationList = function () {
                    loadEventLocation.retrieveEventLocation().then(function (response) {
                        $scope.eventLocationList = response.data.eventLocationList;
                    })
                }

                $scope.loadEventClassList = function () {
                    loadEventClass.retrieveEventClass().then(function (response) {
                        $scope.eventClassList = response.data.eventClassList;
                    })
                }
                
                $scope.loadTeamAffiliationList = function () {
                    loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                        $scope.teamAffiliationList = response.data.teamAffiliationList;
                        var other;
                        for (var obj in $scope.teamAffiliationList) {
                            if ($scope.teamAffiliationList[obj].teamAffiliation == 'Others') {
                                other = $scope.teamAffiliationList.splice(obj, 1);
                            }
                        }
                    });
                };
                
                 $scope.regex = '\\d+';
                
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
                
                $scope.addNumberOfRoles = function () {
                    $scope.roleArray.push({});
                }
            }]);