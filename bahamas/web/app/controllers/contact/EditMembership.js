/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditMembership', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService', 'loadMembershipClass', 
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService, loadMembershipClass) {
        
        $scope.loadMembershipList = function () {
            loadMembershipClass.retrieveMembershipClass().then(function (response) {
                $scope.membershipList = response.data.membershipClassList;
            });
        };
        //membership
        $scope.addingMembership = false;
        $scope.addNewMembership = function () {
            $scope.addingMembership = !$scope.addingMembership;
        };
        $scope.resultMembership = {
            'membership_id': '',
            status: false,
            message: ''
        };
        $scope.editTheMembership = function ($event, membership) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            } else {
                datasend['contact_id'] = $scope.contactToEditCID;
            }
            datasend['user_type'] = session.getSession('userType');
            datasend['membership_id'] = membership['membership_id'];
            if (membership['start_date'] == null) {
                datasend['start_membership'] = '';
            } else if (angular.isUndefined(membership['start_date'])) {
                datasend['start_membership'] = '';
            } else if (isNaN(membership['start_date'])) {
                datasend['start_membership'] = '';
            } else {
                datasend['start_membership'] = membership['start_date'].valueOf() + "";
            }
            if (membership['end_date'] == null) {
                datasend['end_membership'] = '';
            } else if (angular.isUndefined(membership['end_date'])) {
                datasend['end_membership'] = '';
            } else if (isNaN(membership['end_date'])) {
                datasend['end_membership'] = '';
            } else {
                datasend['end_membership'] = membership['end_date'].valueOf() + "";
            }
            if (membership['receipt_date'] == null) {
                datasend['receipt_date'] = '';
            } else if (angular.isUndefined(membership['receipt_date'])) {
                datasend['receipt_date'] = '';
            } else if (isNaN(membership['receipt_date'])) {
                datasend['receipt_date'] = '';
            } else {
                datasend['receipt_date'] = membership['receipt_date'].valueOf() + "";
            }
            datasend['subscription_amount'] = membership['subscription_amount'];
            datasend['ext_transaction_ref'] = membership['ext_transaction_ref'];
            datasend['receipt_number'] = membership['receipt_number'];
            datasend['remarks'] = membership['remarks'];
            datasend['receipt_mode'] = membership['receipt_mode_name'];
            datasend['explain_if_other_receipt'] = membership['explain_if_other_receipt'];
            datasend['membership_class'] = membership['membership_class_name'];
            datasend['explain_if_other_class'] = membership['explain_if_other_class'];
            datasend['payment_mode'] = membership['payment_mode_name'];
            datasend['explain_if_other_payment'] = membership['explain_if_other_payment'];
            var url = AppAPI.updateMembership;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultMembership.status = true;
                if (response.data.message == 'success') {
                    $scope.resultMembership['membership_id'] = datasend['membership_id'];
                    $scope.resultMembership.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editMembershipForm.$setValidity();
                    $timeout(function () {
                        $scope.resultMembership.status = false;
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
        $scope.deleteTheMembership = function ($event, membership) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteMembership = {};
                deleteMembership['token'] = session.getSession('token');
                deleteMembership['membership_id'] = membership['membership_id'];
                var url = AppAPI.deleteMembership;
                deleteService.deleteDataService(deleteMembership, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete membership");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newMembership = {
            token: session.getSession("token"),
            'contact_id': -1,
            'user_type': session.getSession('userType'),
            'start_membership': '',
            'end_membership': '',
            'receipt_date': '',
            'subscription_amount': '',
            'ext_transaction_ref': '',
            'receipt_number': '',
            'remarks': '',
            'receipt_mode': '',
            'explain_if_other_receipt': '',
            'membership_class': 'Ordinary',
            'explain_if_other_class': '',
            'payment_mode': '',
            'explain_if_other_payment': ''
        };
        $scope.copyMembership = angular.copy($scope.newMembership);
        $scope.submitNewMembership = {
            'submittedMembership': false,
            'message': ''
        };
        $scope.$watch("newMembership['start_membership']", function () {
            $scope.newEnd = '';
            if ($scope.newMembership['start_membership'] != '' && $scope.newMembership['start_membership'] != null) {
                $scope.newEnd = angular.copy($scope.newMembership['start_membership']);
                if (angular.isDate($scope.newEnd)) {
                    var newYear = $scope.newEnd.getFullYear() + 1;
                    $scope.newMembership['end_membership'] = $scope.newEnd.setFullYear(newYear);
                    $scope.newMembership['end_membership'] = $scope.newEnd.setDate($scope.newEnd.getDate() - 1)
                }
            }
            ;
        });
        $scope.addMembership = function () {
            var url = AppAPI.addMembership;
            if ($scope.editMode == 'true') {
                $scope.newMembership['contact_id'] = $scope.contactToEditCID;
            } else {
                $scope.newMembership['contact_id'] = $scope.contactToEditCID;
            }
            if ($scope.newMembership['start_membership'] == null) {
                $scope.newMembership['start_membership'] = '';
            } else if (isNaN($scope.newMembership['start_membership'])) {
                $scope.newMembership['start_membership'] = '';
            } else if (angular.isUndefined($scope.newMembership['start_membership'])) {
                $scope.newMembership['start_membership'] = '';
            } else {
                $scope.newMembership['start_membership'] = $scope.newMembership['start_membership'].valueOf() + "";
            }
            if ($scope.newMembership['end_membership'] == null) {
                $scope.newMembership['end_membership'] = '';
            } else if (isNaN($scope.newMembership['end_membership'])) {
                $scope.newMembership['end_membership'] = '';
            } else if (angular.isUndefined($scope.newMembership['end_membership'])) {
                $scope.newMembership['end_membership'] = '';
            } else {
                $scope.newMembership['end_membership'] = $scope.newMembership['end_membership'].valueOf() + "";
            }
            if ($scope.newMembership['receipt_date'] == null) {
                $scope.newMembership['receipt_date'] = '';
            } else if (isNaN($scope.newMembership['receipt_date'])) {
                $scope.newMembership['receipt_date'] = '';
            } else if (angular.isUndefined($scope.newMembership['receipt_date'])) {
                $scope.newMembership['receipt_date'] = '';
            } else {
                $scope.newMembership['receipt_date'] = $scope.newMembership['receipt_date'].valueOf() + "";
            }
            dataSubmit.submitData($scope.newMembership, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewMembership.submittedMembership = true;
                    $scope.submitNewMembership.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newMembership = angular.copy($scope.copyMembership);
                    $scope.form.editMembershipForm.$setValidity();
                    $scope.form.editMembershipForm.$setUntouched();
                    $timeout(function () {
                        $scope.submitNewMembership.submittedMembership = false;
                    }, 1000);
                    //can set $scope.addingPhone = false if wanting to hide
                    $scope.addingMembership = false;
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

        $scope.openedMStart = [];
        $scope.openMStart = function (index) {
            $timeout(function () {
                $scope.openedMStart[index] = true;
            });
        };

        $scope.openedMEnd = [];
        $scope.openMEnd = function (index) {
            $timeout(function () {
                $scope.openedMEnd[index] = true;
            });
        };

        $scope.openedMembership = [];
        $scope.openMembership = function (index) {
            $timeout(function () {
                $scope.openedMembership[index] = true;
            });
        };


        $scope.openNewMStart = function () {
            $timeout(function () {
                $scope.openedNewMStart = true;
            });
        };

        $scope.openNewMEnd = function () {
            $timeout(function () {
                $scope.openedNewMEnd = true;
            });
        };

        $scope.openNewMReceipt = function () {
            $timeout(function () {
                $scope.openedNewMReceipt = true;
            });
        };

    }]);
