/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditSkills', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService) {

        //skills and assets
        $scope.addingSkills = false;
        $scope.addNewSkills = function () {
            $scope.addingSkills = !$scope.addingSkills;
        };
        $scope.resultSkill = {
            'skill_name': '',
            status: false,
            message: ''
        };
        $scope.editTheSkill = function ($event, skill) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            } else {
                datasend['contact_id'] = $scope.contactToEditCID;
            }
            datasend['user_type'] = session.getSession('userType');
            datasend['skill_asset'] = skill['skill_name'];
            datasend['explain_if_other'] = skill['explain_if_other'];
            datasend['remarks'] = skill['remarks'];
            if (skill['date_obsolete'] == null) {
                datasend['date_obsolete'] = '';
            } else if (isNaN(skill['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else if (angular.isUndefined(skill['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else {
                datasend['date_obsolete'] = skill['date_obsolete'].valueOf() + "";
            }
            var url = AppAPI.updateSkill;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultSkill.status = true;
                if (response.data.message == 'success') {
                    $scope.resultSkill['skill_name'] = datasend['skill_asset'];
                    $scope.resultSkill.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editSkillForm.$setValidity();
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
        $scope.deleteTheSkill = function ($event, skill) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteSkill = {};
                deleteSkill['token'] = session.getSession('token');
                if ($scope.editMode == 'true') {
                    deleteSkill['contact_id'] = $scope.contactToEditCID;
                } else {
                    deleteSkill['contact_id'] = $scope.contactToEditCID;
                }
                deleteSkill['skill_asset'] = skill['skill_name'];
                var url = AppAPI.deleteSkill;
                deleteService.deleteDataService(deleteSkill, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function (response) {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete skill");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newSkills = {
            'token': session.getSession('token'),
            'contact_id': -1,
            'user_type': session.getSession('userType'),
            'skill_asset': '',
            'explain_if_other': '',
            'remarks': '',
            'date_obsolete': ''
        };
        $scope.copySkills = angular.copy($scope.newSkills);
        $scope.submitNewSkills = {
            'submittedSkills': false,
            'message': ''
        };
        $scope.addSkills = function () {
            var url = AppAPI.addSkill;
            if ($scope.editMode == 'true') {
                $scope.newSkills['contact_id'] = $scope.contactToEditCID;
            } else {
                $scope.newSkills['contact_id'] = $scope.contactToEditCID;
            }
            if ($scope.newSkills['date_obsolete'] == null) {
                $scope.newSkills['date_obsolete'] = '';
            } else if (isNaN($scope.newSkills['date_obsolete'])) {
                $scope.newSkills['date_obsolete'] = '';
            } else if (angular.isUndefined($scope.newSkills['date_obsolete'])) {
                $scope.newSkills['date_obsolete'] = '';
            } else {
                $scope.newSkills['date_obsolete'] = $scope.newSkills['date_obsolete'].valueOf() + "";
            }
            dataSubmit.submitData($scope.newSkills, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewSkills.submittedSkills = true;
                    $scope.submitNewSkills.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newSkills = angular.copy($scope.copySkills);
                    $scope.form.editSkillForm.$setValidity();
                    $timeout(function () {
                        $scope.submitNewSkills.submittedSkills = false;
                    }, 1000);
                    $scope.addingSkills = false;
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

        $scope.openedSkills = [];
        $scope.openSkills = function (index) {
            $timeout(function () {
                $scope.openedSkills[index] = true;
            });
        };

    }]);

