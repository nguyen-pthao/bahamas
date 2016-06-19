/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('viewContacts',
        ['$scope', '$http', '$location', 'session', '$window', '$state', '$log', 'loadAllContacts',
            function ($scope, $http, $location, session, $window, $state, $log, loadAllContacts, filterFilter) {

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
                    }
                    //for headers
                    var allContactObjKey = [];
                    loadAllContacts.retrieveAllContacts(contactToRetrieve).then(function (response) {
                        $scope.allContactInfo = response.data.contact;
                        var firstContactObject = $scope.allContactInfo[0];
                        for (contactHeader in firstContactObject) {
                            allContactObjKey.push(contactHeader);
                        }
                        $scope.allContactObjectKeys = allContactObjKey;
                        
                        $scope.isAuthorised = true;
                        $scope.takeNoColumns = 5;
                        $scope.userType = session.getSession('userType');
                        if ($scope.userType === 'novice') {
                            $scope.isAuthorised = false;
                            $scope.allContactObjectKeySliced = $scope.allContactObjectKeys;
                        } else if ($scope.userType === 'associate') {
                            $scope.isAuthorised = false;
                            $scope.takeNoColumns = 4;
                            $scope.allContactObjectKeySliced = $scope.allContactObjectKeys.slice(0,4);
                            console.log($scope.allContactObjectKeySliced.length);
                        } else if ($scope.userType === 'eventleader') {
                            $scope.isAuthorised = false;
                            $scope.allContactObjectKeySliced = $scope.allContactObjectKeys.slice(0, 5);
                        } else {
                            $scope.allContactObjectKeySliced = $scope.allContactObjectKeys.slice(0,5);
                        }
                        $scope.totalItems = $scope.allContactInfo.length;
                        var newContactArray = [];
                        angular.forEach($scope.allContactInfo, function (obj) {
                            if($scope.takeNoColumns === 5){
                                var filteredObj = {};
                                filteredObj['name'] = obj.name;
                                filteredObj['alt name'] = obj['alt name'];
                                filteredObj['phone'] = obj.phone;
                                filteredObj['email'] = obj.email;
                                filteredObj['contact type'] = obj['contact type'];
                                newContactArray.push(filteredObj);
                            }else if($scope.takeNoColumns === 4){
                                console.log(obj);
                                var filteredObj = {};
                                filteredObj['name'] = obj.name;
                                filteredObj['alt name'] = obj['altname'];
                                filteredObj['email'] = obj.email;
                                filteredObj['contact type'] = obj['contactType'];
                                newContactArray.push(filteredObj);
                                console.log(filteredObj);
                            }
                        });
                        if ($scope.userType === 'novice') {
                            $scope.allFilteredContact = $scope.allContactInfo;
                        }else{
                             $scope.allFilteredContact = newContactArray;
                        }
                        $scope.currentPage = 1;
                        $scope.itemsPerPage = $scope.allFilteredContact.length;
                        $scope.itemsPerPageChanged = function () {
                            if ($scope.itemsPerPage == 'toAll') {
                                $scope.itemsPerPage = $scope.allFilteredContact.length;
                            }
                            var total = $scope.totalItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.filteredContacts = $scope.allFilteredContact.slice(begin, end);
                            });
                        };
                        var total = $scope.totalItems / $scope.itemsPerPage;
                        $scope.totalPages = Math.ceil(total);
                        $scope.$watch('currentPage + itemsPerPage', function () {
                            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                            var end = begin + parseInt($scope.itemsPerPage);

                            $scope.filteredContacts = $scope.allFilteredContact.slice(begin, end);
                        });
                        $scope.pageChanged = function () {
                            var total = $scope.totalItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);

                                $scope.filteredContacts = $scope.allFilteredContact.slice(begin, end);
                            });
                        };
                    });
                };
                $scope.predicate = '';
                $scope.reverse = true;

                $scope.order = function (predicate) {
                    $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
                    $scope.predicate = predicate;
                };
            }])