/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Created by Marcus Ong

var app = angular.module('bahamas');

app.directive('compare', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elem, attrs, ngModel) {
            if (!ngModel) {
                return;
            }
            
            scope.$watch(attrs.ngModel, function () {
                validate();
            });

            attrs.$observe('compare', function (val) {
                validate();
            });

            var validate = function () {
                var val1 = ngModel.$viewValue;
                var val2 = attrs.compare;
                ngModel.$setValidity('compare', !val1 || !val2 || val1 === val2);
            };
        }
    };
});

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
		
		$scope.nricRegex = '[STFG][0-9]\\d{6}[A-Z]';
        //notice that \d won't work but \\d
        $scope.phoneRegex = '[0-9]\\d{0,19}';
        $scope.emailRegex = '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$';
		
        $scope.contactInfo = {
            'token': session.getSession("token"),
            'name': '',
            'altname': '',
            'username': '',
            'password': '',
			'confirmpassword': '',
            'contacttype': 'Individual',
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




