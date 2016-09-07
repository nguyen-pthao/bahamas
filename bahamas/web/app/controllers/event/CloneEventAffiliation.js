/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('cloneEventAffiliation',
        ['$scope', 'session', '$state', 'localStorageService', '$http', '$timeout', 'ngDialog', 'dataSubmit', 'loadTeamAffiliation',
            function ($scope, session, $state, localStorageService, $http, $timeout, ngDialog, dataSubmit, loadTeamAffiliation) {
                var eventId = localStorageService.get('eventIdCreate');
                var eventIdArray = localStorageService.get('eventIdArray');
                var eventIdToClone = localStorageService.get('eventIdToClone');
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
                    })
                }

                $scope.retrieveEvent = function () {

                    $scope.toRetrieve = {
                        'token': session.getSession('token'),
                        'eventId': eventIdToClone
                    }
                    var url = '/event.retrieveindiv';
                    $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                        $scope.newEventInfo = response.data;
                        var teamAffiliation = $scope.newEventInfo['event_team_affiliation']['teams_affiliated'];
                        loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                            $scope.selectedTeams = {
                            };
                            $scope.teamAffiliationList = response.data.teamAffiliationList;
                            var other;
                            for (var obj in $scope.teamAffiliationList) {
                                if ($scope.teamAffiliationList[obj].teamAffiliation == 'Others') {
                                    other = $scope.teamAffiliationList.splice(obj, 1);
                                }
                                $scope.selectedTeams[obj] = false;
                                angular.forEach(teamAffiliation, function (value, key) {
                                    if ($scope.teamAffiliationList[obj].teamAffiliation == value) {
                                        $scope.selectedTeams[obj] = true;
                                    }
                                })


                            }

                        });
                        $scope.toSubmit = {
                            'token': session.getSession('token'),
                            'event_id': eventId,
                            'event_id_list': eventIdArray,
                            'teams': [
                            ],
                            'explain_if_others': $scope.newEventInfo['event_team_affiliation']['explain_if_other']
                        };
                    })

                }


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
                        $scope.myPromise = dataSubmit.submitData($scope.toSubmit, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/addEventSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    localStorageService.remove('eventIdCreate');
                                    localStorageService.remove('eventIdArray');
                                    localStorageService.remove('eventIdToClone');
                                    var afterSuccess = user + '.viewUpcomingEvents'
                                    $state.go(afterSuccess);
                                })
                            }else if (response.data.message == 'error') {
                                $scope.errorMessages = response.data.errorMsg;
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/errorMessage.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function(response){
                                    $scope.toSubmit['teams'] = [];
                                })
                            }else {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/addEventAffiliationFailure.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    $scope.toSubmit['teams'] = [];
                                    var currentState = user + '.cloneEventAffiliation';
                                    $state.reload(currentState);
                                })
                            }
                        })
                    }
                };

                $scope.toEvents = function () {
                    localStorageService.remove('eventIdCreate');
                    localStorageService.remove('eventIdArray');
                    localStorageService.remove('eventIdToClone');
                    var toEventsUrl = user + '.viewUpcomingEvents';
                    $state.go(toEventsUrl);
                };

            }]);