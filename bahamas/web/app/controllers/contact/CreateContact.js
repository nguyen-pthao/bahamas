/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Created by Marcus Ong

var app = angular.module('bahamas');

app.controller('createContact', ['$scope', '$http', '$location', 'session', '$window', '$state', function ($scope, $http, $location, session, $window, $state) {

        $scope.loadContactTypeList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/contacttypelist";
            $http.get(url).then(function (response) {
                $scope.contactTypeList = response.data.contact;
            });
        };


        $scope.contactInfo = {
            'token': session.getSession("token"),
            'name': '',
            'altname': '',
            'username': '',
            'password': '',
            'contacttype': '',
            'explainifother': '',
            'profession': '',
            'jobtitle': '',
            'nricfin': '',
            'gender': '',
            'nationality': '',
            'dateofbirth': '',
            'remarks': '',
            'countrycode': '65',
            'phonenumber': '',
            'email': '',
            'country': '',
            'zipcode': '',
            'address': '',
        };

        $scope.submitContactInfo = function () {
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!
//          var url = "http://rms.twc2.org.sg/bahamas/contact.add";
            var url = "http://localhost:8084/bahamas/contact.add";
            console.log($scope.contactInfo);
            $http({
                method: 'POST',
                url: url,
                headers: {'Content-Type': 'application/json'},
                data: JSON.stringify($scope.contactInfo)
            }).success(function (response) {
                var seeResponse = response;
//                if(seeResponse === "success"){
                    console.log(response.message);
//                }
            }).error(function () {
                window.alert("Fail to send request!");
            });
        };
    }]);




