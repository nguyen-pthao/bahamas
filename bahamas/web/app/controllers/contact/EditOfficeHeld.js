/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditOfficeHeld', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService', 'loadOfficeList',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService, loadOfficeList) {

        $scope.loadOfficeHoldList = function () {
            loadOfficeList.retrieveOfficeList().then(function (response) {
                $scope.officeList = response.data.officeList;
            });
        };
        //office held
        $scope.addingOffice = false;
        $scope.addNewOffice = function () {
            $scope.addingOffice = !$scope.addingOffice;
        };
        $scope.resultOffice = {
            'office_held': '',
            status: false,
            message: ''
        };
        $scope.editTheOffice = function ($event, officeHeld) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            } else {
                datasend['contact_id'] = $scope.contactToEditCID;
            }
            datasend['user_type'] = session.getSession('userType');
            datasend['office_held_name'] = officeHeld['office_held'];
            if (officeHeld['start_office'] == null) {
                datasend['start_office'] = '';
            } else if (isNaN(officeHeld['start_office'])) {
                datasend['start_office'] = '';
            } else if (angular.isUndefined(officeHeld['start_office'])) {
                datasend['start_office'] = '';
            } else {
                datasend['start_office'] = officeHeld['start_office'].valueOf() + "";
            }
            if (officeHeld['end_office'] == null) {
                datasend['end_office'] = '';
            } else if (isNaN(officeHeld['end_office'])) {
                datasend['end_office'] = '';
            } else if (angular.isUndefined(officeHeld['end_office'])) {
                datasend['end_office'] = '';
            } else {
                datasend['end_office'] = officeHeld['end_office'].valueOf() + "";
            }
            datasend['remarks'] = officeHeld['remarks'];
            var url = AppAPI.updateOfficeHeld;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultOffice.status = true;
                if (response.data.message == 'success') {
                    $scope.resultOffice['office_held'] = datasend['office_held_name']; //to be modified
                    $scope.resultOffice.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editOfficeForm.$setValidity();
                    $timeout(function () {
                        $scope.resultOffice.status = false;
                    }, 1000);
                } else {
                    if (Array.isArray(response.data.message)) {
                        $scope.errorMessages = response.data.message;
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/errorMessage.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        });
                    } else {
                        $scope.errorMessages = [];
                        $scope.errorMessages.push(response.data.message);
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/errorMessage.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        });
                    }
                }
            }, function () {
                window.alert("Fail to send request!");
            });
        };
        $scope.deleteTheOffice = function ($event, officeHeld) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteOffice = {};
                deleteOffice['token'] = session.getSession('token');
                if ($scope.editMode == 'true') {
                    deleteOffice['contact_id'] = $scope.contactToEditCID;
                } else {
                    deleteOffice['contact_id'] = $scope.contactToEditCID;
                }
                deleteOffice['office_held_name'] = officeHeld['office_held'];
                deleteOffice['start_office'] = officeHeld['start_office'].valueOf() + "";
                var url = AppAPI.deleteOfficeHeld;
                deleteService.deleteDataService(deleteOffice, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete office held");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newOffice = {
            token: session.getSession("token"),
            'contact_id': -1,
            'user_type': session.getSession('userType'),
            'office_held_name': '',
            'start_office': '',
            'end_office': '',
            'remarks': ''
        };
        $scope.copyOffice = angular.copy($scope.newOffice);
        $scope.submitNewOffice = {
            'submittedOffice': false,
            'message': ''
        };
        $scope.addOffice = function () {
            var url = AppAPI.addOfficeHeld;
            if ($scope.editMode == 'true') {
                $scope.newOffice['contact_id'] = $scope.contactToEditCID;
            } else {
                $scope.newOffice['contact_id'] = $scope.contactToEditCID;
            }
            if ($scope.newOffice['start_office'] == null) {
                $scope.newOffice['start_office'] = '';
            } else if (isNaN($scope.newOffice['start_office'])) {
                $scope.newOffice['start_office'] = '';
            } else if (angular.isUndefined($scope.newOffice['start_office'])) {
                $scope.newOffice['start_office'] = '';
            } else {
                $scope.newOffice['start_office'] = $scope.newOffice['start_office'].valueOf() + "";
            }
            if ($scope.newOffice['end_office'] == null) {
                $scope.newOffice['end_office'] = '';
            } else if (isNaN($scope.newOffice['end_office'])) {
                $scope.newOffice['end_office'] = '';
            } else if (angular.isUndefined($scope.newOffice['end_office'])) {
                $scope.newOffice['end_office'] = '';
            } else {
                $scope.newOffice['end_office'] = $scope.newOffice['end_office'].valueOf() + "";
            }
            dataSubmit.submitData($scope.newOffice, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewOffice.submittedOffice = true;
                    $scope.submitNewOffice.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newOffice = angular.copy($scope.copyOffice);
                    $scope.form.editOfficeForm.$setValidity();
                    $timeout(function () {
                        $scope.submitNewOffice.submittedOffice = false;
                    }, 1000);
                    //can set $scope.addingPhone = false if wanting to hide
                    $scope.addingOffice = false;
                } else {
                    if (Array.isArray(response.data.message)) {
                        $scope.errorMessages = response.data.message;
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/errorMessage.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        });
                    } else {
                        $scope.errorMessages = [];
                        $scope.errorMessages.push(response.data.message);
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/errorMessage.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        });
                    }
                }
            }, function () {
                window.alert("Fail to send request!");
            });
        };

        $scope.openedOStart = [];
        $scope.openOStart = function (index) {
            $timeout(function () {
                $scope.openedOStart[index] = true;
            });
        };

        $scope.openedOEnd = [];
        $scope.openOEnd = function (index) {
            $timeout(function () {
                $scope.openedOEnd[index] = true;
            });
        };


        $scope.openNewOStart = function () {
            $timeout(function () {
                $scope.openedNewOStart = true;
            });
        };

        $scope.openNewOEnd = function () {
            $timeout(function () {
                $scope.openedNewOEnd = true;
            });
        };

    }]);
