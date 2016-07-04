//Created by Thao Nguyen

var app = angular.module('bahamas');

app.controller('loginController',
        ['$rootScope', '$scope', '$http', 'session', '$state', '$timeout', 'tokenUpdate', 'ngDialog', 'localStorageService',
            function ($rootScope, $scope, $http, session, $state, $timeout, tokenUpdate, ngDialog, localStorageService) {
                $scope.error = false;
//DEFINE AUTHORISED USER OBJECT
                var authorisedUser = {
                    'username': "",
                    'token': "",
                    'userType': "",
                    'contact': {},
                    'teams': []
                };
//DEFINE USER OBJECT
                $scope.user = {
                    'username': '',
                    'password': ''
                };
//LOGIN SERVICE
                $scope.loginUser = function () {
                    var url = $rootScope.commonUrl + "/login";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.user)
                    }).success(function (response) {
                        var returnedUser = response;
//                console.log(response);
                        if (returnedUser.message === "success") {
                            authorisedUser.username = $scope.user.username;
                            authorisedUser.token = returnedUser.token;
                            authorisedUser.userType = returnedUser['user_type'];
                            authorisedUser.contact = returnedUser.contact;
                            authorisedUser.teams = returnedUser.contact.teams;
//STORE AUTHORISED USER INFO IN SESSION SERVICE
                            if (authorisedUser.username !== "") {
//                        console.log(authorisedUser);
                                var storeUsername = session.setSession('username', authorisedUser.username);
                                var storeToken = session.setSession('token', authorisedUser.token);
                                var storeUserType = session.setSession('userType', authorisedUser.userType);
                                var storeContact = session.setSession('contact', angular.toJson(authorisedUser.contact));
                                var storeTeams = session.setSession('teams', angular.toJson(authorisedUser.teams));
                                var getUserType = session.getSession("userType");
                                
                                tokenUpdate.refreshToken(authorisedUser.token);

                                $state.go(getUserType.toString());

                            }
                        } else {
                            $scope.error = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };
            }]);



