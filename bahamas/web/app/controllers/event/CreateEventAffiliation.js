/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('createEventAffiliation',
        ['$scope', 'session', '$state', 'localStorageService', '$http', '$timeout', 'ngDialog', 'dataSubmit', 'loadTeamAffiliation',
            function ($scope, session, $state, localStorageService, $http, $timeout, ngDialog, dataSubmit, loadTeamAffiliation) {
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
                        $scope.eventInfo['event_start_date'] = new Date($scope.eventInfo['event_start_date']);
                        $scope.eventInfo['event_time_start'] = timeS + " " + meridianS;
                        $scope.eventInfo['event_time_end'] = timeE + " " + meridianE;
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

                $scope.selectedTeams = {
                };

                $scope.selectedTeamsCopy = {
                };

                $scope.toSubmit = {
                    'token': session.getSession('token'),
                    'event_id': eventId,
                    'teams': [
                    ],
                    'explain_if_others': ''
                };

                $scope.toSubmitCopy = {
                    'token': session.getSession('token'),
                    'event_id': eventId,
                    'teams': [
                    ],
                    'explain_if_others': ''
                };

                $scope.submit = function () {
                    var hasSelected = false;
                    angular.forEach($scope.selectedTeams, function (value, key) {
                        if (value == true) {
                            hasSelected = true;
                            $scope.toSubmit['teams'].push($scope.teamAffiliationList[key].teamAffiliation);
                        }
                    });
                    if (hasSelected == false) {
                        $scope.errorMessages = ["Please select at least one Team."];
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/errorMessage.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        })
                    } else {
                        var url = "/event.addaffiliation";
                        dataSubmit.submitData($scope.toSubmit, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/addEventSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    localStorageService.remove('eventIdCreate');
                                    var afterSuccess = user + '.viewUpcomingEvents'
                                    $state.go(afterSuccess);
                                })
                            } else {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/addEventAffiliationFailure.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    $scope.toSubmit = angular.copy($scope.toSubmitCopy);
                                    $scope.selectedTeams = angular.copy($scope.selectedTeamsCopy);
                                    var currentState = user + '.createEventAffiliation';
                                    $state.reload(currentState);
                                })
                            }
                        })
                    }
                };

                $scope.toEvents = function () {
                    localStorageService.remove('eventIdCreate');
                    var toEventsUrl = user + '.viewUpcomingEvents';
                    $state.go(toEventsUrl);
                };

            }]);