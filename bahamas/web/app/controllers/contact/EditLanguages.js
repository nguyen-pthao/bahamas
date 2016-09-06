/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditLanguages', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService', 'loadLanguage',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService, loadLanguage) {

        $scope.loadLanguageList = function () {
            loadLanguage.retrieveLanguage().then(function (response) {
                $scope.languageList = response.data.languageList;
            });
        };
        //languages
        $scope.addingLanguages = false;
        $scope.addNewLanguages = function () {
            $scope.addingLanguages = !$scope.addingLanguages;
        };
        $scope.resultLanguage = {
            'language_name': '',
            status: false,
            message: ''
        };
        $scope.editTheLanguage = function ($event, language) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            //if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            //}
//            } else {
//                datasend['contact_id'] = $scope.contactToEditCID;
//            }
            datasend['user_type'] = session.getSession('userType');
            datasend['language'] = language['language_name'];
            datasend['speak_write'] = language['proficiency'];
            datasend['explain_if_other'] = language['explain_if_other'];
            datasend['remarks'] = language['remarks'];
            if (language['date_obsolete'] == null) {
                datasend['date_obsolete'] = '';
            } else if (isNaN(language['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else if (angular.isUndefined(language['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else {
                datasend['date_obsolete'] = language['date_obsolete'].valueOf() + "";
            }
            var url = AppAPI.updateLanguage;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultLanguage.status = true;
                if (response.data.message == 'success') {
                    $scope.resultLanguage['language_name'] = datasend['language'];
                    $scope.resultLanguage.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editLanguageForm.$setValidity();
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
        $scope.deleteTheLanguage = function ($event, language) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteLanguage = {};
                deleteLanguage['token'] = session.getSession('token');
                //if ($scope.editMode == 'true') {
                    deleteLanguage['contact_id'] = $scope.contactToEditCID;
//                } else {
//                    deleteLanguage['contact_id'] = $scope.contactToEditCID;
//                }
                deleteLanguage['language'] = language['language_name'];
                var url = AppAPI.deleteLanguage;
                deleteService.deleteDataService(deleteLanguage, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete language");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newLanguages = {
            'token': session.getSession('token'),
            'contact_id': -1,
            'user_type': session.getSession('userType'),
            'language': '',
            'speak_write': '',
            'explain_if_other': '',
            'remarks': '',
            'date_obsolete': ''
        };
        $scope.copyLanguages = angular.copy($scope.newLanguages);
        $scope.submitNewLanguages = {
            'submittedLanguages': false,
            'message': ''
        };
        $scope.addLanguages = function () {
            var url = AppAPI.addLanguage;
            //if ($scope.editMode == 'true') {
                $scope.newLanguages['contact_id'] = $scope.contactToEditCID;
//            } else {
//                $scope.newLanguages['contact_id'] = $scope.contactToEditCID;
//            }
            if ($scope.newLanguages['date_obsolete'] == null) {
                $scope.newLanguages['date_obsolete'] = '';
            } else if (isNaN($scope.newLanguages['date_obsolete'])) {
                $scope.newLanguages['date_obsolete'] = '';
            } else if (angular.isUndefined($scope.newLanguages['date_obsolete'])) {
                $scope.newLanguages['date_obsolete'] = '';
            } else {
                $scope.newLanguages['date_obsolete'] = $scope.newLanguages['date_obsolete'].valueOf() + "";
            }
            dataSubmit.submitData($scope.newLanguages, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewLanguages.submittedLanguages = true;
                    $scope.submitNewLanguages.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newLanguages = angular.copy($scope.copyLanguages);
                    $scope.form.editLanguageForm.$setValidity();
                    $timeout(function () {
                        $scope.submitNewLanguages.submittedLanguages = false;
                    }, 1000);
                    $scope.addingLanguages = false;
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

        $scope.openedLanguages = [];
        $scope.openLanguages = function (index) {
            $timeout(function () {
                $scope.openedLanguages[index] = true;
            });
        };

    }]);
