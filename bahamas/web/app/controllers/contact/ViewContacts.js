/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('viewContacts', ['$scope', '$http', '$location', 'session', '$window', '$state', function ($scope, $http, $location, session, $window, $state) {

        $scope.retrieveAllContacts = function () {
            var url = "http://localhost:8084/bahamas/contact.retrieve";
            $http.get(url).then(function (response) {
                $scope.allContactInfo = response.data.contact;
                
            });
        };

    }]);