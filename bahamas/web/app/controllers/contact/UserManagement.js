/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('userManagement', ['$scope', 'session', '$state', function($scope, session, state){
        $scope.ownContact = angular.fromJson(session.getSession('contact'));
}]);