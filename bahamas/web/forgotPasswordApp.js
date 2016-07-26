/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

var app = angular.module('forgotPassword', ['ngDialog'])
        .controller('ForgotPassCtrl', ['$scope', '$http', 'ngDialog', function ($scope, $http, ngDialog) {
                $scope.toResetPassword = {
                    username: '',
                    email: ''
                }

                $scope.sendInfo = function () {
                    console.log($scope.toResetPassword);
                    $http({
                        method: 'POST',
                        url: 'https://rms.twc2.org.sg/bahamas/password.forgot',
                        data: JSON.stringify($scope.toResetPassword)
                    }).then(function (response) {
                        if (response.data.message == 'success') {
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/passwordResetSuccess.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            }).then(function (response) {
                                window.location = "https://rms.twc2.org.sg";
                            })
                        } else {
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/passwordResetFailure.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            })
                        }
                    })
                }
            }]);

