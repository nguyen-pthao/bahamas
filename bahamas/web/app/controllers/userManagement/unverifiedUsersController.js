/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('unverifiedUsersCtrl', ['$scope', 'session', 'filterFilter', '$state', 'ngDialog', 'dataSubmit', '$timeout', 'localStorageService', function ($scope, session, filterFilter, $state, ngDialog, dataSubmit, $timeout, localStorageService) {

        var user = session.getSession('userType');
        $scope.authorised = false;
        if (user == "admin") {
            $scope.authorised = true;
        };

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

        $scope.temp1Changed = function () {
            localStorageService.set('unverifiedUsersFilter1', $scope.temp1Date);
            $scope.retrieveList();
        };
        $scope.temp2Changed = function () {
            localStorageService.set('unverifiedUsersFilter2', $scope.temp2Date);
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

        $scope.resendEmail = function () {
            $scope.toResendList = [];
            $scope.toResendNameList = [];
            angular.forEach($scope.userObj, function (value, key) {
                if (value == true) {
//                    var keyString = key + "";
//                    $scope.toResendList.push(keyString);
                    var emailKey = $scope.userObjEmail[key];
                    $scope.toResendList.push(emailKey);
                    var nameDisplay = $scope.userObjName[key];
                    $scope.toResendNameList.push(nameDisplay);
                }
            });
            $scope.toResend = {
                'token': session.getSession('token'),
                'email_list': $scope.toResendList
            };
            var url = '/email.resendverification';
            ngDialog.openConfirm({
                template: './style/ngTemplate/resendConfirm.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                $scope.myPromise = dataSubmit.submitData($scope.toResend, url).then(function (response) {
                    if (response.data.message == "success") {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/resendSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function (response) {
                            $state.reload();
                        });
                    } else {
                        //display error message;
                    };
                });
            })
        };

        $scope.retrieveList = function () {
            if (localStorageService.get('unverifiedUsersFilter1') == null || angular.isUndefined(localStorageService.get('unverifiedUsersFilter1'))) {
                $scope.temp1Date = "";
            } else {
                $scope.temp1Date = new Date(localStorageService.get('unverifiedUsersFilter1'));
            }
            ;
            if (localStorageService.get('unverifiedUsersFilter2') == null || angular.isUndefined(localStorageService.get('unverifiedUsersFilter2'))) {
                $scope.temp2Date = "";
            } else {
                $scope.temp2Date = new Date(localStorageService.get('unverifiedUsersFilter2'));
            }
            ;
            $scope.toRetrieve = {
                "token": session.getSession('token'),
                "user_create_startdate": "",
                "user_create_enddate": ""
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
            var url = '/usermanagement.retrieveunverifiedcontacts';
            $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                $scope.allUsers = response.data.user;
                $scope.userObj = {};
                $scope.userObjName = {};
                $scope.userObjEmail = {};
                angular.forEach($scope.allUsers, function (obj) {
                    $scope.userObj[obj.cid] = false;
                    $scope.userObjName[obj.cid] = obj.name;
                    $scope.userObjEmail[obj.cid] = obj.email;
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