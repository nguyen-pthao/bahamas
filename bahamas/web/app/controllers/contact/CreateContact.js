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
        ['$rootScope', '$scope', '$http', '$location', '$state', 'session', 'loadCountries', 'loadContactType', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass',
            function ($rootScope, $scope, $http, $location, $state, session, loadCountries, loadContactType, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass) {

                var user = session.getSession('userType');
                var currentState = user+'.addContact';
                var homepage = user+'.homepage';
                
                $scope.backHome = function () {
                    $state.go(homepage);
                };

                $scope.addContact = function () {
                    $state.reload(currentState);
                };

                $scope.loadContactTypeList = function () {
                    loadContactType.retrieveContactType().then(function (response) {
                        $scope.contactTypeList = response.data.contact;
                    });
                };

                $scope.loadTeamAffiliationList = function () {
                    loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                        $scope.teamAffiliationList = response.data.teamAffiliationList;
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

                $scope.loadLSAList = function () {
                    loadLSAClass.retrieveLSAClass().then(function (response) {
                        $scope.LSAList = response.data.lsaClassList;
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
                $scope.nameRegex = '[A-Za-z ]{0,49}';
                $scope.professionRegex = '[A-Za-z ]{0,49}';
                $scope.jobtitleRegex = '[A-Za-z ]{0,49}';
                $scope.nationalityRegex = '[A-Za-z ]{0,49}';
                $scope.nricRegex = '[STFG][0-9]\\d{6}[A-Z]'; //notice that \d won't work but \\d
                $scope.phoneRegex = '[0-9]\\d{0,19}';
                $scope.emailRegex = '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}';
                $scope.zipcodeRegex = '[0-9]\\d{0,19}';

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
                    'country': 'Singapore',
                    'zipcode': '',
                    'address': ''
                };

                $scope.result = {
                    message: false,
                    deliver: '',
                    contactId: ''
                };
                $scope.submitted = false;
                $scope.submitContactInfo = function () {
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!
                    var url = $rootScope.commonUrl + "/contact.add";
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
                            $scope.submitted = true;
                            $scope.result.message = true;
                            $scope.result.deliver = 'Thank you for your time. Would you like to add additional information to your contact?';
                            $scope.result.contactId = response.id;
//                            console.log($scope.result.contactId);
                        } else {
                            $scope.result.message = false;
                            $scope.result.deliver = 'It seems that there is error in your form. We would be much appreciated if you could spend time checking through all the data again.';
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

//                $scope.subteamRegex = '[A-Za-z ]{0,49}';

                $scope.additionalContactInfo = {
                    phoneInfo: {
                        token: session.getSession("token"),
                        id: $scope.result.contactId,
                        countrycode: 65,
                        phonenumber: '',
                        phoneremarks: '',
                        dateobsolete: '13-Jun-2020'
                    },
                    emailInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        email: '',
                        emailremarks: '',
                        dateobsolete: '13-Jun-2020'
                    },
                    addressInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        address: '',
                        country: '',
                        zipcode: '',
                        addressremarks: '',
                        dateobsolete: '13-Jun-2020'
                    },
                    languageInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        language: '',
                        explainifother: '',
                        speakwrite: '',
                        remarks: '',
                        dateobsolete: '13-Jun-2020'
                    },
                    skillassetInfo: {
                        token: session.getSession('token'),
                        id: $scope.result.contactId,
                        skillasset: '',
                        explainifother: '',
                        remarks: '',
                        dateobsolete: '13-Jun-2020'
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
                        dateobsolete: '13-Jun-2020'
                    }
                };
                $scope.submittedPhone = false;
                $scope.addPhone = function () {
                    $scope.additionalContactInfo.phoneInfo.id = $scope.result.contactId;
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!

                    var url = $rootScope.commonUrl + "/phone.update";
                    console.log($scope.additionalContactInfo.phoneInfo);
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo.phoneInfo)
                    }).success(function (response) {
                        console.log(response);
                        if (response.message == 'success') {
                            $scope.submittedPhone = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedEmail = false;
                $scope.addEmail = function () {
                    $scope.additionalContactInfo.emailInfo.id = $scope.result.contactId;
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!

                     var url = $rootScope.commonUrl + "/email.update";
                    console.log($scope.additionalContactInfo.emailInfo);
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo.emailInfo)
                    }).success(function (response) {
                        console.log(response);
                        if (response.message == 'success') {
                            $scope.submittedEmail = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedAddress = false;
                $scope.addAddress = function () {
                    $scope.additionalContactInfo.addressInfo.id = $scope.result.contactId;
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!
                     var url = $rootScope.commonUrl + "/address.update";
                    console.log($scope.additionalContactInfo.addressInfo);
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo.addressInfo)
                    }).success(function (response) {
                        console.log(response);
                        if (response.message == 'success') {
                            $scope.submittedAddress = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedTeam = false;
                $scope.addTeam = function () {
                    $scope.additionalContactInfo.teamInfo.id = $scope.result.contactId;
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!
                     var url = $rootScope.commonUrl + "/teamjoin.add";
                    console.log($scope.additionalContactInfo.teamInfo);
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo.teamInfo)
                    }).success(function (response) {
                        console.log(response);
                        if (response.message == 'success') {
                            $scope.submittedTeam = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedLanguage = false;
                $scope.addLanguage = function () {
                    $scope.additionalContactInfo.languageInfo.id = $scope.result.contactId;
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!
                     var url = $rootScope.commonUrl + "/language.update";
                    console.log($scope.additionalContactInfo.languageInfo);
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo.languageInfo)
                    }).success(function (response) {
                        console.log(response);
                        if (response.message == 'success') {
                            $scope.submittedLanguage = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedLSA = false;
                $scope.addSkillasset = function () {
                    $scope.additionalContactInfo.skillassetInfo.id = $scope.result.contactId;
//  REMEMBER TO CHANGE BEFORE DEPLOY!!!
                     var url = $rootScope.commonUrl + "/skill.update";
                    console.log($scope.additionalContactInfo.skillassetInfo);
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo.skillassetInfo)
                    }).success(function (response) {
                        console.log(response);
                        if (response.message == 'success') {
                            $scope.submittedLSA = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

            }]);




