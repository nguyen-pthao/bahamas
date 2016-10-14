/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('viewContacts',
        ['$scope', 'session', '$state', 'loadAllContacts', 'filterFilter', 'ngDialog', 'deleteService', 'loadTeamAffiliation',
            function ($scope, session, $state, loadAllContacts, filterFilter, ngDialog, deleteService, loadTeamAffiliation) {

                var user = session.getSession('userType');
                var currentState = user + '.viewContacts';

                $scope.backHome = function () {
                    $state.go(user);
                };
                $scope.userLoggedIn = user;
                $scope.viewContact = function () {
                    $state.reload(currentState);
                };
                $scope.userViewContacts = user + "/" + 'viewContacts';
                
                $scope.loadTeamList = function () {
                    loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                        $scope.teamList = response.data.teamAffiliationList;
                        if (user === 'eventleader' || user === 'associate' || user === 'novice') {
                            $scope.teamList.unshift({'teamAffiliation': 'MY TEAMS'});
                            $scope.teamList.unshift({'teamAffiliation': 'ALL'});
                        } else {
                            $scope.teamList.unshift({'teamAffiliation': 'EXPIRED MEMBERS'});
                            $scope.teamList.unshift({'teamAffiliation': 'CURRENT MEMBERS'});
                            $scope.teamList.unshift({'teamAffiliation': 'DONORS'});
                            $scope.teamList.unshift({'teamAffiliation': 'MY TEAMS'});
                            $scope.teamList.unshift({'teamAffiliation': 'ALL'});
                        }
                        $scope.teamFilter = $scope.teamList[0].teamAffiliation;
                    });
                };

                $scope.retrieveAllContacts = function () {
                    $scope.checkNovice = session.getSession('userType');
                    $scope.showFive = false;
                    //to determine what to send back to backend.
                    var filter;
                    var contactToRetrieve;
                    if ($scope.teamFilter == "ALL") {
                        filter = "";
                    } else if ($scope.teamFilter == "MY TEAMS") {
                        filter = "my_team";
                    } else if ($scope.teamFilter == "DONORS") {
                        filter = "donors";
                    } else if ($scope.teamFilter == "CURRENT MEMBERS") {
                        filter = "current_members";
                    } else if ($scope.teamFilter == "EXPIRED MEMBERS") {
                        filter = "expired_members";
                    } else {
                        filter = $scope.teamFilter;
                    }
                    if (session.getSession('teams') === 'undefined') {
                        contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'teamname': "",
                            'permission': session.getSession('userType'),
                            'teamFilter': filter
                        };
                    } else {
                        contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'teamname': angular.fromJson(session.getSession('teams'))[0].teamname,
                            'permission': session.getSession('userType'),
                            'teamFilter': filter
                        };
                    }
                    //for headers
                    //var allContactObjKey = [];
                    $scope.myPromise = loadAllContacts.retrieveAllContacts(contactToRetrieve).then(function (response) {
                        $scope.allContactInfo = response.data.contact;
                        $scope.isAuthorised = true;
                        $scope.canDelete = false;
                        $scope.canEmailList = false;
                        $scope.takeNoColumns = 5;
                        $scope.userType = session.getSession('userType');
                        if ($scope.userType === 'admin') {
                            $scope.canDelete = true;
                            $scope.canEmailList = true;
                        }
                        if ($scope.userType === 'teammanager') {
                            $scope.canEmailList = true;
                        }
                        if ($scope.userType === 'associate') {
                            $scope.isAuthorised = false;
                            $scope.takeNoColumns = 4;
                        } else if ($scope.userType === 'eventleader') {
                            $scope.isAuthorised = true;
                            $scope.showFive = true;
                        } else {
                            $scope.showFive = true;
                        }
                        $scope.totalItems = $scope.allContactInfo.length;

                        $scope.currentPage = 1;
                        $scope.itemsPerPage = 100;
                        $scope.allFilteredContacts = $scope.allContactInfo;
                        $scope.allFilteredContacts.reverse();
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
                            if ($scope.totalPages === 0) {
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
                        if ($scope.totalPages === 0) {
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
                            if ($scope.totalPages === 0) {
                                $scope.totalPages = 1;
                            }
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.splitContacts = $scope.allFilteredContacts.slice(begin, end);
                            });
                        });
                        
//                        $scope.trial = function($event) {
//                            if($event.which == 3) {
//                                console.log("hello babe");
//                            }
//                        };
                        
                        $scope.viewContact = function ($event, contact) {
                            var toURL = $scope.userType + ".viewIndivContact";
                            $scope.viewIndivContact = $scope.userType + '/viewIndivContact';
                            var contactCid = contact.cid;
                            session.setSession('contactToDisplayCid', contactCid);
                            if($event.which == 1) {
                                $state.go(toURL);
                            }
                        };

                        $scope.editContact = function ($event, contact) {
                            var toURL = $scope.userType + ".editContact";
                            var contactCid = contact.cid;
                            session.setSession('contactToDisplayCid', contactCid);
                            session.setSession('otherContact', 'true');
                            $state.go(toURL);
                        };

                        $scope.deleteContact = function ($event, contact) {
                            var toURL = $scope.userType + ".viewContacts";
                            var contactCid = contact.cid;

                            var toDelete = {
                                'token': session.getSession('token'),
                                'contact_id': contactCid
                            }
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/deletePrompt.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            }).then(function (response) {
                                deleteService.deleteDataService(toDelete, '/contact.delete').then(function (response) {
                                    if (response.data.message === "success") {
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/deleteSuccess.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        }).then(function (response) {
                                            $state.reload(toURL);
                                        })
                                    } else {
                                        $scope.error = response.data.message;
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/deleteFailure.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        }).then(function (response) {
                                            $state.reload(toURL);
                                        });
                                    }
                                });
                            });
                        };

                        $scope.generateEmailList = function () {
                            $scope.emailList = response.data.emailList;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/emailList.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            })
                        };
                    });
                };

                $scope.predicate = '';
                $scope.reverse = false;

                $scope.order = function (predicate) {
                    $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
                    $scope.predicate = predicate;
                };

            }]);