/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('pastEventsPS',
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
                    var url = '/event.pastparticipants';
                    $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                        $scope.allEventInfo = response.data.event;
                        console.log($scope.allEventInfo);
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
                        $scope.$watch('currentPage + itemsPerPage', function () {
                            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                            var end = begin + parseInt($scope.itemsPerPage);

                            $scope.splitEvents = $scope.allFilteredEvents.slice(begin, end);
                        });

                        $scope.itemsPerPageChanged = function () {
                            if ($scope.itemsPerPage == 'toAll') {
                                $scope.itemsPerPage = $scope.allFilteredEvents.length;
                                $scope.isAll = true;
                            } else {
                                $scope.isAll = false;
                            }

                            var newArray = [];
                            angular.forEach($scope.allEventInfo, function (obj) {
                                var startDate = new Date(obj['event_start_date']);
                                if (startDate >= $scope.dateStart && startDate <= $scope.dateEnd) {
                                    newArray.push(obj);
                                }
                            });
                            $scope.allFilteredEventsTime = newArray;
                            $scope.allFilteredEvents = filterFilter($scope.allFilteredEventsTime, $scope.searchEvents);
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

                        $scope.$watch('searchEvents', function (term) {
                            var newArray = [];
                            angular.forEach($scope.allEventInfo, function (obj) {
                                var startDate = new Date(obj['event_start_date']);
                                if (startDate >= $scope.dateStart && startDate <= $scope.dateEnd) {
                                    newArray.push(obj);
                                }
                            });
                            $scope.allFilteredEventsTime = newArray;

                            $scope.allFilteredEvents = filterFilter($scope.allFilteredEventsTime, term);
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

                        $scope.$watch('dateStart + dateEnd', function () {
                            var newArray = [];
                            if (angular.isUndefined($scope.dateStart)) {
                                $scope.dateStart = null;
                            }
                            if (angular.isUndefined($scope.dateEnd)) {
                                var end = new Date();
                                end.setHours(23, 59, 59, 999);
                                $scope.dateEnd = end;
                            }
                            angular.forEach($scope.allEventInfo, function (obj) {
                                var startDate = new Date(obj['event_start_date']);
                                if (startDate >= $scope.dateStart && startDate <= $scope.dateEnd) {
                                    newArray.push(obj);
                                }
                            });
                            $scope.allFilteredEventsTime = newArray;
                            $scope.allFilteredEvents = filterFilter($scope.allFilteredEventsTime, $scope.searchEvents);
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
                    var end = new Date();
                    end.setHours(23, 59, 59, 999);
                    var d = null;
                    $scope.dateStart = d;
                    $scope.dateEnd = end;

                    $scope.today = function () {
                        $scope.dt = new Date();
                    };
                    $scope.today();

                    $scope.clear = function () {
                        $scope.dt = null;
                    };

                    $scope.inlineOptions = {
                        customClass: getDayClass,
                        minDate: new Date(),
                        showWeeks: true
                    };

                    $scope.dateOptions = {
                        formatYear: 'yy',
                        minDate: new Date(),
                        startingDay: 1
                    };

                    $scope.toggleMin = function () {
                        $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
                        $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
                    };

                    $scope.toggleMin();

                    $scope.open1 = function () {
                        $scope.popup1.opened = true;
                    };

                    $scope.open2 = function () {
                        $scope.popup2.opened = true;
                    };

                    $scope.setDate = function (year, month, day) {
                        $scope.dt = new Date(year, month, day);
                    };


                    $scope.formats = ['dd MMM yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];

                    $scope.format = $scope.formats[0];
                    $scope.altInputFormats = ['M!/d!/yyyy'];

                    $scope.popup1 = {
                        opened: false
                    };

                    $scope.popup2 = {
                        opened: false
                    };
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
                }

                $scope.foo = function ($event, event) {
                    var url = user + '.viewIndivEvent';
                    var eventid = event['event_id'];
                    $state.go(url, {eventId: eventid});
                };

                $scope.edit = function ($event, event) {
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