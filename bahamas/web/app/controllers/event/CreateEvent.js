/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('createEvent',
        ['$scope', 'session', '$state', 'localStorageService', '$http', 'loadEventLocation', 'loadEventClass', '$timeout', 'ngDialog', 'dataSubmit', 'loadEventStatus',
            function ($scope, session, $state, localStorageService, $http, loadEventLocation, loadEventClass, $timeout, ngDialog, dataSubmit, loadEventStatus) {
                var user = session.getSession('userType');
                $scope.backHome = function () {
                    $state.go(user);
                };

                $scope.regex = '\\d+';

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

                $scope.$watch("newEvent['event_start_date']", function () {
                    $scope.newEvent['event_end_date'] = $scope.newEvent['event_start_date'];
                })

                $scope.newEvent = {
                    'token': session.getSession('token'),
                    'event_title': '',
                    'event_start_date': new Date(),
                    'event_end_date': new Date(),
                    'event_time_start': '',
                    'event_time_end': '',
                    'send_reminder': false,
                    'event_description': '',
                    'minimum_participation': '',
                    'event_class': "Internal",
                    'event_location': '',
                    'event_status': "Open",
                    'event_lat': '',
                    'event_lng': '',
                    'address': '',
                    'zipcode': '',
                    'ignore': false
                }

                //--for google maps--
                $scope.map = {center: {latitude: 1.355865, longitude: 103.819129}, zoom: 10, options: {scrollwheel: false}, control: {}};
                $scope.$watch('showGoogleMaps', function () {
                    if ($scope.showGoogleMaps == true) {
                        $timeout(function () {
                            $scope.map.control.refresh({latitude: 1.355865, longitude: 103.819129});
                            $scope.map.zoom = 10;
                        }, 0);
                    }
                })
                $scope.marker = {coords: {latitude: '', longitude: ''}, id: 1};
                $scope.searchbox = {template: './style/ngTemplate/searchbox.tpl.html', events: {places_changed: function (searchBox) {
                            $scope.newEvent['event_lat'] = searchBox.getPlaces()[0].geometry.location.lat();
                            $scope.newEvent['event_lng'] = searchBox.getPlaces()[0].geometry.location.lng();
                            $scope.marker = {coords: {latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()}};
                            $scope.map.zoom = 15;
                            $scope.map.control.refresh({latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()});
                        }}, options: {}};
                //--end of settings for google maps--
                
                //location change function to populate address and zipcode
                $scope.locationChange = function(location){
                    angular.forEach($scope.eventLocationList, function(value, key){
                        if(value.eventLocation==location){
                           $scope.newEvent['address'] = value.address;
                           $scope.newEvent['zipcode'] = value.zipcode;
                        }
                    })
                };
                //--end of location change function--
                
                $scope.createEvent = function () {
                    if ($scope.newEvent['event_start_date'] == null) {
                        $scope.newEvent['event_start_date'] = ''
                    } else if ($scope.newEvent['event_start_date'] != null || $scope.newEvent['event_start_date'] != '') {
                        $scope.newEvent['event_start_date'] = $scope.newEvent['event_start_date'].valueOf() + "";
                    }
                    if ($scope.newEvent['event_end_date'] == null) {
                        $scope.newEvent['event_end_date'] = ''
                    } else if ($scope.newEvent['event_end_date'] != null || $scope.newEvent['event_end_date'] != '') {
                        $scope.newEvent['event_end_date'] = $scope.newEvent['event_end_date'].valueOf() + "";
                    }
                    if ($scope.newEvent['event_time_start'] == null) {
                        $scope.newEvent['event_time_start'] = ''
                    } else if ($scope.newEvent['event_time_start'] != null || $scope.newEvent['event_time_start'] != '') {
                        $scope.newEvent['event_time_start'] = $scope.newEvent['event_time_start'].valueOf() + "";
                    }
                    if ($scope.newEvent['event_time_end'] == null) {
                        $scope.newEvent['event_time_end'] = ''
                    } else if ($scope.newEvent['event_time_end'] != null || $scope.newEvent['event_time_end'] != '') {
                        $scope.newEvent['event_time_end'] = $scope.newEvent['event_time_end'].valueOf() + "";
                    }
                    if ($scope.showGoogleMaps == false) {
                        $scope.newEvent['event_lat'] = '';
                        $scope.newEvent['event_lng'] = '';
                    }
                    if ($scope.newEvent['minimum_participation'] == '') {
                        $scope.newEvent['minimum_participation'] = "1";
                    }

                    var url = "/event.create";
                    dataSubmit.submitData($scope.newEvent, url).then(function (response) {
                        if (response.data.message == 'success') {
                            var id = response.data['event_id'];
                            localStorageService.set('eventIdCreate', id);
                            var toURL = user + '.createEventRoles';
                            $state.go(toURL);
                        } else if(response.data.message == 'error'){
                            $scope.errorMessages = response.data.errorMsg;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            })
                        } else if(response.data.message == 'conflict'){
                            $scope.errorMessages = response.data.errorMsg;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/addEventConflict.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            }).then(function (response) {
                                $scope.newEvent.ignore = true;
                                console.log($scope.newEvent);
                                dataSubmit.submitData($scope.newEvent, url).then(function (response) {
                                    if (response.data.message == 'success') {
                                        var id = response.data['event_id'];
                                        localStorageService.set('eventIdCreate', id);
                                        var toURL = user + '.createEventRoles';
                                        $state.go(toURL);
                                    }
                                });
                            });
                        }else{
                            window.alert("Unable to establish connection");
                        }
                    });

                };

            }]);