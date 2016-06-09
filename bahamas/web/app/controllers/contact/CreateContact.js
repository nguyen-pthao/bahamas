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

        $scope.loadCountryNames = function () {
            var url = "https://restcountries.eu/rest/v1/all";
            $http.get(url).then(function (response) {
                $scope.countryNames = response.data;
            });
        };

        $scope.loadCountryCodes = function () {
            var url = "https://restcountries.eu/rest/v1/all";
            var x = "";
            $http.get(url).then(function (response) {
                $scope.countryCodes = response.data;
           
                $scope.updateCountryNames = function (code) {
                    x = " ";
                    angular.forEach($scope.countryCodes, function (countryObj) {
                        angular.forEach(countryObj.callingCodes, function (value, key) {
                            if (value == code && value !== "") {
//                                $scope.newCountryName = countryObj.name;
                                   x = x + " " + countryObj.name;
                            }
                        });
                    });
                    $scope.newCountryName = x;
  
                  
                };
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




