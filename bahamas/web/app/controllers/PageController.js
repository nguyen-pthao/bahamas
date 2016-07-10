/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('pageController',
        ['$scope', 'session', '$state', 'ngDialog', 'loadAllContacts', 'localStorageService', '$http', '$timeout', '$rootScope',
            function ($scope, session, $state, ngDialog, loadAllContacts, localStorageService, $http, $timeout, $rootScope) {
                var user = session.getSession('userType');

                $scope.populatePage = function () {
                    $scope.name = '';
                    $scope.userType = '';
//            $scope.dateCreated = '';
                    if (session.getSession('username') != null) {
                        var contact = angular.fromJson(session.getSession('contact'));
                        $scope.username = session.getSession('username');
//                var dateToParse = contact['date_created'].substring(0, 10);
                        $scope.userType = session.getSession('userType');
                        $scope.name = angular.fromJson(session.getSession('contact')).name;
                    }

                    var dateS = new Date(localStorageService.get('dateStart'));
                    var dateE = new Date(localStorageService.get('dateEnd'));
                    var dateNow = new Date();
                    var timeDiff = Math.abs(dateE.getTime() - dateNow.getTime());
                    var tokenToSend = {
                        "token": session.getSession('token')
                    };
                    $scope.tokenTimer = $timeout(function () {
                        $scope.logoutTimer = $timeout(function () {
                            ngDialog.closeAll();
                            session.terminateSession();
                            $state.go('login');
                        }, 117000);
                        
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/refreshToken.html',
                            className: 'ngdialog-theme-default',
                            closeByDocument: false,
                            closeByEscape: false
                        }).then(function (response) {
                            return $http({
                                method: 'POST',
                                url: $rootScope.commonUrl + '/token.update',
                                data: JSON.stringify(tokenToSend)
                            }).then(function (response) {
                                var newToken = response.data.token;
                                session.setSession('token', newToken);
                                var dateStart = new Date();
                                var dateEnd = new Date();
                                dateEnd.setMinutes(dateStart.getMinutes() + 27);
                                localStorageService.set('dateStart', dateStart);
                                localStorageService.set('dateEnd', dateEnd);
                                $timeout.cancel($scope.logoutTimer);
                                $state.reload();
                            });
                        });

                    }, timeDiff);
                };

                $scope.logout = function () {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/logoutPrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        $timeout.cancel($scope.logoutTimer);
                        $timeout.cancel($scope.tokenTimer);
                        session.terminateSession();
                        localStorageService.clearAll();
                        $state.go('login');
                    });
                };

                $scope.retrieveAllContacts = function () {

                    if (session.getSession('teams') === 'undefined') {
                        var contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'teamname': "",
                            'permission': user
                        };
                    } else {
                        var contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'teamname': angular.fromJson(session.getSession('teams'))[0].teamname,
                            'permission': user
                        };
                    }

                    loadAllContacts.retrieveAllContacts(contactToRetrieve).then(function (response) {
                        $scope.allContactInfo = response.data.contact;
                        $scope.allContactInfoObj = angular.fromJson($scope.allContactInfo);
                        $scope.contactname = [];
                        angular.forEach($scope.allContactInfo, function (obj) {
                            $scope.contactname.push(obj.name);
                        });
                        $scope.userType = user;
                    });
                    
                    $scope.onSelect = function ($item, $model, $label) {
                        $scope.$item = $item;
                        $scope.$model = $model;
                        $scope.$label = $label;
                        $scope.searchContact();
                    };
                    
                    $scope.searchContact = function () {
                        var contactCid = $scope.selected.cid;
                        var toURL = $scope.userType + ".viewIndivContact";
                        session.setSession('contactToDisplayCid', contactCid);
                        $state.go(toURL, {}, {reload: true});
                    };

                    $scope.toProfile = function () {
                        var toURL = $scope.userType + ".userManagement";
                        $state.go(toURL);
                    };
                };

                $scope.openSidebar = false;
                $scope.openControlBar = function () {
                    $scope.openSidebar = !$scope.openSidebar;
                };
            }]);