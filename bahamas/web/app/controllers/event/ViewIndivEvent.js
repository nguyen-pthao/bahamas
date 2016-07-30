/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewIndivEvent',
        ['$scope', 'session', '$state', 'filterFilter', 'ngDialog', 'dataSubmit',
            function ($scope, session, $state, filterFilter, ngDialog, dataSubmit) {
                var user = session.getSession('userType');
                $scope.backHome = function () {
                    $state.go(user);
                };

            }]);