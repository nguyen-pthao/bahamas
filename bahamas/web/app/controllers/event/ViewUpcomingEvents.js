/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewUpcomingEvents',
        ['$scope', 'session', '$state', 'filterFilter', 'ngDialog', 'dataSubmit', 'localStorageService', 'deleteService', 'loadTeamAffiliation',
            function ($scope, session, $state, filterFilter, ngDialog, dataSubmit, localStorageService, deleteService, loadTeamAffiliation) {
                var user = session.getSession('userType');
                $scope.isNovice = false;
                if(user=="novice"){
                    $scope.isNovice = true;
                }
                $scope.canClone = true;
                if(user == "novice" || user == "associate"){
                    $scope.canClone = false;
                }
                
                $scope.backHome = function () {
                    $state.go(user);
                };
                
                $scope.teamFilterChanged = function(){
                    localStorageService.set('UpcomingEventsTeamFilter', $scope.teamFilter);
                    $scope.retrieveEvents();
                };
                
                $scope.retrieveEvents = function () {
                    $scope.teamFilter = localStorageService.get('UpcomingEventsTeamFilter');
                    if($scope.teamFilter == null){
                        $scope.teamFilter = "MY TEAMS";
                    }
                    loadTeamAffiliation.retrieveTeamAffiliation().then(function(response){
                        $scope.teamList = response.data.teamAffiliationList;
                        $scope.teamList.unshift({'teamAffiliation': 'MY TEAMS'});
                        $scope.teamList.unshift({'teamAffiliation': 'ALL'});
                    })
                    var filter;
                    if($scope.teamFilter == "ALL"){
                        filter = "";
                    }else if($scope.teamFilter == "MY TEAMS"){
                        filter = "my_team";
                    }else{
                        filter = $scope.teamFilter;
                    }
                    $scope.toRetrieve = {
                        'token': session.getSession('token'),
                        'teamFilter': filter
                    };
                    var url = '/event.retrieveupcoming';
                    $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                        $scope.allEventInfo = response.data.event;
                        $scope.filteredEvents = $scope.allEventInfo;
                        $scope.totalItems = $scope.filteredEvents.length;
                        $scope.currentPage = 0;
                        $scope.currentPageIncrement = 1;
                        $scope.$watch('currentPageIncrement', function () {
                            $scope.currentPage = $scope.currentPageIncrement - 1;
                        });
                        $scope.itemsPerPage = 100;
                        $scope.maxSize = 5;
                        $scope.propertyName = '';
                        $scope.reverse = true;
                        $scope.sortBy = function (propertyName) {
                            $scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse : false;
                            $scope.propertyName = propertyName;
                        };
                        $scope.totalPages = function () {
                            return Math.ceil($scope.filteredEvents.length / $scope.itemsPerPage);
                        };
                        $scope.itemsPerPageChanged = function () {
                            if ($scope.itemsPerPage == 'toAll') {
                                $scope.itemsPerPage = $scope.filteredEvents.length;
                                $scope.isAll = true;
                            } else {
                                $scope.isAll = false;
                            }
                        };
                        $scope.$watch('searchEvents', function (term) {
                            $scope.allEventInfoTemp = $scope.allEventInfo;
                            $scope.filteredEvents = filterFilter($scope.allEventInfoTemp, term);
                            $scope.totalItems = $scope.filteredEvents.length;
                        });
                        
                        $scope.$watch('dateStart + dateEnd', function () {
                            var newArray = [];
                            if (angular.isUndefined($scope.dateStart)) {
                                var start = null;
                                $scope.dateStart = start;
                            }
                            if (angular.isUndefined($scope.dateEnd) || $scope.dateEnd === null) {
                                $scope.superFarDate = new Date(2050, 0, 1, 00, 00, 00, 0);
                                angular.forEach($scope.allEventInfo, function (obj) {
                                    var startDate = new Date(obj['event_start_date']);
                                    if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
                                        newArray.push(obj);
                                    }
                                });
                            } else {
                                $scope.superFarDate = $scope.dateEnd;
                                angular.forEach($scope.allEventInfo, function (obj) {
                                    var startDate = new Date(obj['event_start_date']);
                                    if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
                                        newArray.push(obj);
                                    }
                                });
                            }
                            $scope.allFilteredEventsTime = newArray;
                            $scope.filteredEvents = filterFilter($scope.allFilteredEventsTime, $scope.searchEvents);
                            $scope.totalItems = $scope.filteredEvents.length;
                        });
                        
//                        $scope.allEventInfo = response.data.event;
//                        $scope.totalItems = $scope.allEventInfo.length;
//                        $scope.maxSize = 5;
//                        $scope.currentPage = 1;
//                        $scope.itemsPerPage = 50;
//                        $scope.allFilteredEvents = $scope.allEventInfo;
//                        $scope.allFilteredEvents.reverse();
//                        $scope.isAll = false;
//                        var total = $scope.allFilteredEvents.length / $scope.itemsPerPage;
//                        $scope.totalPages = Math.ceil(total);
//                        if ($scope.totalPages === 0) {
//                            $scope.totalPages = 1;
//                        }
//                        $scope.$watch('currentPage + itemsPerPage', function () {
//                            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
//                            var end = begin + parseInt($scope.itemsPerPage);
//                            $scope.splitEvents = $scope.allFilteredEvents.slice(begin, end);
//                        });
//                        
//                        $scope.itemsPerPageChanged = function () {
//                            if ($scope.itemsPerPage == 'toAll') {
//                                $scope.itemsPerPage = $scope.allFilteredEvents.length;
//                                $scope.isAll = true;
//                            } else {
//                                $scope.isAll = false;
//                            }
//                            var newArray = [];
//                            
//                            angular.forEach($scope.allEventInfo, function (obj) {
//                                var startDate = new Date(obj['event_start_date']);
//                                if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
//                                    newArray.push(obj);
//                                }
//                            });
//                            $scope.allFilteredEventsTime = newArray;
//                            $scope.allFilteredEvents = filterFilter($scope.allFilteredEventsTime, $scope.searchEvents);
//                            var total = $scope.allFilteredEvents.length / $scope.itemsPerPage;
//                            $scope.totalPages = Math.ceil(total);
//                            if ($scope.totalPages === 0) {
//                                $scope.totalPages = 1;
//                            }
//                            $scope.$watch('currentPage + itemsPerPage', function () {
//                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
//                                var end = begin + parseInt($scope.itemsPerPage);
//                                $scope.splitEvents = $scope.allFilteredEvents.slice(begin, end);
//                            });
//                        };
//
//                        $scope.$watch('searchEvents', function (term) {
//                            var newArray = [];
//                            angular.forEach($scope.allEventInfo, function (obj) {
//                                var startDate = new Date(obj['event_start_date']);
//                                if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
//                                    newArray.push(obj);
//                                }
//                            });
//                            $scope.allFilteredEventsTime = newArray;
//                            $scope.allFilteredEvents = filterFilter($scope.allFilteredEventsTime, term);
//                            $scope.totalItems = $scope.allFilteredEvents.length;
//                            var total = $scope.allFilteredEvents.length / $scope.itemsPerPage;
//                            $scope.totalPages = Math.ceil(total);
//                            if ($scope.totalPages === 0) {
//                                $scope.totalPages = 1;
//                            }
//                            $scope.$watch('currentPage + itemsPerPage', function () {
//                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
//                                var end = begin + parseInt($scope.itemsPerPage);
//                                $scope.splitEvents = $scope.allFilteredEvents.slice(begin, end);
//                            });
//                        });
//
//                        $scope.$watch('dateStart + dateEnd', function () {
//                            var newArray = [];
//                            if (angular.isUndefined($scope.dateStart)) {
//                                var start = null;
//                                $scope.dateStart = start;
//                            }
//                            if (angular.isUndefined($scope.dateEnd) || $scope.dateEnd === null) {
//                                $scope.superFarDate = new Date(2050, 0, 1, 00, 00, 00, 0);
//                                angular.forEach($scope.allEventInfo, function (obj) {
//                                    var startDate = new Date(obj['event_start_date']);
//                                    if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
//                                        newArray.push(obj);
//                                    }
//                                });
//                            } else {
//                                $scope.superFarDate = $scope.dateEnd;
//                                angular.forEach($scope.allEventInfo, function (obj) {
//                                    var startDate = new Date(obj['event_start_date']);
//                                    if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
//                                        newArray.push(obj);
//                                    }
//                                });
//                            }
//                            $scope.allFilteredEventsTime = newArray;
//                            $scope.allFilteredEvents = filterFilter($scope.allFilteredEventsTime, $scope.searchEvents);
//                            var total = $scope.allFilteredEvents.length / $scope.itemsPerPage;
//                            $scope.totalPages = Math.ceil(total);
//                            if ($scope.totalPages === 0) {
//                                $scope.totalPages = 1;
//                            }
//                            $scope.$watch('currentPage + itemsPerPage', function () {
//                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
//                                var end = begin + parseInt($scope.itemsPerPage);
//                                $scope.splitEvents = $scope.allFilteredEvents.slice(begin, end);
//                            });
//                        });
                    })
                    var start = null;
//                    start.setHours(00, 00, 00, 000);
                    var d = null;
                    $scope.superFarDate = new Date(2050, 0, 1, 00, 00, 00, 0);
                    $scope.dateStart = start;
                    $scope.dateEnd = d;

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
                };
                
                $scope.deleteEvent = function ($event, event) {
                    var toURL = user + ".viewUpcomingEvents";
                    var eventid = event['event_id'];

                    var toDelete = {
                        'token': session.getSession('token'),
                        'event_id': eventid
                    };
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
                                });
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

                $scope.viewEvent = function ($event, event) {
                    var url = user + '.viewIndivEvent';
                    $scope.viewIndivEvent = user + '/viewIndivEvent';
                    session.setSession('eventIdToDisplay', event['event_id']);
                    if($event.which == 1) {
                        $state.go(url);
                    }
                };
                
                $scope.cloneEvent = function($event, event){
                    var id = event['event_id'];
                    localStorageService.set('eventIdToClone', id);
                    var url = user + '.cloneEvent';
                    $scope.cloneIndivEvent = user + '/cloneEvent';
                    if($event.which == 1) {
                        $state.go(url);
                    }
                };
                
                $scope.edit = function ($event, event) {
                    var url = user + '.editEvent';
                    $scope.editIndivEvent = user + '/editEvent';
                    var eventid = event['event_id'];
                    localStorageService.set('eventId', eventid);
                    if($event.which == 1) {
                        $state.go(url);
                    }
                };
            }]);
 
app.filter('startFrom', function () {
    return function (input, start) {
        start = +start; //parse to int
        if (angular.isUndefined(input)) {
            return null;
        } else {
            return input.slice(start);
        }
    }
});     