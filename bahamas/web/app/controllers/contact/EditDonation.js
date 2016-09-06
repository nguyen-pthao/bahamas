/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditDonation', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService) {
        //donation
        $scope.addingDonation = false;
        $scope.addNewDonation = function () {
            $scope.addingDonation = !$scope.addingDonation;
        };
        $scope.resultDonation = {
            'donation_id': '',
            status: false,
            message: ''
        };
        $scope.editTheDonation = function ($event, donation) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            //if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            //}
//            } else {
//                datasend['contact_id'] = $scope.contactToEditCID;
//            }
            datasend['user_type'] = session.getSession('userType');
            datasend['donation_id'] = donation['donation_id'];
            if (donation['date_received'] == null) {
                datasend['date_received'] = '';
            } else if (isNaN(donation['date_received'])) {
                datasend['date_received'] = '';
            } else if (angular.isUndefined(donation['date_received'])) {
                datasend['date_received'] = '';
            } else {
                datasend['date_received'] = donation['date_received'].valueOf() + "";
            }
            datasend['donation_amount'] = donation['donation_amount'];
            datasend['payment_mode'] = donation['payment_mode'];
            datasend['explain_if_other_payment'] = donation['explain_if_other_payment'];
            datasend['ext_transaction_ref'] = donation['ext_transaction_ref'];
            datasend['receipt_number'] = donation['receipt_number'];
            if (donation['receipt_date'] == null) {
                datasend['receipt_date'] = '';
            } else if (isNaN(donation['receipt_date'])) {
                datasend['receipt_date'] = '';
            } else if (angular.isUndefined(donation['receipt_date'])) {
                datasend['receipt_date'] = '';
            } else {
                datasend['receipt_date'] = donation['receipt_date'].valueOf() + "";
            }
            datasend['receipt_mode'] = donation['receipt_mode_name'];
            datasend['explain_if_other_receipt'] = donation['explain_if_other_receipt'];
            datasend['donor_instruction'] = donation['donor_instructions'];
            if (donation['subtotal1'] == '') {
                datasend['subamount1'] = "0";
            } else {
                datasend['subamount1'] = donation['subtotal1'];
            }
            if (donation['subtotal2'] == '') {
                datasend['subamount2'] = "0";
            } else {
                datasend['subamount2'] = donation['subtotal2'];
            }
            if (donation['subtotal3'] == '') {
                datasend['subamount3'] = "0";
            } else {
                datasend['subamount3'] = donation['subtotal3'];
            }
            datasend['allocation1'] = donation['allocation1'];
            datasend['allocation2'] = donation['allocation2'];
            datasend['allocation3'] = donation['allocation3'];
            datasend['associated_occasion'] = donation['associated_occasion'];
            datasend['remarks'] = donation['remarks'];
            var url = AppAPI.updateDonation;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultDonation.status = true;
                if (response.data.message == 'success') {
                    $scope.resultDonation['donation_id'] = datasend['donation_id'];
                    $scope.resultDonation.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editDonationForm.$setValidity();
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
                    $scope.resultDonation.status = false;
                }, 1000);
            }, function () {
                window.alert("Fail to send request!");
            });
        };
        $scope.deleteTheDonation = function ($event, donation) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteDonation = {};
                deleteDonation['token'] = session.getSession('token');
                deleteDonation['donation_id'] = donation['donation_id'];
                var url = AppAPI.deleteDonation;
                deleteService.deleteDataService(deleteDonation, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete donation");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newDonation = {
            token: session.getSession("token"),
            'contact_id': -1,
            'user_type': session.getSession('userType'),
            'date_received': '',
            'donation_amount': '',
            'payment_mode': '',
            'explain_if_other_payment': '',
            'ext_transaction_ref': '',
            'receipt_number': '',
            'receipt_date': '',
            'receipt_mode': '',
            'explain_if_other_receipt': '',
            'donor_instruction': '',
            'allocation1': '',
            'subamount1': '0',
            'allocation2': '',
            'subamount2': '0',
            'allocation3': '',
            'subamount3': '0',
            'associated_occasion': '',
            'remarks': ''
        };
        $scope.copyDonation = angular.copy($scope.newDonation);
        $scope.submitNewDonation = {
            'submittedDonation': false,
            'message': ''
        };
        $scope.addDonation = function () {
            var url = AppAPI.addDonation;
            //if ($scope.editMode == 'true') {
                $scope.newDonation['contact_id'] = $scope.contactToEditCID;
//            } else {
//                $scope.newDonation['contact_id'] = $scope.contactToEditCID;
//            }
            if ($scope.newDonation['date_received'] == null) {
                $scope.newDonation['date_received'] = '';
            } else if (isNaN($scope.newDonation['date_received'])) {
                $scope.newDonation['date_received'] = '';
            } else if (angular.isUndefined($scope.newDonation['date_received'])) {
                $scope.newDonation['date_received'] = '';
            } else {
                $scope.newDonation['date_received'] = $scope.newDonation['date_received'].valueOf() + "";
            }
            if ($scope.newDonation['receipt_date'] == null) {
                $scope.newDonation['receipt_date'] = '';
            } else if (isNaN($scope.newDonation['receipt_date'])) {
                $scope.newDonation['receipt_date'] = '';
            } else if (angular.isUndefined($scope.newDonation['receipt_date'])) {
                $scope.newDonation['receipt_date'] = '';
            } else {
                $scope.newDonation['receipt_date'] = $scope.newDonation['receipt_date'].valueOf() + "";
            }
            if (angular.isUndefined($scope.newDonation['subamount1'])) {
                $scope.newDonation['subamount1'] = '0';
            } else if ($scope.newDonation['subamount1'] == '') {
                $scope.newDonation['subamount1'] = '0';
            }
            if (angular.isUndefined($scope.newDonation['subamount2'])) {
                $scope.newDonation['subamount2'] = '0';
            } else if ($scope.newDonation['subamount2'] == '') {
                $scope.newDonation['subamount2'] = '0';
            }
            if (angular.isUndefined($scope.newDonation['subamount3'])) {
                $scope.newDonation['subamount3'] = '0';
            } else if ($scope.newDonation['subamount3'] == '') {
                $scope.newDonation['subamount3'] = '0';
            }

            dataSubmit.submitData($scope.newDonation, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewDonation.submittedDonation = true;
                    $scope.submitNewDonation.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newDonation = angular.copy($scope.copyDonation);
                    $scope.form.editDonationForm.$setValidity();
                    $scope.form.editDonationForm.$setUntouched();
                    $timeout(function () {
                        $scope.submitNewDonation.submittedDonation = false;
                    }, 1000);
                    $scope.addingDonation = false;
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

        $scope.openedDonationReceived = [];
        $scope.openDonationReceived = function (index) {
            $timeout(function () {
                $scope.openedDonationReceived[index] = true;
            });
        };

        $scope.openedDonationReceipt = [];
        $scope.openDonationReceipt = function (index) {
            $timeout(function () {
                $scope.openedDonationReceipt[index] = true;
            });
        };


        $scope.openNewDReceived = function () {
            $timeout(function () {
                $scope.openedNewDReceived = true;
            });
        };

        $scope.openNewDReceipt = function () {
            $timeout(function () {
                $scope.openedNewDReceipt = true;
            });
        };

    }]);
