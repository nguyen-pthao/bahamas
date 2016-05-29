/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('Admin',[]);

app.controller('AdminController', ['$scope', '$location', 'session', function($scope, $location, session){
    $scope.user = {
        username: session.getSession('username'),
        token: session.getSession('token'),
        userType: session.getSession('userType')
    };
    
    
}]);
