/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('createEventRoles',
        ['$scope', 'session', '$state', 'localStorageService', '$http', '$timeout', '$stateParams', 'ngDialog', 'dataSubmit',
            function ($scope, session, $state, localStorageService, $http, $timeout, $stateParams, ngDialog, dataSubmit) {
                var eventId = localStorageService.get('eventIdCreate');
                var user = session.getSession('userType');
                $scope.backHome = function () {
                    $state.go(user);
                };

                $scope.eventDetails = function () {
                    var urlToRetrieve = "/event.retrieve";
                    var toRetrieve = {
                        'event_id': eventId,
                        'token': session.getSession('token')
                    }
                    dataSubmit.submitData(toRetrieve, urlToRetrieve).then(function (response) {
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
                        $scope.eventInfo['event_date'] = new Date($scope.eventInfo['event_date']);
                        $scope.eventInfo['event_time_start'] = timeS + " " + meridianS;
                        $scope.eventInfo['event_time_end'] = timeE + " " + meridianE;
                    })
                }

                $scope.newRoles = {
                    'event_id': eventId,
                    'roleArray': [
                        {'role1': '', 'description1': ''},
                        {'role2': '', 'description2': ''},
                        {'role3': '', 'description3': ''}
                    ]
                }


                $scope.submitRoles = function () {
                    $scope.error = false;
//                    var size = Object.keys($scope.newRoles).length / 2;
                    var size = $scope.newRoles['roleArray'].length;
                    for (var i = 1; i <= size; i++) {
                        var role = $scope.newRoles['roleArray'][(i - 1)]['role' + i];
                        var description = $scope.newRoles['roleArray'][(i - 1)]['description' + i];
                        if (role == '' && description != '') {
                            $scope.error = true;
                        } else if (angular.isUndefined(role)) {
                            $scope.error = true;
                        } else if (angular.isUndefined(description)) {
                            $scope.error = true;
                        }
                    }
                    if ($scope.error == true) {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/addRolesError.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        })
                    } else {
                        $scope.newRoles.token = session.getSession('token');
                        console.log($scope.newRoles);
                        //submit to backend here.
                        var url = "/event.addroles";
                        dataSubmit.submitData($scope.newRoles, url).then(function (response) {
                            if(response.data.message == 'success'){
                                $state.go('admin.createEventAffiliation');
                            }else{
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/addRolesError.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                })
                            }
                        });
                    }
                }

                $scope.numberOfRoles = [1, 2, 3];
                $scope.addNumberOfRoles = function () {
                    var roleIntoNewRoles = "role" + ($scope.numberOfRoles.length + 1);
                    var descriptionIntoNewRoles = "description" + ($scope.numberOfRoles.length + 1);
                    var newRole = {};
                    newRole[roleIntoNewRoles] = '';
                    newRole[descriptionIntoNewRoles] = '';
                    $scope.newRoles.roleArray.push(newRole);
                    $scope.numberOfRoles.push($scope.numberOfRoles.length + 1);
                }
//
//                $scope.map = {center: {latitude: 1.302918, longitude: 103.864964}, zoom: 15, options: {scrollwheel: false}, control:{}};
//
//                $scope.marker = {coords: {latitude: 1.302918, longitude: 103.864964}, id: 1};
//
//                $scope.searchbox = {template:'./style/ngTemplate/searchbox.tpl.html', events:{places_changed: function (searchBox) {
//                    console.log(searchBox.getPlaces()[0].geometry.location.lat());
//                    console.log(searchBox.getPlaces()[0].geometry.location.lng());
//                    $scope.marker.coords = {
//                        latitude: searchBox.getPlaces()[0].geometry.location.lat(),
//                        longitude: searchBox.getPlaces()[0].geometry.location.lng()
//                    };
//                    $scope.map.control.refresh({latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()});
//                }}};
            }]);