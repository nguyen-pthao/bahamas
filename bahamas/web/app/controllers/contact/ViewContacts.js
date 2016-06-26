/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('viewContacts',
        ['$scope', 'session', '$state','loadAllContacts', 'filterFilter',
            function ($scope, session, $state, loadAllContacts, filterFilter) {

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
                    
                    $scope.checkNovice = session.getSession('userType');
                    $scope.showFive = false;
                    //to determine what to send back to backend.
                    if (session.getSession('teams') === 'undefined') {
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
                            'permission': session.getSession('userType')
                        };
                    }
                    //for headers
                    var allContactObjKey = [];
                    loadAllContacts.retrieveAllContacts(contactToRetrieve).then(function (response) {
                        $scope.allContactInfo = response.data.contact;
//                        console.log($scope.allContactInfo);
                        var firstContactObject = $scope.allContactInfo[0];
                        for (contactHeader in firstContactObject) {
                            allContactObjKey.push(contactHeader);
                        }
                        $scope.allContactObjectKeys = allContactObjKey;

                        $scope.isAuthorised = true;
                        $scope.takeNoColumns = 5;
                        $scope.userType = session.getSession('userType');
                        if ($scope.userType === 'associate') {
                            $scope.isAuthorised = false;
                            $scope.takeNoColumns = 4;
                            $scope.allContactObjectKeySliced = $scope.allContactObjectKeys.slice(0, 4);
                        } else if ($scope.userType === 'eventleader') {
                            $scope.isAuthorised = false;
                            $scope.allContactObjectKeySliced = $scope.allContactObjectKeys.slice(0, 5);
                            $scope.showFive = true;
                        } else {
                            $scope.allContactObjectKeySliced = $scope.allContactObjectKeys.slice(0, 5);
                            $scope.showFive = true;
                        }
                        $scope.totalItems = $scope.allContactInfo.length;

                        $scope.currentPage = 1;
                        $scope.itemsPerPage = $scope.allContactInfo.length;
                        $scope.allFilteredContacts = $scope.allContactInfo;
                        $scope.isAll = false;
                        $scope.itemsPerPageChanged = function () {
                            if ($scope.itemsPerPage == 'toAll') {
                                $scope.itemsPerPage = $scope.allFilteredContacts.length;
                                $scope.isAll = true;
                            } else {
                                $scope.isAll = false;
                            }
                            $scope.allFilteredContacts = filterFilter($scope.allContactInfo, $scope.searchContacts);
                            var total = $scope.allFilteredContacts.length / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            if($scope.totalPages === 0){
                                $scope.totalPages = 1;
                            }
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.splitContacts = $scope.allFilteredContacts.slice(begin, end);
                            });
                        };

                        var total = $scope.allFilteredContacts.length / $scope.itemsPerPage;
                        $scope.totalPages = Math.ceil(total);
                        if($scope.totalPages === 0){
                            $scope.totalPages = 1;
                        }
                        $scope.$watch('currentPage + itemsPerPage', function () {
                            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                            var end = begin + parseInt($scope.itemsPerPage);

                            $scope.splitContacts = $scope.allFilteredContacts.slice(begin, end);
                        });

                        $scope.$watch('searchContacts', function (term) {
                            $scope.allFilteredContacts = filterFilter($scope.allContactInfo, term);
                            $scope.totalItems = $scope.allFilteredContacts.length;
                            var total = $scope.allFilteredContacts.length / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            if($scope.totalPages === 0){
                                $scope.totalPages = 1;
                            }
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.splitContacts = $scope.allFilteredContacts.slice(begin, end);
                            });
                        });

                        $scope.foo = function ($event, contact) {
                            var toURL = $scope.userType + ".viewIndivContact";
                            var contactInfo = angular.toJson(contact);
                            session.setSession('contactToDisplay', contactInfo);
                            $state.go(toURL);
                        };
                    });
                    
                };
              
                $scope.predicate = '';
                $scope.reverse = true;

                $scope.order = function (predicate) {
                    $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
                    $scope.predicate = predicate;
                };
            }]);