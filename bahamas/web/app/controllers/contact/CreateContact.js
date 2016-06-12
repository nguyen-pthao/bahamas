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
        link: function (scope, elem, attrs, ngModel) {
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

app.controller('createContact',
        ['$scope', '$http', '$location', '$state', 'session', 'loadCountries', 'loadContactType', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage',
            function ($scope, $http, $location, $state, session, loadCountries, loadContactType, loadTeamAffiliation, loadPermissionLevel, loadLanguage) {

                $scope.backHome = function () {
                      $state.go('admin.homepage');
                };

                $scope.addContact = function () {
                    $location.path('/addContact');
                };

                $scope.loadContactTypeList = function () {
                    loadContactType.retrieveContactType().then(function (response) {
                        $scope.contactTypeList = response.data.contact;
                        console.log($scope.contactTypeList);
                    });
                };

                $scope.loadTeamAffiliationList = function () {
                    loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                        $scope.teamAffiliationList = response.data.teamAffiliationList;
                        //console.log($scope.teamAffiliationList);
                    });
                };

                $scope.loadPermissionLevelList = function () {
                    loadPermissionLevel.retrievePermissionLevel().then(function (response) {
                        $scope.permissionLevelList = response.data.permissionLevelList;
                    });
                };

                $scope.loadLanguageList = function () {
                    loadLanguage.retrieveLanguage().then(function (response) {
                        $scope.languageList = response.data.languageList;
                    });
                };

                $scope.loadCountryNames = function () {
                    loadCountries.retrieveCountries().then(function (response) {
                        $scope.countryNames = response.data;
                    });
                };

                $scope.loadCountryCodes = function () {
                    var x = "";
                    loadCountries.retrieveCountries().then(function (response) {
                        $scope.countryCodes = response.data;

                        $scope.updateCountryNames = function (code) {
                            x = " ";
                            code += "";
                            angular.forEach($scope.countryCodes, function (countryObj) {
                                angular.forEach(countryObj.callingCodes, function (value, key) {
                                    if (value == code && value !== "") {
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
                $scope.emailRegex = '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}';

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
                    'countrycode': 65,
                    'phonenumber': '',
                    'email': '',
                    'country': '',
                    'zipcode': '',
                    'address': ''
                };

                $scope.result = {
                    message: true,
                    deliver: '',
                    contactId: ''
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
                        console.log(response.message);
                        //TO BE MODIFIED
                        if (response.message == 'success') {
                            $scope.result.message = true;
                            $scope.result.deliver = 'Thank you very much for your time.<br>Would you like to add additional information to your contact?';
                            $scope.result.contactId = response.id;
                        } else {
                            $scope.result.message = false;
                            $scope.result.deliver = 'It seems that there is some error in your form.We would be much appreciated if you could spend time checking through all the data again.';
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.additionalContactInfo = {
                    phoneInfo: {
                        token: session.getSession("token"),
                        id: $scope.result.contactId,
                        countrycode: 65,
                        phonenumber: '',
                        phoneremarks: '',
                        dateobsolete: ''
                    },
                    emailInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        email: '',
                        emailremarks: '',
                        dateobsolete: ''
                    },
                    addressInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        address: '',
                        country: '',
                        zipcode: '',
                        addressremarks: '',
                        dateobsolete: ''
                    },
                    languageInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        language: '',
                        explainifother: '',
                        speakwrite: '',
                        remarks: '',
                        dateobsolete: ''
                    },
                    skillassetInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        skillasset: '',
                        explainifother: '',
                        remarks: '',
                        dateobsolete: ''
                    },
                    teamInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        contactname: $scope.contactInfo.name,
                        team: '',
                        explainifother: '',
                        subteam: '',
                        permissionlevel: '',
                        remarks: '',
                        dateobsolete: ''
                    }
                };




            }]);




