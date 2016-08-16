/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewPastIndivEvent',
        ['$scope', 'session', '$state', 'filterFilter', 'ngDialog', 'dataSubmit', '$timeout', 'localStorageService', 'deleteService', '$uibModal', '$rootScope',
            function ($scope, session, $state, filterFilter, ngDialog, dataSubmit, $timeout, localStorageService, deleteService, $uibModal, $rootScope) {
                var user = session.getSession('userType');
                var eventId = session.getSession('eventIdToDisplay');
                $scope.backHome = function () {
                    $state.go(user);
                };

                $scope.toEvents = function () {
                    var url = user + '.viewUpcomingEvents';
                    $state.go(url);
                };
                
                $scope.toContact = function($event, part){
                    var url = user + '.viewIndivContact';
                    session.setSession('contactToDisplayCid', part['contact_id']);
                    $state.go(url);
                };
                
                $scope.retrieveEvent = function () {
                    $scope.toRetrieve = {
                        'token': session.getSession('token'),
                        'eventId': eventId
                    }
                    var url = '/event.retrieveindiv';
                    $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                        $scope.eventInfo = response.data;
                        $scope.showGmap = true;
                        if ($scope.eventInfo['event_lat'] == '' || $scope.eventInfo['event_lng'] == '') {
                            $scope.showGmap = false;
                        } else {
                            $scope.map = {center: {latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']}, zoom: 15, options: {scrollwheel: false}, control: {}};

                            $scope.marker = {coords: {latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']}, id: 1};
                        }
                        $scope.roles = $scope.eventInfo['event_role'];
                        $scope.participants = $scope.eventInfo['event_participant'];
                        $scope.withdrawn = $scope.eventInfo['withdrawn_participants'];
                        $scope.affiliation = $scope.eventInfo['event_team_affiliation'];
                        $scope.teamA = $scope.eventInfo['event_team_affiliation']['teams_affiliated'];
                    })

                    $scope.$watch('showGmap', function () {
                        if ($scope.showGmap == true) {
                            $timeout(function () {
                                $scope.map.control.refresh({latitude: $scope.eventInfo['event_lat'], longitude: $scope.eventInfo['event_lng']});
                                $scope.map.zoom = 15;
                            }, 0);
                        }
                    })
                }

                $scope.editEvent = function () {
                    var url = user + '.editEvent';
                    localStorageService.set('eventId', eventId);
                    $state.go(url);
                };

                $scope.joinRole = function ($event, role) {
                    $scope.roleName = role['event_role'];
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/joinRolePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        $scope.toJoin = {
                            'token': session.getSession('token'),
                            'event_id': eventId,
                            'event_role_id': role['event_role_id']
                        };
                        var urlToJoin = '/event.join';
                        dataSubmit.submitData($scope.toJoin, urlToJoin).then(function (response) {
                            if (response.data.message == "success") {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/joinRoleSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    var current = user + '.viewIndivEvent';
                                    $state.go(current, {}, {reload: true});
                                })
                            }
                        })
                    })
                };

                $scope.deleteRole = function ($event, role) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        $scope.toDelete = {
                            'token': session.getSession('token'),
                            'event_id': eventId,
                            'event_role_id': role['event_role_id'],
                            'ignore': false
                        }
                        var url = '/event.deleterole';
                        deleteService.deleteDataService($scope.toDelete, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    var current = user + '.viewIndivEvent';
                                    $state.go(current, {}, {reload: true});
                                })
                            } else if (response.data.message == 'Has participants') {
                                $scope.errorMessages = response.data.errorMsg;
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/addEventConflict.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    $scope.toDelete.ignore = true;
                                    deleteService.deleteDataService($scope.toDelete, url).then(function (response) {
                                        var current = user + '.viewIndivEvent';
                                        if (response.data.message == 'success') {
                                            $state.go(current, {}, {reload: true});
                                        }
                                    });
                                });
                            } else {
                                window.alert("Unable to establish connection");
                            }
                        })
                    })
                }

                $scope.removeParticipant = function ($event, participant) {
                    $rootScope.participant = participant;
                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: './style/ngTemplate/removeReason.html',
                        controller: 'ReasonIndivInstanceCtrl',
                        size: "md"
                    });
                };
                
//                $scope.revert = function($event, wp){
//                    ngDialog.openConfirm({
//                        template: './style/ngTemplate/revertPrompt.html',
//                        className: 'ngdialog-theme-default',
//                        scope: $scope
//                    }).then(function(response){
//                        $scope.toRevert = {
//                        'token': session.getSession('token'),
//                        'event_id': eventId,
//                        'role_id': wp['role_id'],
//                        'contact_id': wp['contact_id']
//                    };
//                    var urlToRevert = '/event.revertrole';
//                    dataSubmit.submitData($scope.toRevert, urlToRevert).then(function (response) {
//                            if (response.data.message == "success") {
//                                ngDialog.openConfirm({
//                                    template: './style/ngTemplate/revertRoleSuccess.html',
//                                    className: 'ngdialog-theme-default',
//                                    scope: $scope
//                                }).then(function (response) {
//                                    var current = user + '.viewIndivEvent';
//                                    $state.go(current, {eventId: eventId}, {reload: true});
//                                })
//                            }else if(response.data.message="error"){
//                                $scope.errorMessages = response.data.errorMsg;
//                                ngDialog.openConfirm({
//                                    template: './style/ngTemplate/errorMessage.html',
//                                    className: 'ngdialog-theme-default',
//                                    scope: $scope
//                                })
//                            }
//                        })
//                    })
//                };
            }]);

app.controller('ReasonIndivInstanceCtrl', function ($scope, $rootScope, $uibModalInstance, dataSubmit, session, ngDialog, $state) {
    $scope.ok = function () {
        var part = $rootScope.participant;
        if(angular.isUndefined($scope.input)){
            $scope.input = "";
        };
        $scope.toRemovePart = {
            'token': session.getSession('token'),
            'role_id': part['role_id'],
            'reason': $scope.input,
            'withdraw_contact_id': part['contact_id']
        };
        var urlToRemove = '/event.leaverole';
        dataSubmit.submitData($scope.toRemovePart, urlToRemove).then(function (response) {
            if (response.data.message == 'success') {
                ngDialog.openConfirm({
                    template: './style/ngTemplate/removeSuccessful.html',
                    className: 'ngdialog-theme-default',
                    scope: $scope
                }).then(function (response) {
                    $uibModalInstance.dismiss('cancel');
                    var current = session.getSession('userType') + '.viewIndivEvent';
                    $state.go(current, {}, {reload: true});
                })
            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});