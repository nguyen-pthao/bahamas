/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Created by Marcus Ong


var app = angular.module('bahamas');

app.controller('logoutController', ['$scope', '$location', 'session', '$state', 'authorization', function ($scope, $location, session, $state, authorization) {
        $scope.logoutUser = function () {
            session.terminateSession();
            authorization.clear();
            $state.go('login');
        }
    }]);