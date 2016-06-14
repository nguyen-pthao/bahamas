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

                var user = session.getSession('userType');
                var currentState = user + '.viewContacts';
                var homepage = user + '.homepage';

                $scope.backHome = function () {
                    $state.go(homepage);
                };

                $scope.viewContact = function () {
                    $state.reload(currentState);
                };

                $scope.retrieveAllContacts = function () {
//            console.log(angular.fromJson(session.getSession('contact')));
//            console.log(angular.fromJson(session.getSession('teams')));
                    $scope.checkNovice = session.getSession('userType');
                    if ($scope.checkNovice === 'novice') {
                        var contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'teamname': "",
                            'permission': session.getSession('userType')
                        };
                    } else {
                        var contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'teamname': angular.fromJson(session.getSession('teams'))[0].teamname,
//                        'teamname': "",
                            'permission': session.getSession('userType')
                        };
                    }
                    var allContactObjKey = [];
                    console.log(contactToRetrieve);
                    loadAllContacts.retrieveAllContacts(contactToRetrieve).then(function (response) {
                        console.log(response);
                        $scope.allContactInfo = response.data.contact;
                        var firstContactObject = $scope.allContactInfo[0];
                        for (contactHeader in firstContactObject) {
                            allContactObjKey.push(contactHeader);
                        }
                        $scope.isAuthorised = true;
                        $scope.userType = session.getSession('userType');
                        if ($scope.userType === 'novice') {
                            $scope.isAuthorised = false;
                        } else if ($scope.userType === 'associate') {
                            $scope.isAuthorised = false;
                        } else if ($scope.userType === 'eventleader') {
                            $scope.isAuthorised = false;
                        }
                        $scope.allContactObjectKeys = allContactObjKey;
                        $scope.totalItems = $scope.allContactInfo.length;
                        $scope.currentPage = 1;
                        $scope.itemsPerPage = $scope.allContactInfo.length;
                        $scope.itemsPerPageChanged = function () {
                            if ($scope.itemsPerPage == 'toAll') {
                                $scope.itemsPerPage = $scope.allContactInfo.length;
                            }
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
                    });
                };


            }]);