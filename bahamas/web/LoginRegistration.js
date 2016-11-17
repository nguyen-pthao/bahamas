/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('registration');

app.controller('loginRegisterController', ['$scope', 'session', '$state', 'dataSubmit', '$uibModal', '$timeout',
    function ($scope, session, $state, dataSubmit, $uibModal, $timeout) {
                
        $scope.login = function () {
            var url = "/form.verify";
            var datasend = {
                code: $scope.authenticationCode
            };
            dataSubmit.submitData(datasend, url).then(function (response) {
                var result = response.data;
//                console.log(response);
                if (result.message === "success authorized access to registration form") {
//STORE AUTHORISED CODE INFO IN SESSION SERVICE
                    session.setSession('remoteToken', result.token);
                    session.setSession('formId', result['form_id']);
                    $state.go('register.contact');
                } else {
                    $scope.error = true;
                }
            }, function () {
                window.alert("Fail to send request!");
            });
        };
    }]);
