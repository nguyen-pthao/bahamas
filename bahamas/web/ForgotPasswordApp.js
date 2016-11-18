/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

var app = angular.module('forgotPassword', ['ngDialog'])
        .controller('ForgotPassCtrl', ['$scope', '$http', 'ngDialog', 'dataSubmit', '$state', function ($scope, $http, ngDialog, dataSubmit, $state) {
                $scope.toResetPassword = {
                    email: ''
                };

                $scope.sendInfo = function () {
                    var url = '/password.forgot';
                    dataSubmit.submitData($scope.toResetPassword, url).then(function (response) {
                        if (response.data.message == 'success') {
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/passwordResetSuccess.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            }).then(function (response) {
                                $state.go('login');
                            })
                        } else {
                            alert('connection error');
                        }
                    });
                };
            }]);

