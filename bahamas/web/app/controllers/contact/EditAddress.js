/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditAddress', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService) {

        //address
        $scope.addingAddress = false;
        $scope.addNewAddress = function () {
            $scope.addingAddress = !$scope.addingAddress;
        };
        $scope.resultAddress = {
            address: '',
            status: false,
            message: ''
        };
        $scope.editTheAddress = function ($event, address) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            } else {
                datasend['contact_id'] = $scope.contactToEditCID;
            }
            datasend['user_type'] = session.getSession('userType');
            datasend['country'] = address['country'];
            datasend['address'] = address['address'];
            datasend['zipcode'] = address['zipcode'];
            datasend['address_remarks'] = address['remarks'];
            if (address['date_obsolete'] == null) {
                datasend['date_obsolete'] = '';
            } else if (angular.isUndefined(address['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else if (isNaN(address['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else {
                datasend['date_obsolete'] = address['date_obsolete'].valueOf() + "";
            }
            var url = AppAPI.updateAddress;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultAddress.status = true;
                if (response.data.message == 'success') {
                    $scope.resultAddress.address = datasend['address'];
                    $scope.resultAddress.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editAddressForm.$setValidity();
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
                $timeout(function () {
                    $scope.resultAddress.status = false;
                }, 1000);
            }, function () {
                window.alert("Fail to send request!");
            });
        };
        $scope.deleteTheAddress = function ($event, address) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteAddress = {};
                deleteAddress['token'] = session.getSession('token');
                if ($scope.editMode == 'true') {
                    deleteAddress['contact_id'] = $scope.contactToEditCID;
                } else {
                    deleteAddress['contact_id'] = $scope.contactToEditCID;
                }
                deleteAddress['address'] = address['address'];
                var url = AppAPI.deleteAddress;
                deleteService.deleteDataService(deleteAddress, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function (response) {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete address");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newAddress = {
            token: session.getSession('token'),
            'contact_id': -1,
            address: '',
            country: 'Singapore',
            zipcode: '',
            'address_remarks': '',
            'date_obsolete': ''
        };
        $scope.copyAddress = angular.copy($scope.newAddress);
        $scope.submitNewAddress = {
            'submittedAddress': false,
            'message': ''
        };
        $scope.addAddress = function () {
            var url = AppAPI.addAddress;
            if ($scope.editMode == 'true') {
                $scope.newAddress['contact_id'] = $scope.contactToEditCID;
            } else {
                $scope.newAddress['contact_id'] = $scope.contactToEditCID;
            }
            dataSubmit.submitData($scope.newAddress, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewAddress.submittedAddress = true;
                    $scope.submitNewAddress.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newAddress = angular.copy($scope.copyAddress);
                    $scope.form.editAddressForm.$setValidity();
                    $scope.form.editAddressForm.$setPristine();
                    $timeout(function () {
                        $scope.submitNewAddress.submittedAddress = false;
                    }, 1000);
                    $scope.addingAddress = false;
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

        $scope.openedAddress = [];
        $scope.openAddress = function (index) {
            $timeout(function () {
                $scope.openedAddress[index] = true;
            });
        };
    }]);
