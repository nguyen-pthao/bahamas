/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('verifiedUsersCtrl', ['$scope', 'session', 'filterFilter', '$state', 'ngDialog', 'dataSubmit', '$timeout', 'localStorageService', '$uibModal', function ($scope, session, filterFilter, $state, ngDialog, dataSubmit, $timeout, localStorageService, $uibModal) {

        var user = session.getSession('userType');
        $scope.authorised = false;
        if (user == "admin") {
            $scope.authorised = true;
        };



        $scope.backHome = function () {
            $state.go(user);
        };

        $scope.temp1Date = "";
        $scope.temp2Date = "";

        $scope.temp1Changed = function () {
            localStorageService.set('verifiedUsersFilter1', $scope.temp1Date);
            $scope.retrieveList();
        };
        $scope.temp2Changed = function () {
            localStorageService.set('verifiedUsersFilter2', $scope.temp2Date);
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

        $scope.showAssignModal = function (user) {
            $scope.selectedUser = user;
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: './style/ngTemplate/assignUsernamePassword.html',
                scope: $scope,
                controller: function () {
                    $scope.username = "";
                    $scope.password = "";
                    $scope.generatePassword = function () {
                        var a = Math.floor((Math.random() * 10) + 10);
                        $scope.password = Math.random().toString(36).substring(2, a);
                    };
                    $scope.ok = function () {
                        $scope.toSubmit = {
                            'contact_id': $scope.selectedUser.cid,
                            'email': $scope.selectedUser.email,
                            'password': $scope.password,
                            'token': session.getSession('token'),
                            'user_type': $scope.userType,
                            'username': $scope.username
                        };
                        var url = '/user.update';
                        dataSubmit.submitData($scope.toSubmit, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/editSuccessful.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function(response){
                                   $state.reload();
                                });
                            } else {
                                alert('bye');
                            }
                        })
                    };
                    $scope.cancel = function () {
                        modalInstance.dismiss('cancel');
                    };
                },
                backdrop: 'static',
                keyboard: false,
                size: "md"
            });
        };

        $scope.retrieveList = function () {
            if (localStorageService.get('verifiedUsersFilter1') == null || angular.isUndefined(localStorageService.get('verifiedUsersFilter1'))) {
                $scope.temp1Date = "";
            } else {
                $scope.temp1Date = new Date(localStorageService.get('verifiedUsersFilter1'));
            }
            ;
            if (localStorageService.get('verifiedUsersFilter2') == null || angular.isUndefined(localStorageService.get('verifiedUsersFilter2'))) {
                $scope.temp2Date = "";
            } else {
                $scope.temp2Date = new Date(localStorageService.get('verifiedUsersFilter2'));
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
            var url = '/usermanagement.retrieveverifiedcontacts';
            $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
                $scope.allUsers = response.data.user;
                $scope.totalItems = $scope.allUsers.length;
                $scope.maxSize = 5;
                $scope.currentPage = 1;
                $scope.itemsPerPage = 100;
                $scope.allFilteredUsers = $scope.allUsers;
                $scope.isAll = false;
                var total = $scope.allFilteredUsers.length / $scope.itemsPerPage;
                $scope.totalPages = Math.ceil(total);
                if ($scope.totalPages === 0) {
                    $scope.totalPages = 1;
                }
                $scope.$watch('currentPage + itemsPerPage', function () {
                    var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                    var end = begin + parseInt($scope.itemsPerPage);

                    $scope.splitUsers = $scope.allFilteredUsers.slice(begin, end);
                });
                $scope.itemsPerPageChanged = function () {
                    if ($scope.itemsPerPage == 'toAll') {
                        $scope.itemsPerPage = $scope.allFilteredUsers.length;
                        $scope.isAll = true;
                    } else {
                        $scope.isAll = false;
                    }
                    $scope.allFilteredUsers = filterFilter($scope.allUsers, $scope.searchUsers);
                    var total = $scope.allFilteredUsers.length / $scope.itemsPerPage;
                    $scope.totalPages = Math.ceil(total);
                    if ($scope.totalPages === 0) {
                        $scope.totalPages = 1;
                    }
                    $scope.$watch('currentPage + itemsPerPage', function () {
                        var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                        var end = begin + parseInt($scope.itemsPerPage);
                        $scope.splitUsers = $scope.allFilteredUsers.slice(begin, end);
                    });
                };
                $scope.$watch('searchUsers', function (term) {
                    $scope.allFilteredUsers = filterFilter($scope.allUsers, term);
                    $scope.totalItems = $scope.allFilteredUsers.length;
                    var total = $scope.allFilteredUsers.length / $scope.itemsPerPage;
                    $scope.totalPages = Math.ceil(total);
                    if ($scope.totalPages === 0) {
                        $scope.totalPages = 1;
                    }
                    $scope.$watch('currentPage + itemsPerPage', function () {
                        var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                        var end = begin + parseInt($scope.itemsPerPage);
                        $scope.splitUsers = $scope.allFilteredUsers.slice(begin, end);
                    });
                });
            });

            $scope.predicate = '';
            $scope.reverse = false;

            $scope.order = function (predicate) {
                $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
                $scope.predicate = predicate;
            };

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