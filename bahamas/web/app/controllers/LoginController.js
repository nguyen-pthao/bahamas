//Created by Thao Nguyen

var app = angular.module('bahamas');

app.controller('loginController', ['$scope', '$http', '$location', 'session', '$window', '$state', function ($scope, $http, $location, session, $window, $state) {
        $scope.error = false;
        var authorisedUser = {
            'username': "",
            'token': "",
            'userType': "",
            'contact': {},
            'teams': []
        };
        $scope.user = {
            'username': '',
            'password': ''
        };

        $scope.loginUser = function () {
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!
//      var url = "http://rms.twc2.org.sg/bahamas/login";
            var url = "http://localhost:8084/bahamas/login";

            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/json'},
                data: JSON.stringify($scope.user)
            }).success(function (response) {

                var returnedUser = response;
                if (returnedUser.message === "success") {
                    authorisedUser.username = $scope.user.username;
                    authorisedUser.token = returnedUser.token;
                    authorisedUser.userType = returnedUser.userType;
                    authorisedUser.contact = returnedUser.contact;
                    authorisedUser.teams = returnedUser.contact.teams;

                    if (authorisedUser.username !== "") {
                        console.log(authorisedUser);
                        var storeUsername = session.setSession('username', authorisedUser.username);
                        var storeToken = session.setSession('token', authorisedUser.token);
                        var storeUserType = session.setSession('userType', authorisedUser.userType);
                        var storeContact = session.setSession('contact', angular.toJson(authorisedUser.contact));
                        var storeTeams = session.setSession('teams', angular.toJson(authorisedUser.teams));
                        var getUserType = session.getSession("userType");
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



