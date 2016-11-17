/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('userManagementCtrl', ['$scope', 'session', 'filterFilter', '$state', 'ngDialog', 'dataSubmit', '$timeout', 'localStorageService', function ($scope, session, filterFilter, $state, ngDialog, dataSubmit, $timeout, localStorageService) {

        var user = session.getSession('userType');
        $scope.authorised = false;
        if (user == "admin") {
            $scope.authorised = true;
        }
        ;

        $scope.checkAll = false;
        $scope.checkAllFn = function () {
            if ($scope.checkAll === true) {
                angular.forEach($scope.userObj, function (value, key) {
                    $scope.userObj[key] = true;
                });
            } else {
                angular.forEach($scope.userObj, function (value, key) {
                    $scope.userObj[key] = false;
                });
            }
        };

        $scope.backHome = function () {
            $state.go(user);
        };

        $scope.temp1Date = "";
        $scope.temp2Date = "";
        $scope.temp3Date = "";
        $scope.temp4Date = "";

        $scope.temp1Changed = function () {
            localStorageService.set('userMgmtFilter1', $scope.temp1Date);
            $scope.retrieveList();
        };
        $scope.temp2Changed = function () {
            localStorageService.set('userMgmtFilter2', $scope.temp2Date);
            $scope.retrieveList();
        };
        $scope.temp3Changed = function () {
            localStorageService.set('userMgmtFilter3', $scope.temp3Date);
            $scope.retrieveList();
        };
        $scope.temp4Changed = function () {
            localStorageService.set('userMgmtFilter4', $scope.temp4Date);
            $scope.retrieveList();
        };

        $scope.viewContact = function ($event, user) {
            var toURL = $scope.userType + ".viewIndivContact";
            $scope.viewIndivContact = $scope.userType + '/viewIndivContact';
            var contactCid = user.cid;
            session.setSession('contactToDisplayCid', contactCid);
            if ($event.which == 1) {
                $state.go(toURL);
            }
        };

        $scope.deactivateUsers = function () {
            $scope.toDeactivateList = [];
            $scope.toDeactivateNameList = [];
            angular.forEach($scope.userObj, function (value, key) {
                if (value == true) {
                    var keyString = key + "";
                    $scope.toDeactivateList.push(keyString);
                    var nameDisplay = $scope.userObjName[key];
                    $scope.toDeactivateNameList.push(nameDisplay);
                }
            });
            $scope.toDeactivate = {
                'token': session.getSession('token'),
                'contact_id_list': $scope.toDeactivateList
            };
            var url = '/user.deactivate';
            ngDialog.openConfirm({
                template: './style/ngTemplate/deactivationConfirm.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                $scope.myPromise = dataSubmit.submitData($scope.toDeactivate, url).then(function (response) {
                    if (response.data.message == "success") {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deactivationSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function (response) {
                            $state.reload();
                        });
                    } else {
                        //display error message;
                    }
                    ;
                });
            })
        };

        $scope.reactivateUsers = function () {
            $scope.toReactivateList = [];
            $scope.toReactivateNameList = [];
            angular.forEach($scope.userObj, function (value, key) {
                if (value == true) {
                    var keyString = key + "";
                    $scope.toReactivateList.push(keyString);
                    var nameDisplay = $scope.userObjName[key];
                    $scope.toReactivateNameList.push(nameDisplay);
                }
            });
            $scope.toReactivate = {
                'token': session.getSession('token'),
                'contact_id_list': $scope.toReactivateList
            };
            var url = '/user.activate';
            ngDialog.openConfirm({
                template: './style/ngTemplate/activationConfirm.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                $scope.myPromise = dataSubmit.submitData($scope.toReactivate, url).then(function (response) {
                    if (response.data.message == "success") {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/activationSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function (response) {
                            $state.reload();
                        });
                    } else {
                        //display error message;
                    }
                    ;
                });
            })
        };

        $scope.retrieveList = function () {
            if (localStorageService.get('userMgmtFilter1') == null || angular.isUndefined(localStorageService.get('userMgmtFilter1'))) {
                $scope.temp1Date = "";
            } else {
                $scope.temp1Date = new Date(localStorageService.get('userMgmtFilter1'));
            }
            ;
            if (localStorageService.get('userMgmtFilter2') == null || angular.isUndefined(localStorageService.get('userMgmtFilter2'))) {
                $scope.temp2Date = "";
            } else {
                $scope.temp2Date = new Date(localStorageService.get('userMgmtFilter2'));
            }
            ;
            if (localStorageService.get('userMgmtFilter3') == null || angular.isUndefined(localStorageService.get('userMgmtFilter3'))) {
                $scope.temp3Date = "";
            } else {
                $scope.temp3Date = new Date(localStorageService.get('userMgmtFilter3'));
            }
            ;
            if (localStorageService.get('userMgmtFilter4') == null || angular.isUndefined(localStorageService.get('userMgmtFilter4'))) {
                $scope.temp4Date = "";
            } else {
                $scope.temp4Date = new Date(localStorageService.get('userMgmtFilter4'));
            }
            ;
            $scope.toRetrieve = {
                "token": session.getSession('token'),
                "user_create_startdate": "",
                "user_create_enddate": "",
                "user_login_startdate": "",
                "user_login_enddate": ""
            };
            if ($scope.temp1Date == "" || angular.isUndefined($scope.temp1Date) || $scope.temp1Date == null) {
                $scope.toRetrieve.user_create_startdate = "";
            } else {
                $scope.toRetrieve.user_create_startdate = $scope.temp1Date.valueOf();
            }
            ;
            if ($scope.temp2Date == "" || angular.isUndefined($scope.temp2Date) || $scope.temp2Date == null) {
                $scope.toRetrieve.user_create_enddate = "";
            } else {
                $scope.toRetrieve.user_create_enddate = $scope.temp2Date.valueOf();
            }
            ;
            if ($scope.temp3Date == "" || angular.isUndefined($scope.temp3Date) || $scope.temp3Date == null) {
                $scope.toRetrieve.user_login_startdate = "";
            } else {
                $scope.toRetrieve.user_login_startdate = $scope.temp3Date.valueOf();
            }
            ;
            if ($scope.temp4Date == "" || angular.isUndefined($scope.temp4Date) || $scope.temp4Date == null) {
                $scope.toRetrieve.user_login_enddate = "";
            } else {
                $scope.toRetrieve.user_login_enddate = $scope.temp4Date.valueOf();
            }
            ;
            var url = '/usermanagement.retrieveusers';
            $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                $scope.allUsers = response.data.user;
                $scope.userObj = {};
                $scope.userObjName = {};
                angular.forEach($scope.allUsers, function (obj) {
                    $scope.userObj[obj.cid] = false;
                    $scope.userObjName[obj.cid] = obj.name;
                });
                $scope.filteredUsers = $scope.allUsers;
                $scope.totalItems = $scope.filteredUsers.length;
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
                    return Math.ceil($scope.filteredUsers.length / $scope.itemsPerPage);
                };
                $scope.itemsPerPageChanged = function () {
                    if ($scope.itemsPerPage == 'toAll') {
                        $scope.itemsPerPage = $scope.filteredUsers.length;
                        $scope.isAll = true;
                    } else {
                        $scope.isAll = false;
                    }
                };
                $scope.$watch('searchUsers', function (term) {
                    $scope.allUsersInfoTemp = $scope.allUsers;
                    $scope.filteredUsers = filterFilter($scope.allUsersInfoTemp, term);
                    $scope.totalItems = $scope.filteredUsers.length;
                });
            });

        };

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
            startingDay: 1
        };

        $scope.open1 = function () {
            $timeout(function () {
                $scope.opened1 = true;
            })
        }

        $scope.open2 = function () {
            $timeout(function () {
                $scope.opened2 = true;
            })
        }

        $scope.open3 = function () {
            $timeout(function () {
                $scope.opened3 = true;
            })
        }

        $scope.open4 = function () {
            $timeout(function () {
                $scope.opened4 = true;
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