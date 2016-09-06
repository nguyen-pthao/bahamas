/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditPersonalInfo', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'loadContactType',
    function ($scope, session, ngDialog, $timeout, dataSubmit, loadContactType) {

        $scope.loadContactTypeList = function () {
            loadContactType.retrieveContactType().then(function (response) {
                $scope.contactTypeList = response.data.contact;
            });
        };
        //contact
        $scope.resultContact = {
            status: false,
            message: ''
        };
        $scope.editTheContact = function () {
            $scope.editContact['token'] = session.getSession('token');
            //if ($scope.editMode == 'true') {
                $scope.editContact['contact_id'] = $scope.contactToEditCID;
            //}
//            } else {
//                $scope.editContact['contact_id'] = $scope.contactToEditCID;
//            }
            $scope.editContact['user_type'] = session.getSession('userType');
            if ($scope.editContact['date_of_birth'] == null) {
                $scope.editContact['date_of_birth'] = '';
            } else if (angular.isUndefined($scope.editContact['date_of_birth'])) {
                $scope.editContact['date_of_birth'] = '';
            } else if (isNaN($scope.editContact['date_of_birth'])) {
                $scope.editContact['date_of_birth'] = '';
            } else {
                $scope.editContact['date_of_birth'] = $scope.editContact['date_of_birth'].valueOf() + "";
            }
            //to be modified
            var url = AppAPI.updateContact;
            dataSubmit.submitData($scope.editContact, url).then(function (response) {
                $scope.resultContact.status = true;
                if (response.data.message == 'success') {
                    $scope.resultContact.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editPersonalForm.$setValidity();
                    $timeout(function () {
                        $scope.resultContact.status = false;
                    }, 5000);
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
        
        $scope.open = function () {
            $timeout(function () {
                $scope.opened = true;
            });
        };

    }]);