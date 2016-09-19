/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditAppreciation', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService) {
        
        var permission = session.getSession('userType');
        //appreciation
        $scope.addingAppreciation = false;
        $scope.addNewAppreciation = function () {
            $scope.addingAppreciation = !$scope.addingAppreciation;
        };
        $scope.resultAppreciation = {
            'appreciation_id': '',
            status: false,
            message: ''
        };
        $scope.editTheAppreciation = function ($event, appreciation) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            //if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            //}
//            } else {
//                datasend['contact_id'] = $scope.contactToEditCID;
//            }
            datasend['user_type'] = permission;
            datasend['appreciation_id'] = appreciation['appreciation_id'];
            datasend['appraisal_comment'] = appreciation['appraisal_comments'];
            datasend['appraisal_by'] = appreciation['appraisal_by'];
            if (appreciation['appraisal_date'] == null) {
                datasend['appraisal_date'] = '';
            } else if (isNaN(appreciation['appraisal_date'])) {
                datasend['appraisal_date'] = '';
            } else if (angular.isUndefined(appreciation['appraisal_date'])) {
                datasend['appraisal_date'] = '';
            } else {
                datasend['appraisal_date'] = appreciation['appraisal_date'].valueOf() + "";
            }
            datasend['appreciation_gesture'] = appreciation['appreciation_gesture'];
            datasend['appreciation_by'] = appreciation['appreciation_by'];
            if (appreciation['appreciation_date'] == null) {
                datasend['appreciation_date'] = '';
            } else if (isNaN(appreciation['appreciation_date'])) {
                datasend['appreciation_date'] = '';
            } else if (angular.isUndefined(appreciation['appreciation_date'])) {
                datasend['appreciation_date'] = '';
            } else {
                datasend['appreciation_date'] = appreciation['appreciation_date'].valueOf() + "";
            }
            datasend['remarks'] = appreciation['remarks'];
            var url = AppAPI.updateAppreciation;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultAppreciation.status = true;
                if (response.data.message == 'success') {
                    $scope.resultAppreciation['appreciation_id'] = datasend['appreciation_id'];
                    $scope.resultAppreciation.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editAppreciationForm.$setValidity();
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
                    $scope.resultAppreciation.status = false;
                }, 1000);
            }, function () {
                window.alert("Fail to send request!");
            });
        };
        $scope.deleteTheAppreciation = function ($event, appreciation) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteAppreciation = {};
                deleteAppreciation['token'] = session.getSession('token');
                deleteAppreciation['appreciation_id'] = appreciation['appreciation_id'];
                deleteAppreciation['user_type'] = permission;
                var url = AppAPI.deleteAppreciation;
                deleteService.deleteDataService(deleteAppreciation, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete appreciation");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newAppreciation = {
            'token': session.getSession('token'),
            'contact_id': -1,
            'user_type': permission,
            'appraisal_comment': '',
            'appraisal_by': '',
            'appraisal_date': '',
            'appreciation_gesture': '',
            'appreciation_by': 'TWC2',
            'appreciation_date': '',
            'remarks': ''
        };
        $scope.copyAppreciation = angular.copy($scope.newAppreciation);
        $scope.submitNewAppreciation = {
            'submittedAppreciation': false,
            'message': ''
        };
        $scope.addAppreciation = function () {
            var url = AppAPI.addAppreciation;
            //if ($scope.editMode == 'true') {
                $scope.newAppreciation['contact_id'] = $scope.contactToEditCID;
//            } else {
//                $scope.newAppreciation['contact_id'] = $scope.contactToEditCID;
//            }
            if ($scope.newAppreciation['appraisal_date'] == null) {
                $scope.newAppreciation['appraisal_date'] = '';
            } else if (isNaN($scope.newAppreciation['appraisal_date'])) {
                $scope.newAppreciation['appraisal_date'] = '';
            } else if (angular.isUndefined($scope.newAppreciation['appraisal_date'])) {
                $scope.newAppreciation['appraisal_date'] = '';
            } else {
                $scope.newAppreciation['appraisal_date'] = $scope.newAppreciation['appraisal_date'].valueOf() + "";
            }
            if ($scope.newAppreciation['appreciation_date'] == null) {
                $scope.newAppreciation['appreciation_date'] = '';
            } else if (isNaN($scope.newAppreciation['appreciation_date'])) {
                $scope.newAppreciation['appreciation_date'] = '';
            } else if (angular.isUndefined($scope.newAppreciation['appreciation_date'])) {
                $scope.newAppreciation['appreciation_date'] = '';
            } else {
                $scope.newAppreciation['appreciation_date'] = $scope.newAppreciation['appreciation_date'].valueOf() + "";
            }
            dataSubmit.submitData($scope.newAppreciation, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewAppreciation.submittedAppreciation = true;
                    $scope.submitNewAppreciation.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newAppreciation = angular.copy($scope.copyAppreciation);
                    $scope.form.editAppreciationForm.$setValidity();
                    $timeout(function () {
                        $scope.submitNewAppreciation.submittedAppreciation = false;
                    }, 1000);
                    $scope.addingAppreciation = false;
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

        $scope.openedAppraisal = [];
        $scope.openAppraisal = function (index) {
            $timeout(function () {
                $scope.openedAppraisal[index] = true;
            });
        };

        $scope.openedAppreciation = [];
        $scope.openAppreciation = function (index) {
            $timeout(function () {
                $scope.openedAppreciation[index] = true;
            });
        };

        $scope.openNewAppraisal = function () {
            $timeout(function () {
                $scope.openedNewAppraisal = true;
            });
        };

        $scope.openNewAppreciation = function () {
            $timeout(function () {
                $scope.openedNewAppreciation = true;
            });
        };


    }]);
