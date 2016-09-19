/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditProxy', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService) {

        $scope.onSelect = function ($item, $model, $label) {
            $scope.searchContact($item);
        };
        
        $scope.searchContact = function (selected) {
            $scope.selectedProxyCid = selected.cid;
        };
        var permission = session.getSession('userType');
        //proxy
        $scope.addingProxy = false;
        $scope.addNewProxy = function () {
            $scope.addingProxy = !$scope.addingProxy;
        };
        $scope.resultProxy = {
            'proxy_id': '',
            status: false,
            message: ''
        };
        $scope.editTheProxy = function ($event, proxy) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            datasend['user_type'] = permission;
            datasend['proxy_of'] = proxy['proxy_id'];
            datasend['principal_of'] = proxy['principal_id'];
            datasend['proxy_standing'] = proxy['proxy_standing'];
            datasend['remarks'] = proxy['remarks'];
            if (proxy['date_obsolete'] == null) {
                datasend['date_obsolete'] = '';
            } else if (isNaN(proxy['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else if (angular.isUndefined(proxy['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else {
                datasend['date_obsolete'] = proxy['date_obsolete'].valueOf() + "";
            }
            var url = AppAPI.updateProxy;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultProxy.status = true;
                if (response.data.message == 'success') {
                    $scope.resultProxy['proxy_id'] = datasend['proxy_of'];
                    $scope.resultProxy.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editProxyForm.$setValidity();
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
                    $scope.resultProxy.status = false;
                }, 1000);
            }, function () {
                window.alert("Fail to send request!");
            });
        };
        $scope.deleteTheProxy = function ($event, proxy) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteProxy = {};
                deleteProxy['token'] = session.getSession('token');
                deleteProxy['proxy_of'] = proxy['proxy_id'];
                deleteProxy['principal_of'] = proxy['principal_id'];
                deleteProxy['user_type'] = permission;
                var url = AppAPI.deleteProxy;
                deleteService.deleteDataService(deleteProxy, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete proxy");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };

        $scope.newProxy = {
            'token': session.getSession('token'),
            'proxy_of': -1,
            'user_type': permission,
            'principal_of': '',
            'proxy_standing': '',
            'remarks': '',
            'date_obsolete': ''
        };
        $scope.copyProxy = angular.copy($scope.newProxy);
        $scope.submitNewProxy = {
            'submittedProxy': false,
            'message': ''
        };
        $scope.addProxy = function () {
            $scope.newProxy['proxy_of'] = $scope.selectedProxyCid;
            if ($scope.editMode == 'true') {
                $scope.newProxy['principal_of'] = $scope.contactToEditCID;
            } else {
                $scope.newProxy['principal_of'] = $scope.contactToEditCID;
            }
            if ($scope.newProxy['date_obsolete'] == null) {
                $scope.newProxy['date_obsolete'] = '';
            } else if (isNaN($scope.newProxy['date_obsolete'])) {
                $scope.newProxy['date_obsolete'] = '';
            } else if (angular.isUndefined($scope.newProxy['date_obsolete'])) {
                $scope.newProxy['date_obsolete'] = '';
            } else {
                $scope.newProxy['date_obsolete'] = $scope.newProxy['date_obsolete'].valueOf() + "";
            }
            var url = AppAPI.addProxy;
            dataSubmit.submitData($scope.newProxy, url).then(function (response) {
                if (response.data.message == 'success') {
                    $scope.submitNewProxy.submittedProxy = true;
                    $scope.submitNewProxy.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.newProxy = angular.copy($scope.copyProxy);
                    $scope.form.editProxyForm.$setValidity();
                    $scope.form.editProxyForm.$setPristine();
                    $timeout(function () {
                        $scope.submitNewProxy.submittedProxy = false;
                    }, 1000);
                    $scope.addingProxy = false;
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

        $scope.openedProxy = [];
        $scope.openProxy = function (index) {
            $timeout(function () {
                $scope.openedProxy[index] = true;
            });
        };
    }]);
