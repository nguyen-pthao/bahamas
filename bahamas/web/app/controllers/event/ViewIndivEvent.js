/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewIndivEvent',
        ['$scope', 'session', '$state', 'filterFilter', 'ngDialog', 'dataSubmit', '$stateParams',
            function ($scope, session, $state, filterFilter, ngDialog, dataSubmit, $stateParams) {
                var user = session.getSession('userType');
                var eventId = $stateParams.eventId;
                console.log(eventId);
                $scope.backHome = function () {
                    $state.go(user);
                };
                
                $scope.toEvents = function (){
                    var url = user + '.viewUpcomingEvents';
                    $state.go(url);
                };
                
                $scope.retrieveEvent = function (){
                    $scope.toRetrieve = {
                        'token': session.getSession('token'),
                        'eventId': eventId
                    }
                    var url = '/event.retrieveindiv';
                    dataSubmit.submitData($scope.toRetrieve, url).then(function(response){
                        console.log(response);
                        $scope.eventInfo = response.data;
                    })
                }
                
                $scope.map = {center: {latitude: 1.302918, longitude: 103.864964}, zoom: 15, options: {scrollwheel: false}, control:{}};

                $scope.marker = {coords: {latitude: 1.302918, longitude: 103.864964}, id: 1};

            }]);