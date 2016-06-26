/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Created by Marcus Ong

var app = angular.module('bahamas');

app.controller('createContact',
        ['$scope', '$http', '$state', 'session', 'loadCountries', 'loadContactType', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass',
            function ($scope, $http, $state, session, loadCountries, loadContactType, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass) {
//PAGES TRANSITION
                var user = session.getSession('userType');
                var currentState = user + '.addContact';
                var homepage = user + '.homepage';

                $scope.backHome = function () {
                    $state.go(homepage);
                };

                $scope.addContact = function () {
                    $state.reload(currentState);
                };

                $scope.form = {};
//CALL DROPDOWN LIST SERVICES        
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
//DEFINE REGEX
                $scope.nationalityRegex = '[A-Za-z ]{0,49}';
                $scope.nricRegex = '[STFG][0-9]\\d{6}[A-Z]'; //notice that \d won't work but \\d
                $scope.phoneRegex = '[0-9]\\d{0,19}';
                $scope.emailRegex = '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}';
//DECLARE CONTACT OBJECT              
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
//DECLARE RESULT OBJECT 
                $scope.result = {
                    message: false,
                    deliver: '',
                    contactId: ''
                };
//SUBMIT CONTACT 
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

//DECLARE ADDITIONAL CONTACT INFO OBJECT 
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
                        team1: '',
                        team2: '',
                        team3: ''
                    }
                };
//DEFINE TEAM LISTS
//watch for change in team list 1
                $scope.teamAffiliationList1 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
                $scope.$watch('additionalContactInfo.teamInfo.team1', function () {
                    if ($scope.additionalContactInfo.teamInfo.team1 !== '') {
                        var choice = $scope.additionalContactInfo.teamInfo.team1;
                        var position = -1;
                        for (var i in $scope.teamAffiliationList) {
                            var teamCheck = $scope.teamAffiliationList[i];
                            if (teamCheck.teamAffiliation == $scope.additionalContactInfo.teamInfo.team1) {
                                position = i;
                            }
                        }
                        if (position == -1) {
                            $scope.teamAffiliationList1 = angular.copy($scope.teamAffiliationList);
                        } else {
                            var list = angular.copy($scope.teamAffiliationList);
                            list.splice(position, 1);
                            $scope.teamAffiliationList1 = list;
                            if ($scope.additionalContactInfo.teamInfo.team3 != '' && choice == $scope.additionalContactInfo.teamInfo.team3) {
                                var list2 = angular.copy($scope.teamAffiliationList1);
                                list2.splice(position, 1);
                                $scope.teamAffiliationList2 = list2;
                            }
                        }
                    } else {
                        $scope.teamAffiliationList1 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
                        $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
                    }
                });
//watch for change in team list 2
                $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
                $scope.$watch('additionalContactInfo.teamInfo.team2', function () {
                    if ($scope.additionalContactInfo.teamInfo.team2 !== '') {
                        var position = -1;
                        for (var i in $scope.teamAffiliationList1) {
                            var teamCheck = $scope.teamAffiliationList1[i];
                            if (teamCheck.teamAffiliation == $scope.additionalContactInfo.teamInfo.team2) {
                                position = i;
                            }
                        }
                        if (position == -1) {
                            $scope.teamAffiliationList2 = angular.copy($scope.teamAffiliationList1);
                        } else {
                            var list = angular.copy($scope.teamAffiliationList1);
                            list.splice(position, 1);
                            $scope.teamAffiliationList2 = list;
                        }
                    }
                });
//DEFINE COPYCAT FOR RE-SUBMIT
                $scope.copyCat = angular.copy($scope.additionalContactInfo);
                var copyService = function (copyData) {
                    $scope.additionalContactInfo[copyData] = angular.copy($scope.copyCat[copyData]);
                };
//SUBMIT PHONE
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
//SUBMIT EMAIL
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
//SUBMIT ADDRESS
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
//SUBMIT TEAM PREFERENCE
                $scope.submittedTeam = false;
                $scope.addTeam = function () {
                    var dataParse = 'teamInfo';
                    $scope.additionalContactInfo[dataParse].id = $scope.result.contactId;
                    var dataSend = {};
                    dataSend.token = $scope.additionalContactInfo.teamInfo.token;
                    dataSend.id = $scope.additionalContactInfo.teamInfo.id;
                    dataSend.contactname = $scope.additionalContactInfo.teamInfo.contactname;
                    dataSend.team = $scope.additionalContactInfo.teamInfo.team1;
                    dataSend.explainifother = '';
                    dataSend.subteam = '';
                    dataSend.permissionlevel = '';
                    dataSend.remarks = '';
                    dataSend.dateobsolete = '';
                    var url = $scope.commonUrl + "/teamjoin.add";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify(dataSend)
                    }).success(function (response) {
                        if (response.message == 'success') {
                            if ($scope.additionalContactInfo.teamInfo.team2 != '') {
                                dataSend.team = $scope.additionalContactInfo.teamInfo.team2;
                                $http({
                                    method: 'POST',
                                    url: url,
                                    headers: {'Content-Type': 'application/json'},
                                    data: JSON.stringify(dataSend)
                                }).success(function (response) {
                                    if (response.message == 'success') {
                                        if ($scope.additionalContactInfo.teamInfo.team3 != '') {
                                            dataSend.team = $scope.additionalContactInfo.teamInfo.team3;
                                            $http({
                                                method: 'POST',
                                                url: url,
                                                headers: {'Content-Type': 'application/json'},
                                                data: JSON.stringify(dataSend)
                                            }).success(function (response) {
                                                if (response.message == 'success') {
                                                    $scope.submittedTeam = true;
                                                }
                                            }).error(function () {
                                                window.alert("Fail to send request!");
                                            });
                                        }
                                    }
                                }).error(function () {
                                    window.alert("Fail to send request!");
                                });
                            }
                        } else {
                            //to be modified
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };
//SUBMIT LANGUAGE
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
//SUBMIT SKILLS AND ASSETS
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
//REFRESH PHONE PAGE
                $scope.addMorePhone = function () {
                    copyService('phoneInfo');
                    $scope.submittedPhone = false;
                    $scope.form.additionalContactForm.additionalphonenumber.$setPristine();
                };
//REFRESH EMAIL PAGE
                $scope.addMoreEmail = function () {
                    copyService('emailInfo');
                    $scope.submittedEmail = false;
                    $scope.form.additionalContactForm.additionalemail.$setPristine();
                };
//REFRESH ADDRESS PAGE
                $scope.addMoreAddress = function () {
                    copyService('addressInfo');
                    $scope.submittedAddress = false;
                    $scope.form.additionalContactForm.additionaladdress.$setPristine();
                };
//REFRESH TEAM PAGE
                $scope.addMoreTeam = function () {
                    copyService('teamInfo');
                    $scope.submittedTeam = false;
                };
//REFRESH LANGUAGE PAGE
                $scope.addMoreLanguage = function () {
                    copyService('languageInfo');
                    $scope.submittedLanguage = false;
                };
//REFRESH SKILLS AND ASSETS PAGE
                $scope.addMoreLSA = function () {
                    copyService('skillassetInfo');
                    $scope.submittedLSA = false;
                };
//DATEPICKER               
                $scope.today = function () {
                    $scope.dt = new Date();
                };
                $scope.today();

                $scope.clear = function () {
                    $scope.dt = null;
                };

                $scope.inlineOptions = {
                    customClass: getDayClass,
                    minDate: new Date(),
                    showWeeks: true
                };

                $scope.dateOptions = {
                    formatYear: 'yy',
                    minDate: new Date(),
                    startingDay: 1
                };

                $scope.toggleMin = function () {
                    $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
                    $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
                };

                $scope.toggleMin();

                $scope.open1 = function () {
                    $scope.popup1.opened = true;
                };

                $scope.setDate = function (year, month, day) {
                    $scope.dt = new Date(year, month, day);
                };

                $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
                $scope.format = $scope.formats[0];
                $scope.altInputFormats = ['M!/d!/yyyy'];

                $scope.popup1 = {
                    opened: false
                };

                function getDayClass(data) {
                    var date = data.date,
                            mode = data.mode;
                    if (mode === 'day') {
                        var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

                        for (var i = 0; i < $scope.events.length; i++) {
                            var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

                            if (dayToCheck === currentDay) {
                                return $scope.events[i].status;
                            }
                        }
                    }

                    return '';
                }
            }]);




