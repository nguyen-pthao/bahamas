/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamasLogin');

app.controller('logoutController', ['$scope', '$location', 'session', function($scope, $location, session) {
    $scope.logout = function() {
        session.terminateSession();
    };
}]);
