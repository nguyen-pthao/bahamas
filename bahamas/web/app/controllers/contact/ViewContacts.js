/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('viewContacts', ['$scope', '$http', '$location', 'session', '$window', '$state', '$log', function ($scope, $http, $location, session, $window, $state, $log) {
        
        $scope.backHome = function () {
                      $state.go('admin.homepage');
                };
        
        $scope.retrieveAllContacts = function () {
            var url = "http://localhost:8084/bahamas/contact.retrieve";
            var allContactObjKey = [];
            $http.get(url).then(function (response) {
                $scope.allContactInfo = response.data.contact;
                var firstContactObject = $scope.allContactInfo[0];
                for (contactHeader in firstContactObject) {
                    allContactObjKey.push(contactHeader);
                }
                $scope.allContactObjectKeys = allContactObjKey;
                $scope.totalItems = $scope.allContactInfo.length;
                $scope.currentPage = 1;
                $scope.itemsPerPage = "1";
                var total = $scope.totalItems / $scope.itemsPerPage;
                $scope.totalPages = Math.ceil(total);
                $scope.$watch('currentPage + itemsPerPage', function () {
                    var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
                            end = begin + $scope.itemsPerPage;

                    $scope.filteredContacts = $scope.allContactInfo.slice(begin, end);
                });

//                $scope.maxSize = 100;
//                $scope.bigTotalItems = 175;
//                $scope.bigCurrentPage = 1;
            });
        };


    }]);