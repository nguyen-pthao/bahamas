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
                        if ($scope.eventInfo['send_reminder'] == 'true') {
                            $scope.editEvent['send_reminder'] = true;
                        } else {
                            $scope.editEvent['send_reminder'] = false;
                        }
                        $scope.roleArray = $scope.eventInfo['event_role'].slice(1);
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
                                        $scope.editEvent['event_lat'] = searchBox.getPlaces()[0].geometry.location.lat();
                                        $scope.editEvent['event_lng'] = searchBox.getPlaces()[0].geometry.location.lng();
                                        $scope.marker = {coords: {latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()}};
                                        $scope.map.zoom = 15;
                                        $scope.map.control.refresh({latitude: searchBox.getPlaces()[0].geometry.location.lat(), longitude: searchBox.getPlaces()[0].geometry.location.lng()});
                                    }}, options: {}};
                        }
                        $scope.roles = $scope.eventInfo['event_role'];
                        $scope.affiliation = $scope.eventInfo['event_team_affiliation'];
                        $scope.teamA = $scope.eventInfo['event_team_affiliation']['teams_affiliated'];

                        //for the team
                        loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                            $scope.teamAffiliationList = response.data.teamAffiliationList;
//                        var other;
//                        for (var obj in $scope.teamAffiliationList) {
//                            if ($scope.teamAffiliationList[obj].teamAffiliation == 'Others') {
//                                other = $scope.teamAffiliationList.splice(obj, 1);
//                            }
//                        }
                            $scope.selectedTeams = [];
                            $scope.teamAffiliationList.forEach(function (teamObj) {
                                $scope.selectedTeams.push({
                                    'teamAffiliation': teamObj.teamAffiliation,
                                    'selected': false
                                })
                            });
                            if (angular.equals({}, $scope.eventInfo['event_team_affiliation'])) {

                            } else {
                                var otherTeamExplain = $scope.eventInfo['event_team_affiliation']['explain_if_other'];
                                $scope.otherTeam = {
                                    'selected': false,
                                    'explain_if_other_team': otherTeamExplain,
                                    'remarks': $scope.eventInfo['event_team_affiliation']['remarks']
                                };
                            }
                            if (angular.isUndefined($scope.eventInfo['event_team_affiliation']['explain_if_other'])) {
                                $scope.otherTeam = {
                                    'selected': false,
                                    'explain_if_other_team': '',
                                    'remarks': ''
                                }
                            } else {
                                var otherTeamExplain = $scope.eventInfo['event_team_affiliation']['explain_if_other'];
                                $scope.otherTeam = {
                                    'selected': false,
                                    'explain_if_other_team': otherTeamExplain,
                                    'remarks': $scope.eventInfo['event_team_affiliation']['remarks']
                                };
                                $scope.eventInfo['event_team_affiliation']['teams_affiliated'].forEach(function (name) {
                                    if (name == 'Other') {
                                        $scope.otherTeam['selected'] = true;
                                    }
                                    $scope.selectedTeams.forEach(function (team) {
                                        if (name == team.teamAffiliation) {
                                            team.selected = true;
                                        }
                                    });
                                });
                            }
                        });
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
                    $scope.editEvent['event_role'].push({
                        'event_desc': '',
                        'event_role': ''
                    });
                    $scope.roleArray.push({
                        'event_desc': '',
                        'event_role': ''
                    });
                }

                $scope.submitEditEvent = function () {
                    var startDate = $scope.editEvent['event_start_date'].valueOf() + "";
                    var endDate = $scope.editEvent['event_end_date'].valueOf() + "";
                    var timeStart = new Date($scope.editEvent['event_time_start']).valueOf() + "";
                    var timeEnd = new Date($scope.editEvent['event_time_end']).valueOf() + "";
                    $scope.toEditEvent = {
                        'token': session.getSession('token'),
                        'event_id': eventId,
                        'event_class': $scope.editEvent['event_class'],
                        'event_description': $scope.editEvent['event_description'],
                        'event_end_date': endDate,
                        'event_lat': $scope.editEvent['event_lat'],
                        'event_lng': $scope.editEvent['event_lng'],
                        'event_location': $scope.editEvent['event_location'],
                        'event_start_date': startDate,
                        'event_status': $scope.editEvent['event_status'],
                        'event_time_end': timeEnd,
                        'event_time_start': timeStart,
                        'event_title': $scope.editEvent['event_title'],
                        'explain_if_other': $scope.editEvent['explain_if_other'],
                        'minimum_participation': $scope.editEvent['minimum_participation'],
                        'send_reminder': $scope.editEvent['send_reminder'],
                        'ignore': false
                    }
                    console.log($scope.toEditEvent);
                    var url = "/event.updatedetails";
                    dataSubmit.submitData($scope.toEditEvent, url).then(function (response) {
                        console.log(response);
                        if (response.data.message == "success") {
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/editSuccessful.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            })
                        } else if (response.data.message == "conflict") {
                            $scope.errorMessages = response.data.errorMsg;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/addEventConflict.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            }).then(function (response) {
                                $scope.toEditEvent.ignore = true;
                                dataSubmit.submitData($scope.toEditEvent, url).then(function (response) {
                                    if (response.data.message == 'success') {
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/editSuccessful.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        })
                                    }
                                });
                            });
                        } else {
                            $scope.errorMessages = response.data.errorMsg;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            })
                        }
                    });
                };

                $scope.submitEditRoles = function () {
                    var hasError = false;
                    $scope.editEvent['event_role'].forEach(function (obj) {
                        if (obj['event_role'] == '') {
                            if (obj['event_desc'] != '') {
                                hasError = true;
                            }
                        } else if (angular.isUndefined(obj['event_role'])) {
                            hasError = true;
                        } else if (angular.isUndefined(obj['event_desc'])) {
                            hasError = true;
                        }
                    })

                    if (hasError == true) {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/addRolesError.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        })
                    } else {
                        $scope.toEditRoles = {
                            'token': session.getSession('token'),
                            'event_id': eventId,
                            'event_role': $scope.editEvent['event_role']
                        }
                        console.log($scope.toEditRoles);
                        var url = "/event.updateroles";
                        dataSubmit.submitData($scope.toEditRoles, url).then(function (response) {
                            console.log(response);
                            if (response.data.message == "success") {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/editSuccessful.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                })
                            } else {
                                $scope.errorMessages = response.data.errorMsg;
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/errorMessage.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                })
                            }
                        });
                    }

                }

                $scope.submitEditTeams = function () {
                    $scope.toEditTeams = {
                        'token': session.getSession('token'),
                        'event_id': eventId,
                        'teams': [],
                        'explain_if_other': '',
                        'remarks': ''
                    };
                    $scope.selectedTeams.forEach(function (tObj) {
                        if (tObj.selected == true) {
                            $scope.toEditTeams.teams.push(tObj.teamAffiliation);
                        }
                    });
                    if ($scope.otherTeam.selected == true) {
                        $scope.toEditTeams.teams.push('Other');
                        $scope.toEditTeams['explain_if_other'] = $scope.otherTeam['explain_if_other_team'];
                    }
                    $scope.toEditTeams.remarks = $scope.otherTeam['remarks'];
                    if ($scope.toEditTeams.teams.length == 0) {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/addEventAffiliationAtLeastOne.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        })
                    } else {
                        console.log($scope.toEditTeams);
                        var url = "/event.updateteamaffiliation";
                        dataSubmit.submitData($scope.toEditTeams, url).then(function (response) {
                            console.log(response);
                            if (response.data.message == "success") {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/editSuccessful.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                })
                            } else {
                                $scope.errorMessages = response.data.errorMsg;
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/errorMessage.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                })
                            }
                        });
                    }
                };
            }]);