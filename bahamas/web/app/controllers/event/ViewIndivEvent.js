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
                
                $scope.toEvents = function (){
                    var url = user + '.viewUpcomingEvents';
                    $state.go(url);
                };

                $scope.map = {center: {latitude: 1.302918, longitude: 103.864964}, zoom: 15, options: {scrollwheel: false}, control:{}};

                $scope.marker = {coords: {latitude: 1.302918, longitude: 103.864964}, id: 1};

            }]);