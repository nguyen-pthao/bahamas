/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

var app = angular.module('forgotPassword', []);

app.controller('ForgotPassCtrl', ['$scope', '$http', function($scope, $http){
    $scope.toResetPassword = {
        username: '',
        email: ''
    }  
    
    $scope.sendInfo = function(){
        console.log($scope.toResetPassword);
        $http({
                method: 'POST',
                url:  'https://rms.twc2.org.sg/bahamas/password.forgot',
                data: JSON.stringify($scope.toResetPassword)
            }).then(function(response){
                console.log(response);
            })
    }
}]);

