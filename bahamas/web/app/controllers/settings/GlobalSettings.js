/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('globalSettings',
        ['$scope', '$rootScope', '$state', 'loadContactType', 'loadEventClass', 'loadEventLocation', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass', 'loadMembershipClass', 'loadPaymentMode', 'loadModeOfSendingReceipt', 'loadOfficeList', 'ngDialog', '$uibModal',
            function ($scope, $rootScope, $state, loadContactType, loadEventClass, loadEventLocation, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass, loadMembershipClass, loadPaymentMode, loadModeOfSendingReceipt, loadOfficeList, ngDialog, $uibModal) {

                $scope.backHome = function () {
                    $state.go('admin');
                };
                $scope.listNotChosen = true;
                $scope.getList = function () {
                    $scope.listNotChosen = false;
                    if ($scope.selectedList == 1) {
                        loadContactType.retrieveContactType().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.contact, function (value, key) {
                                var value1 = value.contactType;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 2) {
                        loadEventClass.retrieveEventClass().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.eventClassList, function (value, key) {
                                var value1 = value.eventClass;
                                $rootScope.list.push(value1);
                            })
                        })
                    } else if ($scope.selectedList == 3) {
                        loadEventLocation.retrieveEventLocation().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.eventLocationList, function (value, key) {
                                var value1 = value.eventLocation;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 4) {
                        loadLSAClass.retrieveLSAClass().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.lsaClassList, function (value, key) {
                                var value1 = value.lsaClass;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 5) {
                        loadLanguage.retrieveLanguage().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.languageList, function (value, key) {
                                var value1 = value.language;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 6) {
                        loadMembershipClass.retrieveMembershipClass().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.membershipClassList, function (value, key) {
                                var value1 = value.membershipClass;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 7) {
                        loadModeOfSendingReceipt.retrieveModeOfSendingReceipt().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.mode, function (value, key) {
                                var value1 = value.modeOfSendingReceipt;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 8) {
                        loadOfficeList.retrieveOfficeList().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.officeList, function (value, key) {
                                var value1 = value.office;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 9) {
                        loadPaymentMode.retrievePaymentMode().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.paymentModeList, function (value, key) {
                                var value1 = value.paymentMode;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 10) {
                        loadPermissionLevel.retrievePermissionLevel().then(function (response) {
                            $rootScope.list = [];
                            angular.forEach(response.data.permissionLevelList, function (value, key) {
                                var value1 = value.permissionLevel;
                                $rootScope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 11) {
                        loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
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
                    }).then(function(){
                        console.log($rootScope.list);
                        //call api to update list in DB. and upon success prompt update success.
                    })
                };

            }]);

app.controller('ModalInstanceCtrl', function ($scope, $rootScope, $uibModalInstance) {
    $scope.ok = function () {
        $rootScope.list.push($scope.input);
        $uibModalInstance.close();
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

