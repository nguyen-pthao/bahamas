/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('userManagementCtrl', ['$scope', 'session', '$state', 'ngDialog', 'dataSubmit', function ($scope, session, $state, ngDialog, dataSubmit) {

    var user = session.getSession('userType');
    $scope.backHome = function () {
        $state.go(user);
    };    

    $scope.retrieveList = function(){
        $scope.toRetrieve = {
            "token": session.getSession('token'),
            "user_create_startdate": "",
            "user_create_enddate": "",
            "user_login_startdate": "",
            "user_login_enddate": ""
        };
        var url = '/usermanagement.retrieveusers';
        $scope.myPromise = dataSubmit.submitData($scope.toRetrieve, url).then(function (response) {
            console.log(response);
        });
        
    };

}]);