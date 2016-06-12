/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('viewContacts',
        ['$scope', '$http', '$location', 'session', '$window', '$state', '$log', 'loadAllContacts',
            function ($scope, $http, $location, session, $window, $state, $log, loadAllContacts) {

                $scope.backHome = function () {
                    $state.go('admin.homepage');
                };

                $scope.retrieveAllContacts = function () {
//            console.log(angular.fromJson(session.getSession('contact')));
//            console.log(angular.fromJson(session.getSession('teams')));
                    var contactToRetrieve = {
                        'token': session.getSession('token'),
                        'cid': angular.fromJson(session.getSession('contact')).cid,
//                'teamname': angular.fromJson(session.getSession('teams'))[0].teamName,
                        'teamname': "",
                        'permission': session.getSession('userType')
                    };
                    var allContactObjKey = [];
                    loadAllContacts.retrieveAllContacts(contactToRetrieve).then(function (response) {
                        $scope.allContactInfo = response.data.contact;
                        var firstContactObject = $scope.allContactInfo[0];
                        for (contactHeader in firstContactObject) {
                            allContactObjKey.push(contactHeader);
                        }
                        $scope.isAuthorised = true;
                        $scope.userType = session.getSession('userType');
                        if ($scope.userType === 'novice') {
                            $scope.isAuthorised = false;
                        }
                        $scope.allContactObjectKeys = allContactObjKey;
                        $scope.totalItems = $scope.allContactInfo.length;
                        $scope.currentPage = 1;
                        $scope.itemsPerPage = $scope.allContactInfo.length;

                        $scope.itemsPerPageChanged = function () {
                            var total = $scope.totalItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.filteredContacts = $scope.allContactInfo.slice(begin, end);
                            });
                        };
                        var total = $scope.totalItems / $scope.itemsPerPage;

                        $scope.totalPages = Math.ceil(total);
                        $scope.$watch('currentPage + itemsPerPage', function () {
                            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                            var end = begin + parseInt($scope.itemsPerPage);

                            $scope.filteredContacts = $scope.allContactInfo.slice(begin, end);
                        });

                        $scope.pageChanged = function () {
                            var total = $scope.totalItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);

                                $scope.filteredContacts = $scope.allContactInfo.slice(begin, end);
                            });
                        };

//                $scope.maxSize = 100;
//                $scope.bigTotalItems = 175;
//                $scope.bigCurrentPage = 1;
                    });
                };


            }]);