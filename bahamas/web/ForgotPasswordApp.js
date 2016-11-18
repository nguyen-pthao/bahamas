/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

var app = angular.module('forgotPassword', ['ngDialog'])
        .controller('ForgotPassCtrl', ['$scope', '$http', 'ngDialog', function ($scope, $http, ngDialog) {
                $scope.toResetPassword = {
                    email: ''
                };

                $scope.sendInfo = function () {
                    $http({
                        method: 'POST',
                        url: 'https://rmsdev.twc2.org.sg/bahamas/password.forgot',
                        data: JSON.stringify($scope.toResetPassword)
                    }).then(function (response) {
                        if (response.data.message == 'success') {
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/passwordResetSuccess.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            }).then(function (response) {
                                window.location = "https://rmsdev.twc2.org.sg";
                            })
                        } else {
                            alert('connection error');
                        }
                    });
                };
            }]);

