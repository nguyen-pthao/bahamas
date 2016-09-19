/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditPhone', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService) {

        var permission = session.getSession('userType');
        //phone
        $scope.addingPhone = false;
        $scope.addNewPhone = function () {
            //$scope.addingPhone = true;
            $scope.addingPhone = !$scope.addingPhone;
        };
        $scope.resultPhone = {
            'phone_number': '',
            status: false,
            message: ''
        };
        $scope.editThePhone = function ($event, phone) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            //if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            //}
//            } else {
//                datasend['contact_id'] = $scope.contactToEditCID;
//            }
            datasend['user_type'] = permission;
            datasend['country_code'] = phone['country_code'];
            datasend['phone_number'] = phone['phone_number'];
            datasend['phone_remarks'] = phone['remarks'];
            if (phone['date_obsolete'] == null) {
                datasend['date_obsolete'] = '';
            } else if (angular.isUndefined(phone['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else if (isNaN(phone['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else {
                datasend['date_obsolete'] = phone['date_obsolete'].valueOf() + "";
            }
            var url = AppAPI.updatePhone;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultPhone.status = true;
                if (response.data.message == 'success') {
                    $scope.resultPhone['phone_number'] = datasend['phone_number'];
                    $scope.resultPhone.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editPhoneForm.$setValidity();
                    $timeout(function () {
                        $scope.resultPhone.status = false;
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
        $scope.deleteThePhone = function ($event, phone) {
            //to add ngDialog for confirmation
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deletePhone = {};
                deletePhone['token'] = session.getSession('token');
                deletePhone['user_type'] = permission;
                //if ($scope.editMode == 'true') {
                    deletePhone['contact_id'] = $scope.contactToEditCID;
//                } else {
//                    deletePhone['contact_id'] = $scope.contactToEditCID;
//                }
                deletePhone['phone_number'] = phone['phone_number'];
                var url = AppAPI.deletePhone;
                deleteService.deleteDataService(deletePhone, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete phone");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newPhone = {
            token: session.getSession("token"),
            'contact_id': -1,
            'user_type': permission,
            'country_code': 65,
            'phone_number': '',
            'phone_remarks': '',
            'date_obsolete': ''
        };
        $scope.copyPhone = angular.copy($scope.newPhone);
        $scope.submitNewPhone = {
            'submittedPhone': false,
            'message': ''
        };
        $scope.addPhone = function () {
            var url = AppAPI.addPhone;
            //if ($scope.editMode == 'true') {
                $scope.newPhone['contact_id'] = $scope.contactToEditCID;
//            } else {
//                $scope.newPhone['contact_id'] = $scope.contactToEditCID;
//            }
            dataSubmit.submitData($scope.newPhone, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewPhone.submittedPhone = true;
                    $scope.submitNewPhone.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newPhone = angular.copy($scope.copyPhone);
                    $scope.form.editPhoneForm.$setValidity();
                    $scope.form.editPhoneForm.$setPristine();
                    $timeout(function () {
                        $scope.submitNewPhone.submittedPhone = false;
                    }, 1000);
                    //can set $scope.addingPhone = false if wanting to hide
                    $scope.addingPhone = false;
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

        $scope.openedPhone = [];
        $scope.openPhone = function (index) {
            $timeout(function () {
                $scope.openedPhone[index] = true;
            });
        };

    }]);
