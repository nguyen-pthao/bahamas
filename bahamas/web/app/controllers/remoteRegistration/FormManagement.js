/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('formManagement', ['$scope', 'session', '$state', 'dataSubmit', '$uibModal', '$timeout', 'ngDialog', 'orderByFilter', 'filterFilter',
    function ($scope, session, $state, dataSubmit, $uibModal, $timeout, ngDialog, orderBy, filterFilter) {

        var token = session.getSession('token');
        $scope.permission = session.getSession('userType');

        //datepicker
        $scope.today = function () {
            $scope.dt = new Date();
        };

        $scope.clear = function () {
            $scope.dt = null;
        };
        //activate new form
        $scope.dateOptions = {
            formatYear: 'yy',
            formatMonth: 'MMM',
            formatDay: 'dd',
            startingDay: 1
        };
        
        $scope.format = 'dd MMM yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];

        $scope.openedStart = [];
        $scope.openStart = function (index) {
            $timeout(function () {
                $scope.openedStart[index] = true;
            });
        };

        $scope.openedEnd = [];
        $scope.openEnd = function (index) {
            $timeout(function () {
                $scope.openedEnd[index] = true;
            });
        };
        
        $scope.dateStart = '';
        $scope.dateEnd = '';
        
        $scope.code = '';
        $scope.startdate = new Date(new Date().setSeconds(00));
        $scope.enddate = new Date(new Date(new Date().setSeconds(00)).setHours($scope.startdate.getHours() + 2));

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
        $scope.formChoice = 'retrieveAllForm';
        $scope.formRetrieve = function () {
            var url = AppAPI[$scope.formChoice];
            $scope.myPromise = dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultData = response.data.Records;
                if ($scope.resultData != '') {
                    $scope.tableHeader = Object.keys($scope.resultData[0]);
                    
                    $scope.filteredForms = $scope.resultData;
                    $scope.totalItems = $scope.filteredForms.length;
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
                        return Math.ceil($scope.filteredForms.length / $scope.itemsPerPage);
                    };
                    $scope.itemsPerPageChanged = function () {
                        if ($scope.itemsPerPage == 'toAll') {
                            $scope.itemsPerPage = $scope.filteredForms.length;
                            $scope.isAll = true;
                        } else {
                            $scope.isAll = false;
                        }
                    };
                    
                    $scope.$watch('searchForm', function (term) {
                        $scope.allFormsTemp = $scope.resultData;
                        $scope.filteredForms = filterFilter($scope.allFormsTemp, term);
                        $scope.totalItems = $scope.filteredForms.length;
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
                                var startDate = new Date(obj['Window start (date/time)']);
                                if (startDate >= $scope.dateStart && startDate <= $scope.superFarDate) {
                                    newArray.push(obj);
                                }
                            });
                        } else {
                            var datecheck = $scope.dateEnd;
                            $scope.superFarDate = new Date(new Date().setDate(datecheck.getDate() + 1));
                            angular.forEach($scope.resultData, function (obj) {
                                var startDate = new Date(obj['Window start (date/time)']);
                                var endDate = new Date(obj['Window end (date/time)']);
                                if (startDate >= $scope.dateStart && endDate <= $scope.superFarDate) {
                                    newArray.push(obj);
                                }
                            });
                        }
                        $scope.allFilteredFormTime = newArray;
                        $scope.filteredForms = filterFilter($scope.allFilteredFormTime, $scope.searchForm);
                        $scope.totalItems = $scope.filteredForms.length;
                    });
                }

            }, function () {
                window.alert("Fail to send request!");
            });
        };

        //watch for change in form choice
        $scope.$watch('formChoice', function () {
            if ($scope.formChoice == 'retrieveAllForm' || $scope.formChoice == 'retrieveForm') {
                $scope.formRetrieve();
            }
        });
        //add new form
        $scope.newForm = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: './style/ngTemplate/registrationForm.html',
                scope: $scope,
                controller: function () {
                    $scope.activate = function () {
                        modalInstance.dismiss('cancel');
                        var url = AppAPI.addForm;
                        function activateNewForm() {
                            
                            if ($scope.startdate == null) {
                                datasend['start_date'] = '';
                            } else if (angular.isUndefined($scope.startdate)) {
                                datasend['start_date'] = '';
                            } else if (isNaN($scope.startdate)) {
                                datasend['start_date'] = '';
                            } else {
                                datasend['start_date'] = $scope.startdate.valueOf() + "";
                            }

                            if ($scope.enddate == null) {
                                datasend['end_date'] = '';
                            } else if (angular.isUndefined($scope.enddate)) {
                                datasend['end_date'] = '';
                            } else if (isNaN($scope.enddate)) {
                                datasend['end_date'] = '';
                            } else {
                                datasend['end_date'] = $scope.enddate.valueOf() + "";
                            }

                            datasend['code'] = $scope.code;
                            dataSubmit.submitData(datasend, url).then(function (response) {
                                if (response.data.message == 'success') {
                                    ngDialog.openConfirm({
                                        template: './style/ngTemplate/addSuccess.html',
                                        className: 'ngdialog-theme-default',
                                        scope: $scope
                                    }).then(function () {
                                        $state.reload();
                                    });
                                } else {
                                    $scope.errorMessages = response.data.message;
                                    ngDialog.openConfirm({
                                        template: './style/ngTemplate/errorMessage.html',
                                        className: 'ngdialog-theme-default',
                                        scope: $scope
                                    });
                                }

                            }, function () {
                                window.alert("Fail to send request!");
                            });
                        }
                        activateNewForm();
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

        //generate authentication code
        $scope.generateCode = function () {
            $scope.code = Math.random().toString(36).substring(2, 8);
        };

        //edit form
        $scope.editForm = function ($event, form) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: './style/ngTemplate/editForm.html',
                scope: $scope,
                controller: function () {
                    $scope.formId = form['Form Id'];
                    $scope.formCode = form['Authentication code created'];
                    $scope.formStart = new Date(form['Window start (date/time)']);
                    $scope.formEnd = new Date(form['Window end (date/time)']);
                    
                    $scope.update = function () {
                        var url = AppAPI.updateForm;
//                        datasend['token'] = token;
//                        datasend['user_type'] = $scope.permission;
                        datasend['form_id'] = $scope.formId;
                        datasend['code'] = $scope.formCode;

                        if ($scope.formStart == null) {
                            datasend['start_date'] = '';
                        } else if (angular.isUndefined($scope.formStart)) {
                            datasend['start_date'] = '';
                        } else if (isNaN($scope.formStart)) {
                            datasend['start_date'] = '';
                        } else {
                            datasend['start_date'] = $scope.formStart.valueOf() + "";
                        }

                        if ($scope.formEnd == null) {
                            datasend['end_date'] = '';
                        } else if (angular.isUndefined($scope.formEnd)) {
                            datasend['end_date'] = '';
                        } else if (isNaN($scope.formEnd)) {
                            datasend['end_date'] = '';
                        } else {
                            datasend['end_date'] = $scope.formEnd.valueOf() + "";
                        }

                        dataSubmit.submitData(datasend, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/addSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $state.reload();
                                });
                            } else {
                                $scope.errorMessages = response.data.message;
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/errorMessage.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                });
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
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

        //delete form
        $scope.deleteForm = function($event, form) {
            var url = AppAPI.deleteForm;
            datasend['form_id'] = form['Form Id'];
            
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
                }, function() {
                    window.alert("Fail to send request!");
                });
            });
        };
        
        $scope.viewRegistration = function($event, result) {
            session.setSession('registrationView', result['Form Id']);
            var toURL = $scope.userType + '.remoteRegistration';
            $scope.viewSelectedRegistration = $scope.userType + '/remoteRegistration';

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