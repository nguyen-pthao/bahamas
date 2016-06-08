//Created by Thao Nguyen

var app = angular.module('bahamas');

app.controller('loginController', ['$scope', '$http', '$location', 'session', '$window', '$state', 'authorization', function ($scope, $http, $location, session, $window, $state, authorization) {
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
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!
        var url = "http://localhost:8084/bahamas/login";
        
        $http({
            method: 'POST',
            url: url,
            headers: {'Content-Type': 'application/json'},
            data: JSON.stringify($scope.user)
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
                    
                    var getUsername = session.getSession("username");
                    var getToken = session.getSession("token");
                    var getUserType = session.getSession("userType");
                    
                    if(authorisedUser.userType ==="admin"){
                        authorization.setAdmin(getUserType.toString());
                    }
                    //$state.go(getUserType.toString());
                }   
            } else {
                $scope.error = true;
            }
        }).error(function () {
            window.alert("Fail to send request!");
        });
    };     
}]);



