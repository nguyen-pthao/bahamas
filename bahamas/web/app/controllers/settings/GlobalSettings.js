/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('globalSettings',
        ['$scope', '$rootScope', '$state', 'session', '$http', 'loadContactType', 'loadEventClass', 'loadEventLocation', 'loadEventStatus', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass', 'loadMembershipClass', 'loadPaymentMode', 'loadModeOfSendingReceipt', 'loadOfficeList', 'ngDialog', '$uibModal', 'localStorageService',
            function ($scope, $rootScope, $state, session, $http, loadContactType, loadEventClass, loadEventLocation, loadEventStatus, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass, loadMembershipClass, loadPaymentMode, loadModeOfSendingReceipt, loadOfficeList, ngDialog, $uibModal, localStorageService) {
                localStorageService.set('isLocation', false);
                $scope.backHome = function () {
                    $state.go('admin');
                };
                $scope.listNotChosen = true;
                $rootScope.list = null;
                $scope.getList = function () {
                    $scope.listNotChosen = false;
                    if ($scope.selectedList == 1) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadContactType.retrieveContactType().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.contact, function (value, key) {
                                var value1 = value.contactType;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 2) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadEventClass.retrieveEventClass().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.eventClassList, function (value, key) {
                                var value1 = value.eventClass;
                                $rootScope.list.push(value1);
                            })
                        })
                    } else if ($scope.selectedList == 3) {
                        localStorageService.set('isLocation', true);
                        $scope.myPromise = loadEventLocation.retrieveEventLocation().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.eventLocationList, function (value, key) {
                                var obj = {
                                    'location': value.eventLocation,
                                    'address': value.address,
                                    'zipcode': value.zipcode
                                }
                                $rootScope.list.push(obj);
                            })
                        });
                    } else if ($scope.selectedList == 4) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadEventStatus.retrieveEventStatus().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.eventStatusList, function (value, key) {
                                var value1 = value.eventStatus;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 5) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadLSAClass.retrieveLSAClass().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.lsaClassList, function (value, key) {
                                var value1 = value.lsaClass;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 6) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadLanguage.retrieveLanguage().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.languageList, function (value, key) {
                                var value1 = value.language;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 7) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadMembershipClass.retrieveMembershipClass().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.membershipClassList, function (value, key) {
                                var value1 = value.membershipClass;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 8) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadModeOfSendingReceipt.retrieveModeOfSendingReceipt().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.mode, function (value, key) {
                                var value1 = value.modeOfSendingReceipt;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 9) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadOfficeList.retrieveOfficeList().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.officeList, function (value, key) {
                                var value1 = value.office;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 10) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadPaymentMode.retrievePaymentMode().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.paymentModeList, function (value, key) {
                                var value1 = value.paymentMode;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 11) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadPermissionLevel.retrievePermissionLevel().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.permissionLevelList, function (value, key) {
                                var value1 = value.permissionLevel;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 12) {
                        localStorageService.set('isLocation', false);
                        $scope.myPromise = loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.teamAffiliationList, function (value, key) {
                                var value1 = value.teamAffiliation;
                                $rootScope.list.push(value1);
                            })
                        });
                    }
                };

                $scope.remove = function (scope) {
                    scope.remove();
                };

                $scope.newItem = function () {
                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: './style/ngTemplate/newItem.html',
                        controller: 'ModalInstanceCtrl',
                        size: "sm"
                    });
                };

                $scope.submit = function () {
                    ngDialog.openConfirm({
                        templateUrl: './style/ngTemplate/updateListConfirm.html',
                        className: 'ngdialog-theme-default',
                        closeByDocument: false,
                        closeByEscape: false
                    }).then(function () {
                        $scope.listToSend = {};
                        $scope.listToSend['token'] = session.getSession('token');
                        $scope.listToSend['list'] = $rootScope.list;
                        $scope.listToSend['selectedList'] = $scope.selectedList;
                        //call api to update list in DB. and upon success prompt update success.
                        $http({
                            method: 'POST',
                            url: $rootScope.commonUrl + '/updatelist',
                            data: JSON.stringify($scope.listToSend)
                        }).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    templateUrl: './style/ngTemplate/updateListSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    closeByDocument: false,
                                    closeByEscape: false
                                })
                            }
                        })
                    })
                };

            }]);

app.controller('ModalInstanceCtrl', function ($scope, $rootScope, $uibModalInstance, localStorageService) {
    $scope.ok = function () {
        if (localStorageService.get('isLocation') == true) {
            var obj = {
                'location': $scope.input,
                'address': '',
                'zipcode': ''
            }
            $rootScope.list.push(obj);
        } else {
            $rootScope.list.push($scope.input);
        }
        $uibModalInstance.close();
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

