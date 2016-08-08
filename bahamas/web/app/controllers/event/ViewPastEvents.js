/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewPastEvents',
        ['$scope', 'session', '$state', 'filterFilter', 'ngDialog', 'dataSubmit', 'deleteService', 'localStorageService',
            function ($scope, session, $state, filterFilter, ngDialog, dataSubmit, deleteService, localStorageService) {
                var user = session.getSession('userType');
                $scope.backHome = function () {
                    $state.go(user);
                };

                $scope.retrieveEvents = function () {
                    $scope.toRetrieve = {
                        'token': session.getSession('token')
                    };
                    var url = '/event.retrieveall';
                    $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                        $scope.allEventInfo = response.data.event;
                        $scope.totalItems = $scope.allEventInfo.length;

                        $scope.currentPage = 1;
                        $scope.itemsPerPage = $scope.allEventInfo.length;
                        $scope.allFilteredEvents = $scope.allEventInfo;
                        $scope.isAll = false;
                        var total = $scope.allFilteredEvents.length / $scope.itemsPerPage;
                        $scope.totalPages = Math.ceil(total);
                        if ($scope.totalPages === 0) {
                            $scope.totalPages = 1;
                        }
                        $scope.itemsPerPageChanged = function () {
                            if ($scope.itemsPerPage == 'toAll') {
                                $scope.itemsPerPage = $scope.allFilteredEvents.length;
                                $scope.isAll = true;
                            } else {
                                $scope.isAll = false;
                            }
                            $scope.allFilteredEvents = filterFilter($scope.allEventInfo, $scope.searchEvents);
                            var total = $scope.allFilteredEvents.length / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            if ($scope.totalPages === 0) {
                                $scope.totalPages = 1;
                            }
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.splitEvents = $scope.allFilteredEvents.slice(begin, end);
                            });
                        };

                        $scope.$watch('currentPage + itemsPerPage', function () {
                            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                            var end = begin + parseInt($scope.itemsPerPage);

                            $scope.splitEvents = $scope.allFilteredEvents.slice(begin, end);
                        });

                        $scope.$watch('searchEvents', function (term) {
                            $scope.allFilteredEvents = filterFilter($scope.allEventInfo, term);
                            $scope.totalItems = $scope.allFilteredEvents.length;
                            var total = $scope.allFilteredEvents.length / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            if ($scope.totalPages === 0) {
                                $scope.totalPages = 1;
                            }
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.splitEvents = $scope.allFilteredEvents.slice(begin, end);
                            });
                        });
                    })
                }

                $scope.foo = function ($event, event) {
                      var url = user + '.viewIndivEvent';
                      var eventid = event['event_id'];
                      $state.go(url, {eventId: eventid});
                };
                
                $scope.edit = function($event, event){
                    var url = user + '.editEvent';
                    var eventid = event['event_id'];
                    localStorageService.set('eventId', eventid);
                    $state.go(url);
                };
                
                $scope.deleteEvent = function ($event, event) {
                            var toURL = user + ".viewPastEvents";
                            var eventid = event['event_id'];
                            
                            var toDelete = {
                                'token': session.getSession('token'),
                                'event_id': eventid
                            }
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/deletePrompt.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            }).then(function (response) {
                                deleteService.deleteDataService(toDelete, '/event.delete').then(function (response) {
                                    if (response.data.message === "success") {
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/deleteSuccess.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        }).then(function (response) {
                                            $state.reload(toURL);
                                        })
                                    } else {
                                        $scope.error = response.data.message;
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/deleteFailure.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        }).then(function (response) {
                                            $state.reload(toURL);
                                        });
                                    }
                                });
                            });
                        };
                
                $scope.predicate = '';
                $scope.reverse = true;

                $scope.order = function (predicate) {
                    $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
                    $scope.predicate = predicate;
                };
            }]);