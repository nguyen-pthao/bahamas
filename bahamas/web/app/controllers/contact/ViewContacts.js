/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

                    var userType = session.getSession('userType');
                    if (userType === 'admin') {
                        $scope.canSeePhone = true;
                        $scope.canDelete = true;
                        $scope.canReport = true;
                        $scope.canEmailList = true;
                    } else if (userType === 'teammanager') {
                        $scope.canSeePhone = true;
                        $scope.canDelete = false;
                        $scope.canReport = true;
                        $scope.canEmailList = true;
                    } else if (userType === 'eventleader') {
                        $scope.canSeePhone = true;
                        $scope.canDelete = false;
                        $scope.canReport = false;
                        $scope.canEmailList = false;
                    } else if (userType === 'associate') {
                        $scope.canSeePhone = false;
                        $scope.canDelete = false;
                        $scope.canReport = false;
                        $scope.canEmailList = false;
                    };
                    //for headers
                    //var allContactObjKey = [];
                    $scope.myPromise = loadAllContacts.retrieveAllContacts(contactToRetrieve).then(function (response) {
                        $scope.allContactInfo = response.data.contact;
                        $scope.filteredContacts = $scope.allContactInfo;
                        $scope.totalItems = $scope.filteredContacts.length;
                        $scope.currentPage = 0;
                        $scope.currentPageIncrement = 1;
                        $scope.$watch('currentPageIncrement', function () {
                            $scope.currentPage = $scope.currentPageIncrement - 1;
                        });
                        $scope.itemsPerPage = 100;
                        $scope.maxSize = 5;
                        $scope.propertyName = '';
                        $scope.reverse = true;
                        $scope.sortBy = function (propertyName) {
                            $scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse : false;
                            $scope.propertyName = propertyName;
                        };
                        $scope.totalPages = function () {
                            return Math.ceil($scope.filteredContacts.length / $scope.itemsPerPage);
                        };
                        $scope.itemsPerPageChanged = function () {
                            if ($scope.itemsPerPage == 'toAll') {
                                $scope.itemsPerPage = $scope.filteredContacts.length;
                                $scope.isAll = true;
                            } else {
                                $scope.isAll = false;
                            }
                        };
                        $scope.$watch('searchContacts', function (term) {
                            $scope.allContactInfoTemp = $scope.allContactInfo;
                            $scope.filteredContacts = filterFilter($scope.allContactInfoTemp, term);
                            $scope.totalItems = $scope.filteredContacts.length;
                        });


                        $scope.viewContact = function ($event, contact) {
                            var toURL = $scope.userType + ".viewIndivContact";
                            $scope.viewIndivContact = $scope.userType + '/viewIndivContact';
                            var contactCid = contact.cid;
                            session.setSession('contactToDisplayCid', contactCid);
                            if ($event.which == 1) {
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

                        $scope.generateReport = function ($event, contact, selection) {
                            session.setSession('contactReport', contact.cid);
                            session.setSession('reportSelection', selection);
                            session.setSession('contactName', contact.name);
                            session.setSession('contactToDisplayCid', contact.cid);

                            var toURL = $scope.userType + '.individualReport';
                            $scope.viewReport = $scope.userType + '/individualReport';

                            if ($event.which == 1) {
                                $state.go(toURL);
                            }
                        };

                        $scope.deleteContact = function ($event, contact) {
                            var toURL = $scope.userType + ".viewContacts";
                            var contactCid = contact.cid;

                            var toDelete = {
                                'token': session.getSession('token'),
                                'contact_id': contactCid
                            };
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
                                        });
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
                            });
                        };
                    });
                };
            }]);

app.filter('startFrom', function () {
    return function (input, start) {
        start = +start; //parse to int
        if (angular.isUndefined(input)) {
            return null;
        } else {
            return input.slice(start);
        }
    }
});