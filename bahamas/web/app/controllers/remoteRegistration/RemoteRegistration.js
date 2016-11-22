/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('remoteRegistration', ['$scope', 'session', '$state', 'dataSubmit', '$uibModal', 'ngDialog', 'filterFilter', '$timeout',
    function ($scope, session, $state, dataSubmit, $uibModal, ngDialog, filterFilter, $timeout) {

        var token = session.getSession('token');
        $scope.permission = session.getSession('userType');

        $scope.dateStart = '';
        $scope.dateEnd = '';
        //datepicker
        $scope.today = function () {
            $scope.dt = new Date();
        };

        $scope.clear = function () {
            $scope.dt = null;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            formatMonth: 'MMM',
            formatDay: 'dd',
            startingDay: 1
        };

        $scope.format = 'dd MMM yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];

        $scope.openedStart = false;
        $scope.openStart = function () {
            $timeout(function () {
                $scope.openedStart = true;
            });
        };

        $scope.openedEnd = false;
        $scope.openEnd = function () {
            $timeout(function () {
                $scope.openedEnd = true;
            });
        };

        //orderBy for table header
        $scope.predicate = '';
        $scope.reverse = false;

        $scope.order = function (predicate) {
            $scope.reverse = ($scope.predicate === ('\u0022' + predicate + '\u0022')) ? !$scope.reverse : false;
            $scope.predicate = ('\u0022' + predicate + '\u0022');
            $scope.records = orderBy($scope.records, $scope.predicate, $scope.reverse);
        };

        //general datasend
        var datasend = {
            'token': token,
            'user_type': $scope.permission
        };
        //form retrieve
        if (session.getSession('registrationView') != null) {
            $scope.registrationChoice = 'retrieveCurrentRegistration';
        } else {
            if ($scope.permission != 'eventleader') {
                $scope.registrationChoice = 'retrieveRegistration';
            } else {
                $scope.registrationChoice = '';
            }
        }

        $scope.registrationRetrieve = function () {
            var url = AppAPI[$scope.registrationChoice];
            $scope.myPromise = dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultData = response.data.Records;
                if ($scope.resultData != '') {
                    $scope.tableHeader = Object.keys($scope.resultData[0]);

                    $scope.filteredRegistrations = $scope.resultData;
                    $scope.totalItems = $scope.filteredRegistrations.length;
                    $scope.currentPage = 0;
                    $scope.currentPageIncrement = 1;
                    $scope.$watch('currentPageIncrement', function () {
                        $scope.currentPage = $scope.currentPageIncrement - 1;
                    });
                    $scope.itemsPerPage = 100;
                    $scope.maxSize = 5;
                    $scope.propertyName = '';
                    $scope.reverse = true;

                    $scope.totalPages = function () {
                        return Math.ceil($scope.filteredRegistrations.length / $scope.itemsPerPage);
                    };
                    $scope.itemsPerPageChanged = function () {
                        if ($scope.itemsPerPage == 'toAll') {
                            $scope.itemsPerPage = $scope.filteredRegistrations.length;
                            $scope.isAll = true;
                        } else {
                            $scope.isAll = false;
                        }
                    };
                    $scope.$watch('searchRegistration', function (term) {
                        $scope.allRegistrationTemp = $scope.resultData;
                        $scope.filteredRegistrations = filterFilter($scope.allRegistrationTemp, term);
                        $scope.totalItems = $scope.filteredRegistrations.length;
                    });

                    $scope.$watch('dateStart + dateEnd', function () {
                        
                        var newArray = [];
                        if (angular.isUndefined($scope.dateStart)) {
                            var start = null;
                            $scope.dateStart = start;
                        }
                        if (angular.isUndefined($scope.dateEnd) || $scope.dateEnd === null || $scope.dateEnd === '') {
                            $scope.superFarDate = new Date(2050, 0, 1, 00, 00, 00, 0);
                            angular.forEach($scope.resultData, function (obj) {
                                var startDate = new Date(obj['Date/Time stamp']);
                                if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
                                    newArray.push(obj);
                                }
                            });
                        } else {
                            var datecheck = $scope.dateEnd;
                            $scope.superFarDate = new Date(new Date().setDate(datecheck.getDate() + 1));
                            angular.forEach($scope.resultData, function (obj) {
                                var startDate = new Date(obj['Date/Time stamp']);
                                if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
                                    newArray.push(obj);
                                }
                            });
                        }
                        $scope.allFilteredRegistrationTime = newArray;
                        $scope.filteredRegistrations = filterFilter($scope.allFilteredRegistrationTime, $scope.searchRegistration);
                        $scope.totalItems = $scope.filteredRegistrations.length;
                    });
                } else {
                    $scope.tableHeader = 'No record is found';
                    $scope.filteredRegistrations = [];
                    $scope.itemsPerPage = 0;
                    $scope.isAll = true;
                }
                $scope.registrationChoice = '';
            }, function () {
                window.alert("Fail to send request!");
            });
        };


        $scope.form_id = '';
        //watch for change in form choice
        $scope.$watch('registrationChoice', function () {
            if ($scope.registrationChoice == 'retrieveCurrentRegistration') {
                if (session.getSession('registrationView') == null) {
                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: './style/ngTemplate/registrationSessionRetrieve.html',
                        scope: $scope,
                        controller: function () {
                            $scope.activate = function () {
                                modalInstance.dismiss('cancel');
                                datasend.form_id = $scope.form_id;
                                $scope.registrationRetrieve();
                            };
                            $scope.cancel = function () {
                                modalInstance.dismiss('cancel');
                            };
                        }
                    });
                } else {
                    datasend.form_id = session.getSession('registrationView');
                    $scope.registrationRetrieve();
                }
            } else if ($scope.registrationChoice == 'retrieveRegistration') {
                $scope.registrationRetrieve();
            }
        });

        $scope.deleteRegistry = function ($event, registry) {
            var url = AppAPI.deleteRegistration;
            datasend['registration_id'] = registry['Registration Id'];

            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function () {
                dataSubmit.submitData(datasend, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $state.reload();
                        });
                    } else {
                        $scope.errorMessages = response.data.message;
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteFailure.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        });
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };

        $scope.viewContact = function ($event, contact) {
            var toURL = $scope.userType + ".viewIndivContact";
            $scope.viewIndivContact = $scope.userType + '/viewIndivContact';
            var contactCid = contact['Contact ID'];
            session.setSession('contactToDisplayCid', contactCid);

            if ($event.which == 1) {
                $state.go(toURL);
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
    };
});