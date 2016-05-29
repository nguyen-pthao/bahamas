//Created by Thao Nguyen

var app = angular.module('bahamasLogin', []);
//PLEASE DELETE COMMENTS WHEN YOU'RE DONE
//
//There're multiple ways of declaring a module by either assigning it to a variable or just call it directly
//When a module is already declared, please remove [] in the argument.
//any built-in variables such as $scope, $http, $location are considered dependency to be used by the controller
//and thus need to be injected in controller declaration.
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

//It would be a better practice to separate this method in a login service but forgive my lack of knowledge, I just insert it in here.    
    $scope.loginUser = function () {
//in Angular controller, only object in $scope would be shown,
//so need to retrieve global $scope.location object by $location.path first. It's fine to store it as a var 
//location.origin(or $scope.location) returns http://localhost:8080, we need to add /application_name/location 
//there're other choices such as hash, search,... 
        $scope.location = $location.path();
        var url = location.origin + "/bahamas/login?";

        $http({
            method: 'POST',
            url: url,
//Since backend are receiving request input directly instead of json object, content-type is set to application/x-www-form-urlencoded
//In case of JSON data transfer, please change to '/application/json'
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            params: {
                'username': $scope.user.username,
                'password': $scope.user.password
            }
//in fact, it's fine to not define user object but instead $scope.username object and $scope.password object.
        }).success(function (response) {
//the response is already a valid JSON object so there's no need to use JSON.parse(string json) anymore. 
//Please note that any out.println() from the servlet will result in console.log in frontend and wrong JSON parsing.
            var returnedUser = response;
            if (returnedUser.status === "success") { //using == or === is fine since === refers implies strictly equal object but == is equal object's value
                authorisedUser.username = $scope.user.username;
                authorisedUser.token = returnedUser.token;
                authorisedUser.userType = returnedUser.userType;
                if (authorisedUser.username !== "") {
//assigning variable to session.setSession is meant for testing purpose only
                    var storeUsername = session.setSession('username', authorisedUser.username);
                    var storeToken = session.setSession('token', authorisedUser.token);
                    var storeUserType = session.setSession('userType', authorisedUser.userType);
                    console.log(storeUsername + " " + storeToken + " " + storeUserType);
                    
//this is just for testing, feel free to delete.
                    var getUsername = session.getSession("username");
                    var getToken = session.getSession("token");
                    var getUserType = session.getSession("userType");
                    $window.location.href = location.origin + '/bahamas/app/views/' + getUserType.toString() + '.html';
                }
            } else {
                $scope.error = true;
            }
        }).error(function () {
            window.alert("Fail to send request!");
        });
    };     
//sadly, $window storage key/value only stores string type, 
//so either we have to stringify the object or we have to store 3 parameters separately   

}]);

app.factory('session', ['$window', function ($window) {
    return {
        setSession: function(key, value) {
            try {
                if ($window.Storage) {
                    $window.sessionStorage.setItem(key, value);
                    return true;
                } else {
                    return false;
                }
            } catch (error) {
                window.alert(error, error.message);
            }
        }, 
        getSession: function (key) {
            try {
                if ($window.Storage) {
                    return $window.sessionStorage.getItem(key);
                } else {
                    return false;
                }
            } catch (error) {
                window.alert(error, error.message);
            }
        }
    };

}]);

