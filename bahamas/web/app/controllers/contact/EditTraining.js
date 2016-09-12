/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditTraining', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService', 'loadPermissionLevel', 
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService, loadPermissionLevel) {
        $scope.addingTraining = false;
        $scope.addNewTraining = function () {
            $scope.addingTraining = !$scope.addingTraining;
        };
        $scope.resultTraining = {
            'training_id': '',
            status: false,
            message: ''
        };
        $scope.editTheTraining = function ($event, training) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            //if ($scope.editMode == 'true') {
            datasend['contact_id'] = $scope.contactToEditCID;
            //}
//            } else {
//                datasend['contact_id'] = $scope.contactToEditCID;
//            }
            datasend['training_id'] = training['training_id'];  
            datasend['team_name'] = training['team_name'];
            datasend['explain_if_other'] = training['explain_if_others'];
            datasend['training_course'] = training['training_course'];
            datasend['training_by'] = training['training_by'];
            datasend['remarks'] = training['remarks'];
            if (training['training_date'] == null) {
                datasend['training_date'] = '';
            } else if (angular.isUndefined(training['training_date'])) {
                datasend['training_date'] = '';
            } else if (isNaN(training['training_date'])) {
                datasend['training_date'] = '';
            } else {
                datasend['training_date'] = training['training_date'].valueOf() + "";
            }
            var url = AppAPI.updateTraining;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultTraining.status = true;
                if (response.data.message == 'success') {
                    $scope.resultTraining.training_id = datasend['training_id'];
                    $scope.resultTraining.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    //$scope.form.editTrainingForm.$setValidity();
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
                    $scope.resultTraining.status = false;
                }, 1000);
            }, function () {
                window.alert("Fail to send request!");
            });
        };
        $scope.deleteTheTraining = function ($event, training) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteTraining = {};
                deleteTraining['token'] = session.getSession('token');
                deleteTraining['training_id'] = training['training_id'];
                var url = AppAPI.deleteTraining;
                deleteService.deleteDataService(deleteTraining, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete training");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newTraining = {
            token: session.getSession("token"),
            'contact_id': -1,
            'team_name': '',
            'explain_if_other': '',
            'training_course': '',
            'training_by': '',
            'training_date': '',
            'remarks': ''
        };
        $scope.copyTraining = angular.copy($scope.newTraining);
        $scope.submitNewTraining = {
            'submittedTraining': false,
            'message': ''
        };
        $scope.addTraining = function () {
            var url = AppAPI.addTraining;
            //if ($scope.editMode == 'true') {
                $scope.newTraining['contact_id'] = $scope.contactToEditCID;
            //} else {
            //    $scope.newAddress['contact_id'] = $scope.contactToEditCID;
            //}
            if ($scope.newTraining['training_date'] == null) {
                $scope.newTraining['training_date'] = '';
            } else if (isNaN($scope.newTraining['training_date'])) {
                $scope.newTraining['training_date'] = '';
            } else if (angular.isUndefined($scope.newTraining['training_date'])) {
                $scope.newTraining['training_date'] = '';
            } else {
                $scope.newTraining['training_date'] = $scope.newTraining['training_date'].valueOf() + "";
            }
            dataSubmit.submitData($scope.newTraining, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewTraining.submittedTraining = true;
                    $scope.submitNewTraining.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newTraining = angular.copy($scope.copyTraining);
                    $scope.form.editTrainingForm.$setValidity();
                    $scope.form.editTrainingForm.$setPristine();
                    $timeout(function () {
                        $scope.submitNewTraining.submittedTraining = false;
                    }, 1000);
                    $scope.addingTraining = false;
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
        
        $scope.openedTraining = [];
        $scope.openTraining = function (index) {
            $timeout(function () {
                $scope.openedTraining[index] = true;
            });
        };
        
        $scope.openNewTraining = function () {
            $timeout(function () {
                $scope.openedNewTraining = true;
            });
        };

    }]);
