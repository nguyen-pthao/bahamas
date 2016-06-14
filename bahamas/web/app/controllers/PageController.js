/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('pageController', ['$scope', '$location', 'session', '$state', function ($scope, $location, session, $state) {
        $scope.populatePage = function () {
            $scope.name = session.getSession('contact').name;
            $scope.userType = session.getSession('usertype');
            $scope.dateCreated = session.getSession('datecreated');
        }
    }]);