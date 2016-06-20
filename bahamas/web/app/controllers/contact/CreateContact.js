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
        ['$scope', '$http', '$location', '$state', 'session', 'loadCountries', 'loadContactType', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass',
            function ($scope, $http, $location, $state, session, loadCountries, loadContactType, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass) {

                var user = session.getSession('userType');
                var currentState = user+'.addContact';
                var homepage = user+'.homepage';
                
                $scope.form = {};
                
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
                    'countrycode': '65',
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
                    var url = $scope.commonUrl + "/contact.add";
                    console.log($scope.contactInfo);
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.contactInfo)
                    }).success(function (response) {
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
                
                $scope.copyCat = angular.copy($scope.additionalContactInfo);
                var copyService = function(copyData) {
                   $scope.additionalContactInfo[copyData] = angular.copy($scope.copyCat[copyData]);
                };
                
                $scope.message = '';
                $scope.submittedPhone = false;
                $scope.addPhone = function () {
                    var dataParse = 'phoneInfo';
                    $scope.additionalContactInfo[dataParse].id = $scope.result.contactId;
                    var url = $scope.commonUrl + "/phone.add";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo[dataParse])
                    }).success(function (response) {
                        if (response.message == 'success') {
                            $scope.submittedPhone = true;
                            $scope.message = 'Submitted successfully.';
                        } else {
                            $scope.message = 'There is error in the data, please check again.';
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedEmail = false;
                $scope.addEmail = function () {
                    var dataParse = 'emailInfo';
                    $scope.additionalContactInfo[dataParse].id = $scope.result.contactId;
                     var url = $scope.commonUrl + "/email.add";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo[dataParse])
                    }).success(function (response) {
                        if (response.message == 'success') {
                            $scope.submittedEmail = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedAddress = false;
                $scope.addAddress = function () {
                    var dataParse = 'addressInfo';
                    $scope.additionalContactInfo[dataParse].id = $scope.result.contactId;
                     var url = $scope.commonUrl + "/address.add";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo[dataParse])
                    }).success(function (response) {
                        if (response.message == 'success') {
                            $scope.submittedAddress = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedTeam = false;
                $scope.addTeam = function () {
                    var dataParse = 'teamInfo';
                    $scope.additionalContactInfo[dataParse].id = $scope.result.contactId;
                     var url = $scope.commonUrl + "/teamjoin.add";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo[dataParse])
                    }).success(function (response) {
                        if (response.message == 'success') {
                            $scope.submittedTeam = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedLanguage = false;
                $scope.addLanguage = function () {
                    var dataParse = 'languageInfo';
                    $scope.additionalContactInfo[dataParse].id = $scope.result.contactId;
                     var url = $scope.commonUrl + "/language.add";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo[dataParse])
                    }).success(function (response) {
                        if (response.message == 'success') {
                            $scope.submittedLanguage = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };

                $scope.submittedLSA = false;
                $scope.addSkillasset = function () {
                    var dataParse = 'skillassetInfo';
                    $scope.additionalContactInfo[dataParse].id = $scope.result.contactId;
                     var url = $scope.commonUrl + "/skill.add";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.additionalContactInfo[dataParse])
                    }).success(function (response) {
                        if (response.message == 'success') {
                            $scope.submittedLSA = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.addMorePhone = function() {
                    copyService('phoneInfo');
                    $scope.submittedPhone = false;
                    $scope.form.additionalContactForm.additionalphonenumber.$setPristine();
                };
                
                $scope.addMoreEmail = function() {
                    copyService('emailInfo');
                    $scope.submittedEmail = false;
                    $scope.form.additionalContactForm.additionalemail.$setPristine();
                };
                
                $scope.addMoreAddress = function() {
                    copyService('addressInfo');
                    $scope.submittedAddress = false;
                    $scope.form.additionalContactForm.additionaladdress.$setPristine();
                };
                
                $scope.addMoreTeam = function() {
                    copyService('teamInfo');
                    $scope.submittedTeam = false;
                };
                
                $scope.addMoreLanguage = function() {
                    copyService('languageInfo');
                     $scope.submittedLanguage = false;
                };
                
                $scope.addMoreLSA = function() {
                    copyService('skillassetInfo');
                    $scope.submittedLSA = false;
                };
            }]);




