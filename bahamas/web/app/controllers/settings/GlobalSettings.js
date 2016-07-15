/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('globalSettings',
        ['$scope', '$state', 'loadContactType', 'loadEventClass', 'loadEventLocation', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass', 'loadMembershipClass', 'loadPaymentMode', 'loadModeOfSendingReceipt', 'loadOfficeList', 'ngDialog', '$uibModal',
            function ($scope, $state, loadContactType, loadEventClass, loadEventLocation, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass, loadMembershipClass, loadPaymentMode, loadModeOfSendingReceipt, loadOfficeList, ngDialog, $uibModal) {

                $scope.backHome = function () {
                    $state.go('admin');
                };
                
                $scope.ok = function(){
                    //$uibModalInstance.close();
                }

                $scope.getList = function () {
                    if ($scope.selectedList == 1) {
                        loadContactType.retrieveContactType().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.contact, function (value, key) {
                                var value1 = value.contactType;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 2) {
                        loadEventClass.retrieveEventClass().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.eventClassList, function (value, key) {
                                var value1 = value.eventClass;
                                $scope.list.push(value1);
                            })
                        })
                    } else if ($scope.selectedList == 3) {
                        loadEventLocation.retrieveEventLocation().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.eventLocationList, function (value, key) {
                                var value1 = value.eventLocation;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 4) {
                        loadLSAClass.retrieveLSAClass().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.lsaClassList, function (value, key) {
                                var value1 = value.lsaClass;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 5) {
                        loadLanguage.retrieveLanguage().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.languageList, function (value, key) {
                                var value1 = value.language;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 6) {
                        loadMembershipClass.retrieveMembershipClass().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.membershipClassList, function (value, key) {
                                var value1 = value.membershipClass;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 7) {
                        loadModeOfSendingReceipt.retrieveModeOfSendingReceipt().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.mode, function (value, key) {
                                var value1 = value.modeOfSendingReceipt;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 8) {
                        loadOfficeList.retrieveOfficeList().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.officeList, function (value, key) {
                                var value1 = value.office;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 9) {
                        loadPaymentMode.retrievePaymentMode().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.paymentModeList, function (value, key) {
                                var value1 = value.paymentMode;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 10) {
                        loadPermissionLevel.retrievePermissionLevel().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.permissionLevelList, function (value, key) {
                                var value1 = value.permissionLevel;
                                $scope.list.push(value1);
                            })
                        });
                    } else if ($scope.selectedList == 11) {
                        loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                            $scope.list = [];
                            angular.forEach(response.data.teamAffiliationList, function (value, key) {
                                var value1 = value.teamAffiliation;
                                $scope.list.push(value1);
                            })
                        });
                    }
                };

                $scope.remove = function (scope) {
                    scope.remove();
                };

                $scope.newItem = function () {
//                    var nodeData = $scope.data[$scope.data.length - 1];
//                    $scope.data.push({
//                        id: $scope.data.length + 1,
//                        title: 'node ' + ($scope.data.length + 1)
//                    });

                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: './style/ngTemplate/newItem.html'
                    });

                };

            }]);



