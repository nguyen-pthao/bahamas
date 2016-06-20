/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewIndivContact', ['$scope', 'session', '$state', function ($scope, session, $state) {
        $scope.contact = session.getSession('contactToDisplay');
        $scope.permission = session.getSession('userType');

        $scope.isAuthorised = false;
        if ($scope.permission === 'admin') {
            $scope.isAuthorised = true;
        } else if ($scope.permission === 'teammanager') {
            $scope.isAuthorised = true;
        }

        $scope.contactToDisplay = angular.fromJson($scope.contact);

        var toAllContacts = $scope.permission + '.viewContacts';
        var homepage = $scope.permission + '.homepage';
        $scope.backHome = function () {
            $state.go(homepage);
        };

        $scope.viewContact = function () {
            $state.go(toAllContacts);
        };
    }]);
