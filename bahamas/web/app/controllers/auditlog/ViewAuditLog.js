/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.filter('dateFilter', function () {
    return function (input, dateStart, dateEnd) {
        var retArray = [];
        angular.forEach(input, function (obj) {
            var receivedDate = obj.date;
            var parsedDate = new Date(receivedDate);
            if (parsedDate >= dateStart && parsedDate <= dateEnd) {
                retArray.push(obj);
            }
        });
        return retArray;
    };
});

app.controller('viewAuditLog',
        ['filterFilter', 'dateFilter', '$scope', '$http', '$location', 'session', '$window', '$state', '$log', 'loadAllAudit',
            function (filterFilter, dateFilter, $scope, $http, $location, session, $window, $state, $log, loadAllAudit) {

                $scope.backHome = function () {
                    $state.go('admin.homepage');
                };

                $scope.retrieveAllAuditLog = function () {
                    var auditToRetrieve = {
                        'token': session.getSession('token'),
                        'cid': angular.fromJson(session.getSession('contact')).cid
                    };
                    var allAuditObjKey = [];
                    $scope.myPromise = loadAllAudit.retrieveAllAudit(auditToRetrieve).then(function (response) {
                        //to get the headers for auditlog
                        $scope.allAuditInfo = response.data.auditlog;
                        var firstAuditObject = $scope.allAuditInfo[0];
                        for (auditHeader in firstAuditObject) {
                            allAuditObjKey.push(auditHeader);
                        }
                        $scope.allAuditObjectKeys = allAuditObjKey;
                        //end of getting headers for auditlog
                        $scope.allFiltered = $scope.allAuditInfo;
                        $scope.totalFilteredItems = $scope.allFiltered.length;
                        $scope.currentPage = 1;
                        $scope.itemsPerPage = 30;
                        var total = $scope.totalFilteredItems / $scope.itemsPerPage;
                        $scope.totalPages = Math.ceil(total);
                        if ($scope.totalPages === 0) {
                            $scope.totalPages = 1;
                        }
                        $scope.$watch('currentPage + itemsPerPage', function () {
                            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                            var end = begin + parseInt($scope.itemsPerPage);

                            $scope.filteredAudit = $scope.allFiltered.slice(begin, end);
                        });
                        $scope.isAll = false;
                        $scope.itemsPerPageChanged = function () {
                            if ($scope.itemsPerPage == 'toAll') {
                                $scope.itemsPerPage = $scope.allFiltered.length;
                                $scope.isAll = true;
                            } else {
                                $scope.isAll = false;
                            }
                            var newArray = [];
                            angular.forEach($scope.allFiltered, function (obj) {
                                var receivedDate = obj.date;
                                var parsedDate = new Date(receivedDate);
                                if (parsedDate >= $scope.dateStart && parsedDate <= $scope.dateEnd) {
                                    newArray.push(obj);
                                }
                            });
                            $scope.filtered = newArray;
                            $scope.searchFiltered = filterFilter($scope.filtered, $scope.searchAudit);
                            $scope.totalFilteredItems = $scope.searchFiltered.length;
                            var total = $scope.totalFilteredItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            if ($scope.totalPages === 0) {
                                $scope.totalPages = 1;
                            }
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.filteredAudit = $scope.searchFiltered.slice(begin, end);
                            });
                        };

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
                            angular.forEach($scope.allFiltered, function (obj) {
                                var receivedDate = obj.date;
                                var parsedDate = new Date(receivedDate);
                                if (parsedDate >= $scope.dateStart && parsedDate <= $scope.dateEnd) {
                                    newArray.push(obj);
                                }
                            });
                            $scope.filtered = newArray;
                            $scope.searchFiltered = filterFilter($scope.filtered, $scope.searchAudit);
                            $scope.totalFilteredItems = $scope.searchFiltered.length;
                            var total = $scope.totalFilteredItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            if ($scope.totalPages === 0) {
                                $scope.totalPages = 1;
                            }
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.filteredAudit = $scope.searchFiltered.slice(begin, end);
                            });
                        });

                        $scope.$watch('searchAudit', function (term) {
                            var newArray = [];
                            angular.forEach($scope.allFiltered, function (obj) {
                                var receivedDate = obj.date;
                                var parsedDate = new Date(receivedDate);
                                if (parsedDate >= $scope.dateStart && parsedDate <= $scope.dateEnd) {
                                    newArray.push(obj);
                                }
                            });
                            $scope.filtered = newArray;
                            $scope.searchFiltered = filterFilter($scope.filtered, term);
                            $scope.totalFilteredItems = $scope.searchFiltered.length;
                            var total = $scope.totalFilteredItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            if ($scope.totalPages === 0) {
                                $scope.totalPages = 1;
                            }
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.filteredAudit = $scope.searchFiltered.slice(begin, end);
                            });
                        });



                    });

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
                };
            }]);