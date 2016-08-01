/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('editEvent',
        ['$scope', 'session', '$state', 'localStorageService', '$http', 'loadEventLocation', 'loadEventClass', '$timeout', 'ngDialog', 'dataSubmit', 'loadEventStatus',
            function ($scope, session, $state, localStorageService, $http, loadEventLocation, loadEventClass, $timeout, ngDialog, dataSubmit, loadEventStatus) {
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
                    dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                        $scope.eventInfo = response.data;
                        
                        $scope.showGmap = true;
                        if ($scope.eventInfo['event_lat'] == '' || $scope.eventInfo['event_lng'] == '') {
                            $scope.showGmap = false;
                        } else {
                            $scope.map = {center: {latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']}, zoom: 15, options: {scrollwheel: false}, control: {}};

                            $scope.marker = {coords: {latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']}, id: 1};
                        }
                        $scope.roles = $scope.eventInfo['event_role'];
                        $scope.affiliation = $scope.eventInfo['event_team_affiliation'];
                        $scope.teamA = $scope.eventInfo['event_team_affiliation']['teams_affiliated'];
                    })
                    $scope.searchbox = {template: './style/ngTemplate/searchbox.tpl.html', events: {places_changed: function (searchBox) {
                            $scope.editEvent['event_lat'] = searchBox.getPlaces()[0].geometry.location.lat();
                            $scope.editEvent['event_lng'] = searchBox.getPlaces()[0].geometry.location.lng();
                            $scope.marker = {coords: {latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()}};
                            $scope.map.zoom = 15;
                            $scope.map.control.refresh({latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()});
                        }}, options: {}};
                
                    $scope.$watch('showGmap', function () {
                        if ($scope.showGmap == true) {
                            $timeout(function () {
                                $scope.map.control.refresh({latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']});
                                $scope.map.zoom = 15;
                            }, 0);
                        }
                    })
                    
                }
            }]);