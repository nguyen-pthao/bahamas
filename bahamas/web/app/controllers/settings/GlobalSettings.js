/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('globalSettings',
        ['$scope', '$state', 'loadContactType', 'loadEventClass', 'loadEventLocation','loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass', 'loadMembershipClass', 'loadPaymentMode', 'loadModeOfSendingReceipt', 'loadOfficeList',
            function ($scope, $state, loadContactType, loadEventClass, loadEventLocation,loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass, loadMembershipClass, loadPaymentMode, loadModeOfSendingReceipt, loadOfficeList) {

                $scope.backHome = function () {
                    $state.go('admin');
                };

                $scope.getList = function () {
                    if ($scope.selectedList == 1) {
                        loadContactType.retrieveContactType().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 2) {
                        loadEventClass.retrieveEventClass().then(function(response){
                            console.log(response);
                        })
                    } else if ($scope.selectedList == 3) {
                        loadEventLocation.retrieveEventLocation().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 4) {
                        loadLSAClass.retrieveLSAClass().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 5) {
                        loadLanguage.retrieveLanguage().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 6) {
                        loadMembershipClass.retrieveMembershipClass().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 7) {
                        loadModeOfSendingReceipt.retrieveModeOfSendingReceipt().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 8) {
                        loadOfficeList.retrieveOfficeList().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 9) {
                        loadPaymentMode.retrievePaymentMode().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 10) {
                        loadPermissionLevel.retrievePermissionLevel().then(function (response) {
                            console.log(response);
                        });
                    } else if ($scope.selectedList == 11) {
                        loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                            console.log(response);
                        });
                    }
                };

            }]);



