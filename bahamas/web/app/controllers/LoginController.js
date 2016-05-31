//Created by Thao Nguyen

var app = angular.module('bahamas');

app.controller('loginController', ['$scope', '$http', '$location', 'session', '$window', function ($scope, $http, $location, session, $window) {
    $scope.error = false;
    var authorisedUser = {
        'username': "",
        'token': "",
        'userType': ""
    };
    $scope.user = {
        'username': '',
        'password': ''
    };
   
    $scope.loginUser = function () {
        $scope.location = $location.path();
        var url = location.origin + "/bahamas/login?";

        $http({
            method: 'POST',
            url: url,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            params: {
                'username': $scope.user.username,
                'password': $scope.user.password
            }
        }).success(function (response) {
            var returnedUser = response;
            if (returnedUser.status === "success") { 
                authorisedUser.username = $scope.user.username;
                authorisedUser.token = returnedUser.token;
                authorisedUser.userType = returnedUser.userType;
                if (authorisedUser.username !== "") {

                    var storeUsername = session.setSession('username', authorisedUser.username);
                    var storeToken = session.setSession('token', authorisedUser.token);
                    var storeUserType = session.setSession('userType', authorisedUser.userType);
                    console.log(storeUsername + " " + storeToken + " " + storeUserType);
                    
                    var getUsername = session.getSession("username");
                    var getToken = session.getSession("token");
                    var getUserType = session.getSession("userType");
                    
                    $window.location.href = location.origin + "/bahamas/app/views/"+getUserType.toString()+".html";
                }   
            } else {
                $scope.error = true;
            }
        }).error(function () {
            window.alert("Fail to send request!");
        });
    };     
}]);



