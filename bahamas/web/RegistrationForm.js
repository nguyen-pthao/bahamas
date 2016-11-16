/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('registration');

app.controller('registrationController', ['$scope', 'session', '$state', 'dataSubmit', '$uibModal', '$timeout', 'ngDialog', 'loadLanguage', 'loadLSAClass', 'loadCountries', 'loadTeamAffiliation',
    function ($scope, session, $state, dataSubmit, $uibModal, $timeout, ngDialog, loadLanguage, loadLSAClass, loadCountries, loadTeamAffiliation) {
        
        $scope.dob = '';
        
        $scope.formData = {
            form_id: session.getSession('formId'),
            contact_id: '',
            name: '',
            profession: '',
            'job_title': '',
            'nric_fin': '',
            gender: '',
            nationality: '',
            'date_of_birth': '',
            remarks: '',
            language: '',
            speak_write: '',
            email: '',
            'country_code': 65,
            'phone_number': '',
            'team': '',
            'skill_asset': '',
            first: false,
            second: false,
            'request_user': true
        };

        $scope.languageInfo = {
            language1: '',
            'speak_write_1': '',
            language2: '',
            'speak_write_2': ''
        };
        $scope.skillInfo = {
            skill1: '',
            skill2: ''
        };
        $scope.teamPreference = {
            team1: '',
            team2: ''
        };

        //load country names and codes
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

        //load languages
        $scope.loadLanguageList = function () {
            loadLanguage.retrieveLanguage().then(function (response) {
                $scope.languageList = response.data.languageList;
                for (var obj in $scope.languageList) {
                    if ($scope.languageList[obj].language == 'Others' || $scope.languageList[obj].language == 'Other') {
                        $scope.languageList.splice(obj, 1);
                    }
                }
            });
        };

        //load skills
        $scope.loadLSAList = function () {
            loadLSAClass.retrieveLSAClass().then(function (response) {
                $scope.LSAList = response.data.lsaClassList;
                for (var obj in $scope.LSAList) {
                    if ($scope.LSAList[obj].lsaClass == 'Others' || $scope.LSAList[obj].lsaClass == 'Other') {
                        $scope.LSAList.splice(obj, 1);
                    }
                }
            });
        };

        //load team preferences
        $scope.loadTeamAffiliationList = function () {
            loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                $scope.teamAffiliationList = response.data.teamAffiliationList;
                for (var obj in $scope.teamAffiliationList) {
                    if ($scope.teamAffiliationList[obj].teamAffiliation == 'Others' || $scope.teamAffiliationList[obj].teamAffiliation == 'Other') {
                        $scope.teamAffiliationList.splice(obj, 1);
                    }
                }
            });
        };

        //define regex
        $scope.nationalityRegex = '[A-Za-z ]{0,20}';
        $scope.nricRegex = '[A-Za-z][0-9]\\d{6}[A-Za-z]'; //notice that \d won't work but \\d
        $scope.phoneRegex = '[0-9-]{0,20}';
        $scope.emailRegex = '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}';

        //DEFINE TEAM LISTS
        //watch for change in team list 1
        $scope.teamAffiliationList1 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
        $scope.$watch('formData.team', function () {
            if ($scope.formData.team !== '') {
                var choice = $scope.formData.team;
                var position = -1;
                for (var i in $scope.teamAffiliationList) {
                    var teamCheck = $scope.teamAffiliationList[i];
                    if (teamCheck.teamAffiliation == $scope.formData.team) {
                        position = i;
                    }
                }
                if (position == -1) {
                    $scope.teamAffiliationList1 = angular.copy($scope.teamAffiliationList);
                } else {
                    var list = angular.copy($scope.teamAffiliationList);
                    list.splice(position, 1);
                    $scope.teamAffiliationList1 = list;
                    //$scope.additionalContactInfo.teamInfo.team2 = '';
                    if (choice == $scope.teamPreference.team1) {
                        $scope.teamPreference.team1 = '';
                    }
                    if ($scope.teamPreference.team2 != '' && choice == $scope.teamPreference.team2) {
                        var list2 = angular.copy($scope.teamAffiliationList1);
                        list2.splice(position, 1);
                        $scope.teamAffiliationList2 = list2;
                    }
                }
            }
//            else {
//                $scope.teamAffiliationList1 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
//                $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
//            }
        });
        //watch for change in team list 2
        $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
        $scope.$watch('teamPreference.team1', function () {
            if ($scope.teamPreference.team1 !== '') {
                var position = -1;
                for (var i in $scope.teamAffiliationList1) {
                    var teamCheck = $scope.teamAffiliationList1[i];
                    if (teamCheck.teamAffiliation == $scope.teamPreference.team1) {
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
            } else {
                $scope.teamPreference.team2 = '';
                $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
            }
        });

        //DEFINE LANGUAGE LIST
        //watch for change in language list 1
        $scope.languageList1 = [{language: '-------------------------------------------------'}, {language: '[Please choose the above option first.]'}];
        $scope.$watch('formData.language', function () {
            if ($scope.formData.language !== '') {
                var choice = $scope.formData.language;
                var position = -1;
                for (var i in $scope.languageList) {
                    var languageCheck = $scope.languageList[i];
                    if (languageCheck.language == $scope.formData.language) {
                        position = i;
                    }
                }
                if (position == -1) {
                    $scope.languageList1 = angular.copy($scope.languageList);
                } else {
                    var list = angular.copy($scope.languageList);
                    list.splice(position, 1);
                    $scope.languageList1 = list;
                    if (choice == $scope.languageInfo.language1) {
                        $scope.languageInfo.language1 = '';
                    }
                    if ($scope.languageInfo.language2 != '' && choice == $scope.languageInfo.language2) {
                        var list2 = angular.copy($scope.languageList1);
                        list2.splice(position, 1);
                        $scope.languageList2 = list2;
                    }
                }
            }
//            else {
//                $scope.teamAffiliationList1 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
//                $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
//            }
        });
        //watch for change in language list 2
        $scope.languageList2 = [{language: '-------------------------------------------------'}, {language: '[Please choose the above option first.]'}];
        $scope.$watch('languageInfo.language1', function () {
            if ($scope.languageInfo.language1 !== '') {
                var position = -1;
                for (var i in $scope.languageList1) {
                    var languageCheck = $scope.languageList1[i];
                    if (languageCheck.language == $scope.languageInfo.language1) {
                        position = i;
                    }
                }
                if (position == -1) {
                    $scope.languageList2 = angular.copy($scope.languageList1);
                } else {
                    var list = angular.copy($scope.languageList1);
                    list.splice(position, 1);
                    $scope.languageList2 = list;
                }
            } else {
                $scope.languageInfo.language2 = '';
                $scope.languageList2 = [{language: '-------------------------------------------------'}, {language: '[Please choose the above option first.]'}];
            }
        });

        //DEFINE SKILLS&ASSETS LIST
        //watch for change in language list 1
        $scope.LSAList1 = [{lsaClass: '-------------------------------------------------'}, {lsaClass: '[Please choose the above option first.]'}];
        $scope.$watch('formData["skill_asset"]', function () {
            if ($scope.formData["skill_asset"] !== '') {
                var choice = $scope.formData["skill_asset"];
                var position = -1;
                for (var i in $scope.LSAList) {
                    var lsaCheck = $scope.LSAList[i];
                    if (lsaCheck.lsaClass == $scope.formData["skill_asset"]) {
                        position = i;
                    }
                }
                if (position == -1) {
                    $scope.LSAList1 = angular.copy($scope.LSAList);
                } else {
                    var list = angular.copy($scope.LSAList);
                    list.splice(position, 1);
                    $scope.LSAList1 = list;
                    if (choice == $scope.skillInfo.skill1) {
                        $scope.skillInfo.skill1 = '';
                    }
                    if ($scope.skillInfo.skill2 != '' && choice == $scope.skillInfo.skill2) {
                        var list2 = angular.copy($scope.LSAList1);
                        list2.splice(position, 1);
                        $scope.LSAList2 = list2;
                    }
                }
            }
//            else {
//                $scope.teamAffiliationList1 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
//                $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
//            }
        });
        //watch for change in language list 2
        $scope.LSAList2 = [{lsaClass: '-------------------------------------------------'}, {lsaClass: '[Please choose the above option first.]'}];
        $scope.$watch('skillInfo.skill1', function () {
            if ($scope.skillInfo.skill1 !== '') {
                var position = -1;
                for (var i in $scope.LSAList1) {
                    var lsaCheck = $scope.LSAList1[i];
                    if (lsaCheck.lsaClass == $scope.skillInfo.skill1) {
                        position = i;
                    }
                }
                if (position == -1) {
                    $scope.LSAList2 = angular.copy($scope.LSAList1);
                } else {
                    var list = angular.copy($scope.LSAList1);
                    list.splice(position, 1);
                    $scope.LSAList2 = list;
                }
            } else {
                $scope.skillInfo.skill2 = '';
                $scope.LSAList2 = [{lsaClass: '-------------------------------------------------'}, {lsaClass: '[Please choose the above option first.]'}];
            }
        });

        $scope.$watch('dob', function () {
            if (angular.isUndefined($scope.dob)) {
                $scope.formData['date_of_birth'] = "";
            } else if ($scope.dob === "") {
                $scope.formData['date_of_birth'] = "";
            } else if ($scope.dob === null) {
                $scope.formData['date_of_birth'] = "";
            } else {
                var day = $scope.dob.getDate() + "";
                var month = ($scope.dob.getMonth() + 1) + "";
                var year = $scope.dob.getFullYear() + "";
                if (day.length < 2) {
                    day = '0' + day;
                }
                if (month.length < 2) {
                    month = '0' + month;
                }
                var ds = day + '/' + month + '/' + year;
                $scope.formData['date_of_birth'] = ds;
            }
        });

        //datepicker
        $scope.today = function () {
            $scope.dt = new Date();
        };

        $scope.clear = function () {
            $scope.dt = null;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            formatMonth: 'MMM',
            formatDay: 'dd',
            maxDate: new Date(),
            startingDay: 1
        };

        $scope.open = function () {
            $timeout(function () {
                $scope.opened = true;
            });
        };

        $scope.format = 'dd MMM yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];

        $scope.processForm = function () {
            
            var contactData = {
                'token': session.getSession("remoteToken"),
                'user_type': '',
                'name': $scope.formData.name,
                'alt_name': '',
                'contact_type': 'Individual',
                'explain_if_other': '',
                'profession': $scope.formData.profession,
                'job_title': $scope.formData['job_title'],
                'nric_fin': $scope.formData['nric_fin'],
                'gender': $scope.formData.gender,
                'nationality': $scope.formData.nationality,
                'date_of_birth': $scope.formData['date_of_birth'],
                'remarks': $scope.formData.remarks,
                'notification': false
            };
            
            var contact_url = "/contact.add";
            dataSubmit.submitData(contactData, contact_url).then(function (response) {
                if (response.data.message == 'success') {
                    var contactId = response.data['contact_id'];

                    var phoneData = {
                        token: session.getSession("remoteToken"),
                        'contact_id': contactId,
                        'user_type': '',
                        'country_code': $scope.formData['country_code'],
                        'phone_number': $scope.formData['phone_number'],
                        'phone_remarks': '',
                        'date_obsolete': ''
                    };

                    var emailData = {
                        token: session.getSession("remoteToken"),
                        'contact_id': contactId,
                        'user_type': '',
                        email: $scope.formData.email,
                        'email_remarks': '',
                        'date_obsolete': ''
                    };

                    var languageData = {
                        token: session.getSession("remoteToken"),
                        'contact_id': contactId,
                        'user_type': '',
                        language: $scope.formData.language,
                        'explain_if_other': '',
                        'speak_write': $scope.formData['speak_write'],
                        remarks: '',
                        'date_obsolete': ''
                    };

                    var skillData = {
                        token: session.getSession("remoteToken"),
                        'contact_id': contactId,
                        'user_type': '',
                        'skill_asset': $scope.formData['skill_asset'],
                        'explain_if_other': '',
                        remarks: '',
                        'date_obsolete': ''
                    };

                    var teamData = {
                        token: session.getSession("remoteToken"),
                        'contact_id': contactId,
                        'user_type': '',
                        'contact_name': $scope.formData.name,
                        team: $scope.formData.team,
                        'explain_if_other': '',
                        subteam: '',
                        'permission_level': '',
                        remarks: '',
                        'date_obsolete': '',
                    };

                    var phone_url = "/phone.add";
                    //var submittedPhone = false;
                    dataSubmit.submitData(phoneData, phone_url).then(function (response) {
                        if (response.data.message == 'success') {
                            //submittedPhone = true;
                        } else {
                            $scope.errorMessages = response.data.message;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });

                    var email_url = "/email.add";
                    //var submittedEmail = false;
                    dataSubmit.submitData(emailData, email_url).then(function (response) {
                        if (response.data.message == 'success') {
                            //submittedEmail = true;
                        } else {
                            $scope.errorMessages = response.data.message;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });

                    var language_url = "/language.add";
                    //var submittedLanguage = false;
                    dataSubmit.submitData(languageData, language_url).then(function (response) {
                        if (response.data.message == 'success') {
                            
                            if($scope.languageInfo.language1 != '') {
                                languageData.language = $scope.languageInfo.language1;
                                languageData.speak_write = $scope.languageInfo.speak_write_1;
                                dataSubmit.submitData(languageData, language_url).then(function (response) {
                                    if (response.data.message == 'success') {
                                        
                                        if($scope.languageInfo.language2 != '') {
                                            languageData.language = $scope.languageInfo.language2;
                                            languageData.speak_write = $scope.languageInfo.speak_write_2;
                                            dataSubmit.submitData(languageData, language_url).then(function (response) {
                                                if (response.data.message == 'success') {
                                                    //var submittedLanguage = true;
                                                } else {
                                                    $scope.errorMessages = response.data.message;
                                                    ngDialog.openConfirm({
                                                        template: './style/ngTemplate/errorMessage.html',
                                                        className: 'ngdialog-theme-default',
                                                        scope: $scope
                                                    });
                                                }
                                            }, function() {
                                                window.alert("Fail to send request!");
                                            });
                                        }
                                    } else {
                                        $scope.errorMessages = response.data.message;
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/errorMessage.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        });
                                    }
                                }, function() {
                                    window.alert("Fail to send request!");
                                });
                            }
                        } else {
                            $scope.errorMessages = response.data.message;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });

                    var skill_url = "/skill.add";
                    //var submittedLSA = false;
                    dataSubmit.submitData(skillData, skill_url).then(function (response) {
                        if (response.data.message == 'success') {
                            
                            if($scope.skillInfo.skill1 != '') {
                                skillData.skill_asset = $scope.skillInfo.skill1;
                                
                                dataSubmit.submitData(skillData, skill_url).then(function (response) {
                                    if (response.data.message == 'success') {
                                        if($scope.skillInfo.skill2 != '') {
                                            skillData.skill_asset = $scope.skillInfo.skill2;
                                            dataSubmit.submitData(skillData, skill_url).then(function (response) {
                                                if (response.data.message == 'success') {
                                                    //submittedLSA = true;
                                                } else {
                                                    $scope.errorMessages = response.data.message;
                                                    ngDialog.openConfirm({
                                                        template: './style/ngTemplate/errorMessage.html',
                                                        className: 'ngdialog-theme-default',
                                                        scope: $scope
                                                    });
                                                }
                                            }, function() {
                                                window.alert("Fail to send request!");
                                            });
                                        }
                                    } else {
                                        $scope.errorMessages = response.data.message;
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/errorMessage.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        });
                                    }
                                }, function() {
                                    window.alert("Fail to send request!");
                                });
                            }
                        } else {
                            $scope.errorMessages = response.data.message;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });

                    var team_url = "/teamjoin.add";
                    var submittedTeam = false;
                    dataSubmit.submitData(teamData, team_url).then(function (response) {
                        if (response.data.message == 'success') {
                            //submittedTeam = true;
                            if($scope.teamPreference.team1 != '') {
                                
                                teamData = $scope.teamPreference.team1;
                                dataSubmit.submitData(teamData, team_url).then(function (response) {
                                    if (response.data.message == 'success') {
                                        if($scope.teamPreference.team2 != '') { 
                                            teamData = $scope.teamPreference.team1;
                                            dataSubmit.submitData(teamData, team_url).then(function (response) {
                                                if (response.data.message == 'success') {
                                                    //submittedTeam = true;
                                                } else {
                                                    $scope.errorMessages = response.data.message;
                                                    ngDialog.openConfirm({
                                                        template: './style/ngTemplate/errorMessage.html',
                                                        className: 'ngdialog-theme-default',
                                                        scope: $scope
                                                    });
                                                }
                                            }, function() {
                                                window.alert("Fail to send request!");
                                            });
                                        }
                                    } else {
                                        $scope.errorMessages = response.data.message;
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/errorMessage.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        });
                                    }
                                }, function() {
                                    window.alert("Fail to send request!");
                                });          
                            }
                        } else {
                            $scope.errorMessages = response.data.message;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                    
                    //if (submittedPhone && submittedEmail && submittedLanguage && submittedLSA && submittedTeam) {
                    var register_url = '/remoteregistration.add';
                    $scope.formData.contact_id = contactId;
                    dataSubmit.submitData($scope.formData, register_url).then(function (response) {
                        if (response.data.message == 'success') {
                            window.alert("success");
                            $state.go('register');
                        } else {
                            $scope.errorMessages = response.data.message;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                }
            }, function (response) {
                window.alert("Fail to send request!");
            });
        };
    }]);
