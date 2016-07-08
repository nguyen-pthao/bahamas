/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*Created by Marcus Ong*/

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

app.directive('dateparse',['$filter', function ($filter) {
    return {
        restrict: 'EAC',
        require: '?ngModel',
        link: function (scope, elem, attrs, ngModel) {
            ngModel.$parsers.push(function toModel(date) {
                var dateString = $filter('date')(date ,"dd-MMM-yyyy");
                return dateString;
                //return date.getDate() + ' ' + (date.getMonth() + 1) + ' ' +  date.getFullYear();
            });
        }
    };
}]);

app.controller('editContact',
        ['$scope', '$http', '$state', 'session', 'ngDialog', '$timeout',
            'loadCountries', 'loadContactType', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass', 'loadMembershipClass', 'loadPaymentMode', 'loadModeOfSendingReceipt', 'loadOfficeList', 
            'retrieveContactByCid', 'loadAllContacts',
            'dataSubmit', 'deleteService', 'retrieveOwnContactInfo', '$filter',
            function ($scope, $http, $state, session, ngDialog, $timeout,
                    loadCountries, loadContactType, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass, loadMembershipClass, loadPaymentMode, loadModeOfSendingReceipt, loadOfficeList, 
                    retrieveContactByCid, loadAllContacts,
                    dataSubmit, deleteService, retrieveOwnContactInfo, $filter) {

                var permission = session.getSession('userType');

                //viewing mode: false for own contact and true for other people contact
                $scope.editMode = session.getSession('otherContact');
//GET USER PERMISSION
                if ($scope.editMode == null) {
                    $scope.editMode = 'false';
                }

                $scope.authorised = false;
                if (permission != 'associate' && permission != 'novice') {
                    $scope.authorised = true;
                }

                //only admin is allowed to view username and to deactivate user
                $scope.isAdmin = false;
                $scope.isEventLeader = false;
                var contactToRetrieve = {};

                if ($scope.authorised && $scope.editMode == 'true') {
                    if (permission === 'admin') {
                        $scope.isAdmin = true;
                        $scope.isEventLeader = false;
                    } else if (permission === 'eventleader') {
                        $scope.isAdmin = false;
                        $scope.isEventLeader = true;
                    }

//CONTACT TO BE EDITTED       
                    if (session.getSession('teams') === 'undefined') {
                        contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'other_cid': session.getSession('contactToDisplayCid'),
                            'team_name': '',
                            'permission': permission
                        };
                    } else {
                        contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'other_cid': session.getSession('contactToDisplayCid'),
                            'team_name': angular.fromJson(session.getSession('teams'))[0].teamname,
                            'permission': permission
                        };
                    }
                } else {
                    contactToRetrieve = {
                        'token': session.getSession('token')
                    };
                }

//PAGES TRANSITION
                var toContact = '';
                if ($scope.authorised && $scope.editMode == 'true') {
                    toContact = permission + '.viewIndivContact';
                } else {
                    toContact = permission + '.userManagement';
                }
                var homepage = permission + '.homepage';
                var toContacts = permission + '.viewContacts';
                var toEditContact = permission + '.editContact';

                $scope.backHome = function () {
                    $state.go(homepage);
                };
                $scope.viewContact = function () {
                    $state.go(toContact);
                };
                $scope.viewAllContacts = function () {
                    $state.go(toContacts);
                };

//CALL DROPDOWN LIST SERVICES        
                $scope.loadContactTypeList = function () {
                    loadContactType.retrieveContactType().then(function (response) {
                        $scope.contactTypeList = response.data.contact;
                    });
                };
                $scope.loadMembershipList = function () {
                    loadMembershipClass.retrieveMembershipClass().then(function (response) {
                        $scope.membershipList = response.data.membershipClassList;
                    });
                };
                $scope.loadPaymentModeList = function () {
                    loadPaymentMode.retrievePaymentMode().then(function (response) {
                        $scope.paymentModeList = response.data.paymentModeList;
                    });
                };
                $scope.loadSendReceiptModeList = function () {
                    loadModeOfSendingReceipt.retrieveModeOfSendingReceipt().then(function (response) {
                        $scope.sendReceiptModList = response.data.mode;
                    });
                };
                $scope.loadOfficeHoldList = function () {
                    loadOfficeList.retrieveOfficeList().then(function (response) {
                        $scope.officeList = response.data.officeList;
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

//RETRIEVE CONTACT OBJECTS INITIAL
                var contactToEdit;
                $scope.retrieveFunc = function () {
                    if ($scope.authorised && $scope.editMode == 'true') {
                        $scope.myPromise = retrieveContactByCid.retrieveContact(contactToRetrieve).then(function (response) {
                            if (response.data.message == 'success') {
                                contactToEdit = response.data.contact[0];
//                                console.log(contactToEdit);
                                retrieveUserInfo(contactToEdit);
                                retrievePersonalInfo(contactToEdit);
                                retrievePhoneInfo(contactToEdit);
                                retrieveEmailInfo(contactToEdit);
                                retrieveAddressInfo(contactToEdit);
                                retrieveMembershipInfo(contactToEdit);
                                retrieveOfficeInfo(contactToEdit);
                                retrieveDonationInfo(contactToEdit);
                                retrieveTeamInfo(contactToEdit);
                                retrieveTrainingInfo(contactToEdit);
                                retrieveAppreciationInfo(contactToEdit);
                                retrieveProxyInfo(contactToEdit);
                                retrieveLanguagesInfo(contactToEdit);
                                retrieveSkillsInfo(contactToEdit);
                                
                                retrieveAllContacts();
                            } else {
                                return contactToEdit;
                                console.log("Wrong data passed");
                            }
                        }, function (response) {
                            window.alert('Fail to connect to server');
                        });
                    } else {
                        $scope.myPromise = retrieveOwnContactInfo.retrieveContact(contactToRetrieve).then(function (response) {
                            if (response.data.message == 'success') {
                                contactToEdit = response.data.contact;
                                retrieveUserInfo(contactToEdit);
                                retrievePersonalInfo(contactToEdit);
                                retrievePhoneInfo(contactToEdit);
                                retrieveEmailInfo(contactToEdit);
                                retrieveAddressInfo(contactToEdit);
                                retrieveMembershipInfo(contactToEdit);
                                retrieveOfficeInfo(contactToEdit);
                                retrieveDonationInfo(contactToEdit);
                                retrieveTeamInfo(contactToEdit);
                                retrieveTrainingInfo(contactToEdit);
                                retrieveAppreciationInfo(contactToEdit);
                                retrieveProxyInfo(contactToEdit);
                                retrieveLanguagesInfo(contactToEdit);
                                retrieveSkillsInfo(contactToEdit);
                                
                                retrieveAllContacts();
                            } else {
                                console.log("Wrong data passed");
                                return contactToEdit;

                            }
                        }, function (response) {
                            window.alert('Fail to connect to server');
                        });
                    }
                };
                
//RETRIEVE ALL CONTACT FOR PROXY
                var proxyToRetrieve = {};
                var selectedProxy = {};
                var retrieveAllContacts = function () {
                    if (session.getSession('teams') === 'undefined') {
                        proxyToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'teamname': "",
                            'permission': permission
                        };
                    } else {
                        proxyToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'teamname': angular.fromJson(session.getSession('teams'))[0].teamname,
                            'permission': permission
                        };
                    }

                    loadAllContacts.retrieveAllContacts(proxyToRetrieve).then(function (response) {
                        $scope.allContactInfo = response.data.contact;
                        $scope.allContactInfoObj = angular.fromJson($scope.allContactInfo);
                        $scope.contactname = [];
                        angular.forEach($scope.allContactInfo, function (obj) {
                            $scope.contactname.push(obj.name);
                        });
                        $scope.userType = permission;
                    });

                    $scope.searchContact = function () {
                        selectedProxy = $scope.selected;
                        console.log(selectedProxy);
                    };
                };
                
//DECLARE OBJECTS FOR EDIT CONTACT
                $scope.editUser = {
                    'username': ''
                };
                $scope.editContact = {};
                $scope.isUser = true;
                //user
                var retrieveUserInfo = function (contactToEdit) {
                    if ($scope.editMode == 'true') {
                        if ($scope.isAdmin) {
                            if (contactToEdit.username != '') {
                                $scope.editUser['username'] = contactToEdit.username;
                                $scope.editUser.deactivated = contactToEdit.deactivated;
                                $scope.editUser['is_admin'] = contactToEdit['is_admin'];
                            } else {
                                $scope.isUser = false;
                                $scope.editUser['username'] = '';
                                $scope.editUser.deactivated = false;
                                $scope.editUser['password'] = ''; //to be changed
                                $scope.editUser['email'] = '';
                            }
                        }
                    } else {
                        $scope.editUser['username'] = contactToEdit.username;
                        $scope.editUser['newPassword'] = '';
                        $scope.editUser['confirmPassword'] = '';
                    }
                };
                //contact
                var retrievePersonalInfo = function (contactToEdit) {
                    $scope.editContact['date_created'] = contactToEdit['date_created'];
                    $scope.editContact['created_by'] = contactToEdit['created_by'];
                    $scope.editContact['name'] = contactToEdit.name;
                    $scope.editContact['alt_name'] = contactToEdit['alt_name'];
                    $scope.editContact['contact_type'] = contactToEdit['contact_type'];
                    $scope.editContact['explain_if_other'] = contactToEdit['explain_if_other'];
                    $scope.editContact['profession'] = contactToEdit.profession;
                    $scope.editContact['job_title'] = contactToEdit['job_title'];
                    if ($scope.isAdmin || ($scope.editMode == 'false')) {
                        $scope.editContact['nric_fin'] = contactToEdit['nric_fin'];
                    } else {
                        $scope.editContact['nric_fin'] = '';
                    }
                    $scope.editContact.nationality = contactToEdit.nationality;
                    $scope.editContact['gender'] = contactToEdit.gender;
                    $scope.editContact['date_of_birth'] = new Date(contactToEdit['date_of_birth']);
                    $scope.editContact['remarks'] = contactToEdit.remarks;
                };
                //phone
                var retrievePhoneInfo = function (contactToEdit) {
                    if (contactToEdit.phone != '') {
                        $scope.editPhone = contactToEdit.phone;
                        for(var i = 0; i<contactToEdit.phone.length; i++){
                            $scope.editPhone[i]['date_obsolete'] = new Date(contactToEdit.phone[i]['date_obsolete']);
                        }
                    } else {
                        $scope.editPhone = '';
                    }
                };
                //email
                var retrieveEmailInfo = function (contactToEdit) {
                    if (contactToEdit.email != '') {
                        $scope.editEmail = contactToEdit.email;
                        $scope.emailList = [];
                        for(var i = 0; i < contactToEdit.email.length; i++){
                            $scope.editEmail[i]['date_obsolete'] = new Date(contactToEdit.email[i]['date_obsolete']);
                            $scope.emailList.push({'email' : $scope.editEmail[i]['email']});
                        }
                    } else {
                        $scope.editEmail = '';
                    }
                };
                //address
                var retrieveAddressInfo = function (contactToEdit) {
                    if (contactToEdit.address != '') {
                        $scope.editAddress = contactToEdit.address;
                        for(var i = 0; i<contactToEdit.address.length; i++){
                            $scope.editAddress[i]['date_obsolete'] = new Date(contactToEdit.address[i]['date_obsolete']);
                        }
                    } else {
                        $scope.editAddress = '';
                    }
                };
                //membership
                var retrieveMembershipInfo = function (contactToEdit) {
                    if (contactToEdit.membership != '') {
                        $scope.editMembership = contactToEdit.membership;
                        for(var i = 0; i<contactToEdit.membership.length; i++){
                            $scope.editMembership[i]['date_obsolete'] = new Date(contactToEdit.membership[i]['date_obsolete']);
                            $scope.editMembership[i]['start_date'] = new Date(contactToEdit.membership[i]['start_date']);
                            $scope.editMembership[i]['end_date'] = new Date(contactToEdit.membership[i]['end_date']);
                            $scope.editMembership[i]['receipt_date'] = new Date(contactToEdit.membership[i]['receipt_date']);
                        }
                    } else {
                        $scope.editMembership = '';
                    }
                };
                //office held
                var retrieveOfficeInfo = function (contactToEdit) {
                    if (contactToEdit['office_held'] != '') {
                        $scope.editOfficeHeld = contactToEdit['office_held'];
                        for(var i = 0; i<contactToEdit['office_held'].length; i++){
                            $scope.editOfficeHeld[i]['start_office'] = new Date(contactToEdit['office_held'][i]['start_office']);
                            $scope.editOfficeHeld[i]['end_office'] = new Date(contactToEdit['office_held'][i]['end_office']);
                        }
                    } else {
                        $scope.editOfficeHeld = '';
                    }
                };
                //donation
                var retrieveDonationInfo = function (contactToEdit) {
                    if (contactToEdit.donation != '') {
                        $scope.editDonation = contactToEdit.donation;
                        for(var i = 0; i<contactToEdit.donation.length; i++){
                            $scope.editDonation[i]['date_received'] = new Date(contactToEdit.donation[i]['date_received']);
                            $scope.editDonation[i]['receipt_date'] = new Date(contactToEdit.donation[i]['receipt_date']);
                        }
                    } else {
                        $scope.editDonation = '';
                    }
                };
                //team join
                var retrieveTeamInfo = function (contactToEdit) {
                    if (contactToEdit['team_join'] != '') {
                        $scope.editTeam = contactToEdit['team_join'];
                        for(var i = 0; i<contactToEdit['team_join'].length; i++){
                            $scope.editTeam[i]['date_obsolete'] = new Date(contactToEdit['team_join'][i]['date_obsolete']);
                        }
                    } else {
                        $scope.editTeam = '';
                    }
                };
                //training
                var retrieveTrainingInfo = function (contactToEdit) {
                    if (contactToEdit.hasOwnProperty('training')) {
                        if (contactToEdit.training != '') {
                            $scope.editTraining = contactToEdit.training;
                        } else {
                            $scope.editTraining = '';
                        }
                    } else {
                        $scope.editTraining = '';
                    }
                };
                //appreciation
                var retrieveAppreciationInfo = function (contactToEdit) {
                    if (contactToEdit.appreciation != '') {
                        $scope.editAppreciation = contactToEdit.appreciation;
                        for(var i = 0; i<contactToEdit.appreciation.length; i++){
                            $scope.editAppreciation[i]['appraisal_date'] = new Date(contactToEdit.appreciation[i]['appraisal_date']);
                            $scope.editAppreciation[i]['appreciation_date'] = new Date(contactToEdit.appreciation[i]['appreciation_date']);
                        }
                    } else {
                        $scope.editAppreciation = '';
                    }
                };
                //proxy
                var retrieveProxyInfo = function (contactToEdit) {
                    if (contactToEdit.proxy != '') {
                        $scope.editProxy = contactToEdit.proxy;
                    } else {
                        $scope.editProxy = '';
                    }
                };
                //languages
                var retrieveLanguagesInfo = function (contactToEdit) {
                    if (contactToEdit['language_assignment'] != '') {
                        $scope.editLanguages = contactToEdit['language_assignment'];
                        for(var i = 0; i<contactToEdit['language_assignment'].length; i++){
                            $scope.editLanguages[i]['date_obsolete'] = new Date(contactToEdit['language_assignment'][i]['date_obsolete']);
                        }
                    } else {
                        $scope.editLanguages = '';
                    }
                };
                //skills and assets
                var retrieveSkillsInfo = function (contactToEdit) {
                    if (contactToEdit['skill_assignment'] != '') {
                        $scope.editSkillsAssets = contactToEdit['skill_assignment'];
                        for(var i = 0; i<contactToEdit['skill_assignment'].length; i++){
                            $scope.editSkillsAssets[i]['date_obsolete'] = new Date(contactToEdit['skill_assignment'][i]['date_obsolete']);
                        }
                    } else {
                        $scope.editSkillsAssets = '';
                    }
                };

                //For generating password
                $scope.generatePassword = function () {
                    var a = Math.floor((Math.random() * 10) + 10);
                    $scope.editUser.password = Math.random().toString(36).substring(2, a);
                };
                //For changing password
                $scope.changePass = false;
                $scope.changePassword = function () {
                    $scope.changePass = !$scope.changePass;
                };
                
//HTTP REQUEST TO EDIT CONTACT

                //define general return message
                var successMsg = "Successfully saved!";
                var failMsg = "Fail to save changes. Please check through your data again.";
                
                //user
                $scope.existedUsername = false;
                $scope.checkingUsername = false;
                $scope.checkedUsername = false;
                $scope.ignore = false;
                
                $scope.$watch('editUser.username', function(){
                    console.log('username: '+$scope.editUser.username);
                    console.log("1 "+$scope.ignore);
                    if($scope.editUser.username == '') {
                        $scope.ignore = false;
                    }
                });
                
                $scope.resultUser = {
                    status: false,
                    message: ''
                };
                $scope.editTheUser = function () {
                    var datasend = {};
                    if ($scope.editMode == 'true') {
                        if ($scope.isUser) {
                            if ($scope.isAdmin) {
                                datasend['token'] = session.getSession('token');
                                datasend['contact_id'] = session.getSession('contactToDisplayCid');
                                datasend['deactivated'] = $scope.editUser['deactivated'];
                                datasend['username'] = $scope.editUser['username'];
                                datasend['password'] = '';
                                datasend['is_admin'] = $scope.editUser['is_admin'];
                                datasend['email'] = '';
                                submitUser(datasend);
                            }
                        } else {
                            $scope.checkingUsername = true;
                            var dataToSend = {};
                            dataToSend['token'] = session.getSession('token');
                            dataToSend['username'] = $scope.editUser['username'];
                            var url = AppAPI.usernameCheck;
                            dataSubmit.submitData(dataToSend, url).then(function (response) {
                                $scope.checkedUsername = true;
                                if (response.data.message == 'success') {
                                    datasend['token'] = session.getSession('token');
                                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                                    datasend['username'] = $scope.editUser['username'];
                                    datasend['password'] = $scope.editUser['password'];
                                    datasend['email'] = $scope.editUser['email'];
                                    datasend['deactivated'] = '';
                                    datasend['is_admin'] = '';
                                    submitUser(datasend);
                                } else {
                                    $scope.existedUsername = true;
                                    $scope.ignore = true;
                                }
                            }, function () {
                                window.alert("Fail to send request!");
                            });  
                        }
                    } else {
                        datasend['token'] = session.getSession('token');
                        datasend['contact_id'] = session.getSession('contactToDisplayCid');
                        datasend['username'] = $scope.editUser['username'];
                        datasend['password'] = $scope.editUser['password'];
                        datasend['email'] = '';
                        datasend['deactivated'] = '';
                        datasend['is_admin'] = '';
                        submitUser(datasend);
                    }
                };
                
                var submitUser = function(datasend) {
                    var url = AppAPI.updateUser;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                    $scope.resultUser.status = true;
                    if (response.data.message == 'success') {
                        $scope.resultUser.message = successMsg;
                        $scope.retrieveFunc();
                    } else {
                        $scope.resultUser.message = failMsg;
                    }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                //contact
                $scope.resultContact = {
                    status: false,
                    message: ''
                };
                $scope.editTheContact = function () {
                    $scope.editContact['token'] = session.getSession('token');
                    $scope.editContact['contact_id'] = session.getSession('contactToDisplayCid');
                    $scope.editContact['user_type'] = session.getSession('userType');
                    //to be modified
                    var url = AppAPI.updateContact;
                    dataSubmit.submitData($scope.editContact, url).then(function (response) {
                        $scope.resultContact.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultContact.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultContact.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheContact = function () {
                    //to add ngDialog for confirmation
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteContact = {};
                        deleteContact['token'] = session.getSession('token');
                        deleteContact['contact_id'] = session.getSession('contactToDisplayCid');
                        var url = AppAPI.deleteContact;
                        deleteService.deleteDataService(deleteContact, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $state.go(toContacts);
                                });
                            } else {
                                console.log("del contact fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };

                //phone
                $scope.addingPhone = false;
                $scope.addNewPhone = function () {
                    $scope.addingPhone = true;
                };
                $scope.resultPhone = {
                    'phone_number': '',
                    status: false,
                    message: ''
                };
                $scope.editThePhone = function ($event, phone) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['country_code'] = phone['country_code'];
                    datasend['phone_number'] = phone['phone_number'];
                    datasend['phone_remarks'] = phone['remarks'];
                    datasend['date_obsolete'] = phone['date_obsolete'];
                    var url = AppAPI.updatePhone;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultPhone.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultPhone['phone_number'] = datasend['phone_number'];
                            $scope.resultPhone.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultPhone.message = failMsg;
                        }
                        $timeout(function () {
                            $scope.resultPhone.status = false;
                        }, 1000);
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteThePhone = function ($event, phone) {
                    //to add ngDialog for confirmation
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deletePhone = {};
                        deletePhone['token'] = session.getSession('token');
                        deletePhone['contact_id'] = session.getSession('contactToDisplayCid');
                        deletePhone['phone_number'] = phone['phone_number'];
                        var url = AppAPI.deletePhone;
                        deleteService.deleteDataService(deletePhone, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del phone fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newPhone = {
                    token: session.getSession("token"),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    'country_code': 65,
                    'phone_number': '',
                    'phone_remarks': '',
                    'date_obsolete': ''
                };
                $scope.copyPhone = angular.copy($scope.newPhone);
                $scope.submitNewPhone = {
                    'submittedPhone': false,
                    'message': ''
                };
                $scope.addPhone = function () {
                    var url = AppAPI.addPhone;
                    dataSubmit.submitData($scope.newPhone, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewPhone.submittedPhone = true;
                            $scope.submitNewPhone.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newPhone = angular.copy($scope.copyPhone);
                            $timeout(function () {
                                $scope.submitNewPhone.submittedPhone = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingPhone = false;
                        } else {
                            $scope.submitNewPhone.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //email
                $scope.addingEmail = false;
                $scope.addNewEmail = function () {
                    $scope.addingEmail = true;
                };
                $scope.resultEmail = {
                    email: '',
                    status: false,
                    message: ''
                };
                $scope.editTheEmail = function ($event, email) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['email'] = email['email'];
                    datasend['email_remarks'] = email['remarks'];
                    datasend['date_obsolete'] = email['date_obsolete'];
                    var url = AppAPI.updateEmail;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultEmail.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultEmail['email'] = datasend['email'];
                            $scope.resultEmail.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultEmail.message = failMsg;
                        }
                        $timeout(function () {
                            $scope.resultEmail.status = false;
                        }, 1000);
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheEmail = function ($event, email) {
                    //to add ngDialog for confirmation
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteEmail = {};
                        deleteEmail['token'] = session.getSession('token');
                        deleteEmail['contact_id'] = session.getSession('contactToDisplayCid');
                        deleteEmail['email'] = email['email'];
                        var url = AppAPI.deleteEmail;
                        deleteService.deleteDataService(deleteEmail, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del email fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newEmail = {
                    token: session.getSession('token'),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    email: '',
                    'email_remarks': '',
                    'date_obsolete': ''
                };
                $scope.copyEmail = angular.copy($scope.newEmail);
                $scope.submitNewEmail = {
                    'submittedEmail': false,
                    'message': ''
                };
                $scope.addEmail = function () {
                    var url = AppAPI.addEmail;
                    dataSubmit.submitData($scope.newEmail, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewEmail.submittedEmail = true;
                            $scope.submitNewEmail.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newEmail = angular.copy($scope.copyEmail);
                            $timeout(function () {
                                $scope.submitNewEmail.submittedEmail = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingEmail = false;
                        } else {
                            $scope.submitNewEmail.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //address
                $scope.addingAddress = false;
                $scope.addNewAddress = function () {
                    $scope.addingAddress = true;
                };
                $scope.resultAddress = {
                    address: '',
                    status: false,
                    message: ''
                };
                $scope.editTheAddress = function ($event, address) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['country'] = address['country'];
                    datasend['address'] = address['address'];
                    datasend['zipcode'] = address['zipcode'];
                    datasend['address_remarks'] = address['remarks'];
                    datasend['date_obsolete'] = address['date_obsolete'];
                    var url = AppAPI.updateAddress;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultAddress.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultAddress.address = datasend['address'];
                            $scope.resultAddress.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultAddress.message = failMsg;
                        }
                        $timeout(function () {
                            $scope.resultAddress.status = false;
                        }, 1000);
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheAddress = function ($event, address) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteAddress = {};
                        deleteAddress['token'] = session.getSession('token');
                        deleteAddress['contact_id'] = session.getSession('contactToDisplayCid');
                        deleteAddress['address'] = address['address'];
                        var url = AppAPI.deleteAddress;
                        deleteService.deleteDataService(deleteAddress, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del address fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newAddress = {
                    token: session.getSession('token'),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    address: '',
                    country: '',
                    zipcode: '',
                    'address_remarks': '',
                    'date_obsolete': ''
                };
                $scope.copyAddress = angular.copy($scope.newAddress);
                $scope.submitNewAddress = {
                    'submittedAddress': false,
                    'message': ''
                };
                $scope.addAddress = function () {
                    var url = AppAPI.addAddress;
                    dataSubmit.submitData($scope.newAddress, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewAddress.submittedAddress = true;
                            $scope.submitNewAddress.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newAddress = angular.copy($scope.copyAddress);
                            $timeout(function () {
                                $scope.submitNewAddress.submittedAddress = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingAddress = false;
                        } else {
                            $scope.submitNewAddress.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //membership
                $scope.addingMembership = false;
                $scope.addNewMembership = function () {
                    $scope.addingMembership = true;
                };
                $scope.resultMembership = {
                    'membership_id': '',
                    status: false,
                    message: ''
                };
                $scope.editTheMembership = function ($event, membership) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['membership_id'] = membership['membership_id'];
                    datasend['start_membership'] = membership['start_date'];
                    datasend['end_membership'] = membership['end_date'];
                    datasend['receipt_date'] = membership['receipt_date'];
                    datasend['subscription_amount'] = membership['subscription_amount'];
                    datasend['ext_transaction_ref'] = membership['ext_transaction_ref'];
                    datasend['receipt_number'] = membership['receipt_number'];
                    datasend['remarks'] = membership['remarks'];
                    datasend['receipt_mode'] = membership['receipt_mode_name'];
                    datasend['explain_if_other_receipt'] = membership['explain_if_other_receipt'];
                    datasend['membership_class'] = membership['membership_class_name'];
                    datasend['explain_if_other_class'] = membership['explain_if_other_class'];
                    datasend['payment_mode'] = membership['payment_mode_name'];
                    datasend['explain_if_other_payment'] = membership['explain_if_other_payment'];
                    var url = AppAPI.updateMembership;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultMembership.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultMembership['membership_id'] = datasend['membership_id'];
                            $scope.resultMembership.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultMembership.message = failMsg;
                        }
                        $timeout(function () {
                            $scope.resultMembership.status = false;
                        }, 1000);
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheMembership = function ($event, membership) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteMembership = {};
                        deleteMembership['token'] = session.getSession('token');
                        deleteMembership['membership_id'] = membership['membership_id'];
                        var url = AppAPI.deleteMembership;
                        deleteService.deleteDataService(deleteMembership, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del membership fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newMembership = {
                    token: session.getSession("token"),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    'user_type': session.getSession('userType'),
                    'start_membership': '',
                    'end_membership': '',
                    'receipt_date': '',
                    'subscription_amount': '',
                    'ext_transaction_ref': '',
                    'receipt_number': '',
                    'remarks': '',
                    'receipt_mode': '',
                    'explain_if_other_receipt': '',
                    'membership_class': '',
                    'explain_if_other_class': '',
                    'payment_mode': '',
                    'explain_if_other_payment': ''
                };
                $scope.copyMembership = angular.copy($scope.newMembership);
                $scope.submitNewMembership = {
                    'submittedMembership': false,
                    'message': ''
                };
                $scope.addMembership = function () {
                    var url = AppAPI.addMembership;
                    dataSubmit.submitData($scope.newMembership, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewMembership.submittedMembership = true;
                            $scope.submitNewMembership.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newMembership = angular.copy($scope.copyMembership);
                            $timeout(function () {
                                $scope.submitNewMembership.submittedMembership = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingMembership = false;
                        } else {
                            $scope.submitNewMembership.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //office held
                //require office_held_id to be returned as well???
                $scope.addingOffice = false;
                $scope.addNewOffice = function () {
                    $scope.addingOffice = true;
                };
                $scope.resultOffice = {
                    'office_held': '',
                    status: false,
                    message: ''
                };
                $scope.editTheOffice = function ($event, officeHeld) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['office_held_name'] = officeHeld['office_held'];
                    datasend['start_office'] = officeHeld['start_office'];
                    datasend['end_office'] = officeHeld['end_office'];
                    datasend['remarks'] = officeHeld['remarks'];
                    var url = AppAPI.updateOfficeHeld;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultOffice.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultOffice['office_held'] = datasend['office_held_name']; //to be modified
                            $scope.resultOffice.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultOffice.message = failMsg;
                        }
                        $timeout(function () {
                            $scope.resultOffice.status = false;
                        }, 1000);
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheOffice = function ($event, officeHeld) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteOffice = {};
                        deleteOffice['token'] = session.getSession('token');
                        deleteOffice['contact_id'] = session.getSession('contactToDisplayCid');
                        deleteOffice['office_held_name'] = officeHeld['office_held'];
                        var url = AppAPI.deleteOfficeHeld;
                        deleteService.deleteDataService(deleteOffice, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del office fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newOffice = {
                    token: session.getSession("token"),
                    'contact_id': session.getSession('contactToDisplayCid'), //to be confirmed
                    'user_type': session.getSession('userType'),
                    'office_held_name': '',
                    'start_office': '',
                    'end_office': '',
                    'remarks': ''
                };
                $scope.copyOffice = angular.copy($scope.newOffice);
                $scope.submitNewOffice = {
                    'submittedOffice': false,
                    'message': ''
                };
                $scope.addOffice = function () {
                    var url = AppAPI.addOfficeHeld;
                    dataSubmit.submitData($scope.newOffice, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewOffice.submittedOffice = true;
                            $scope.submitNewOffice.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newOffice = angular.copy($scope.copyOffice);
                            $timeout(function () {
                                $scope.submitNewOffice.submittedOffice = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingOffice = false;
                        } else {
                            $scope.submitNewOffice.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //donation
                $scope.addingDonation = false;
                $scope.addNewDonation = function () {
                    $scope.addingDonation = true;
                };
                $scope.resultDonation = {
                    'donation_id': '',
                    status: false,
                    message: ''
                };
                $scope.editTheDonation = function ($event, donation) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['donation_id'] = donation['donation_id'];
                    datasend['date_received'] = donation['date_received'];
                    datasend['donation_amount'] = donation['donation_amount'];
                    datasend['payment_mode'] = donation['payment_mode'];
                    datasend['explain_if_other_payment'] = donation['explain_if_other_payment'];
                    datasend['ext_transaction_ref'] = donation['ext_transaction_ref'];
                    datasend['receipt_number'] = donation['receipt_number'];
                    datasend['receipt_date'] = donation['receipt_date'];
                    datasend['receipt_mode'] = donation['receipt_mode_name'];
                    datasend['explain_if_other_receipt'] = donation['explain_if_other_receipt'];
                    datasend['donor_instruction'] = donation['donor_instructions'];
                    datasend['allocation1'] = donation['allocation1'];
                    datasend['subamount1'] = donation['subtotal1'];
                    datasend['allocation2'] = donation['allocation2'];
                    datasend['subamount2'] = donation['subtotal2'];
                    datasend['allocation3'] = donation['allocation3'];
                    datasend['subamount3'] = donation['subtotal3'];
                    datasend['associated_occasion'] = donation['associated_occasion'];
                    datasend['remarks'] = donation['remarks'];
                    var url = AppAPI.updateDonation;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultDonation.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultDonation['donation_id'] = datasend['donation_id'];
                            $scope.resultDonation.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultDonation.message = "Fail to update donation.";
                        }
                        $timeout(function () {
                            $scope.resultDonation.status = false;
                        }, 1000);
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheDonation = function ($event, donation) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteDonation = {};
                        deleteDonation['token'] = session.getSession('token');
                        deleteDonation['donation_id'] = donation['donation_id'];
                        var url = AppAPI.deleteDonation;
                        deleteService.deleteDataService(deleteDonation, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del donation fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newDonation = {
                    token: session.getSession("token"),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    'user_type': session.getSession('userType'),
                    'date_received': '',
                    'donation_amount': '',
                    'payment_mode': '',
                    'explain_if_other_payment': '',
                    'ext_transaction_ref': '',
                    'receipt_number': '',
                    'receipt_date': '',
                    'receipt_mode': '',
                    'explain_if_other_receipt': '',
                    'donor_instruction': '',
                    'allocation1': '',
                    'subamount1': '',
                    'allocation2': '',
                    'subamount2': '',
                    'allocation3': '',
                    'subamount3': '',
                    'associated_occasion': '',
                    'remarks': ''
                };
                $scope.copyDonation = angular.copy($scope.newDonation);
                $scope.submitNewDonation = {
                    'submittedDonation': false,
                    'message': ''
                };
                $scope.addDonation = function () {
                    var url = AppAPI.addDonation;
                    dataSubmit.submitData($scope.newDonation, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewDonation.submittedDonation = true;
                            $scope.submitNewDonation.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newDonation = angular.copy($scope.copyDonation);
                            $timeout(function () {
                                $scope.submitNewDonation.submittedDonation = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingDonation = false;
                        } else {
                            $scope.submitNewDonation.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                //team join
                $scope.addingTeam = false;
                $scope.addNewTeam = function () {
                    $scope.addingTeam = true;
                };
                $scope.resultTeam = {
                    'team_name': '',
                    status: false,
                    message: ''
                };
                $scope.editTheTeam = function ($event, team) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['team'] = team['team_name'];
                    datasend['permission_level'] = team['permission'];
                    datasend['explain_if_other'] = team['explain_if_others'];
                    datasend['subteam'] = team['sub_team'];
                    datasend['date_obsolete'] = team['date_obsolete'];
                    datasend['remarks'] = team['remarks'];
                    var url = AppAPI.updateTeamJoin;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        console.log(response.data);
                        $scope.resultTeam.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultTeam['team_name'] = datasend['team'];
                            $scope.resultTeam.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultTeam.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheTeam = function ($event, team) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteTeamjoin = {};
                        deleteTeamjoin['token'] = session.getSession('token');
                        deleteTeamjoin['contact_id'] = session.getSession('contactToDisplayCid');
                        deleteTeamjoin['team'] = team['team_name'];
                        var url = AppAPI.deleteTeamJoin;
                        deleteService.deleteDataService(deleteTeamjoin, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del team join fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newTeam = {
                    'token': session.getSession('token'),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    'user_type': session.getSession('userType'),
                    'team': '',
                    'permission_level': '',
                    'explain_if_other': '',
                    'subteam': '',
                    'date_obsolete': '',
                    'remarks': ''
                };
                $scope.copyTeam = angular.copy($scope.newTeam);
                $scope.submitNewTeam = {
                    'submittedTeam': false,
                    'message': ''
                };
                $scope.addTeam = function () {
                    var url = AppAPI.addTeamJoin;
                    dataSubmit.submitData($scope.newTeam, url).then(function (response) {
                        console.log(response.data);
                        if (response.data.message == 'success') {
                            $scope.submitNewTeam.submittedTeam = true;
                            $scope.submitNewTeam.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newTeam = angular.copy($scope.copyTeam);
                            $timeout(function () {
                                $scope.submitNewTeam.submittedTeam = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingTeam = false;
                        } else {
                            $scope.submitNewTeam.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //appreciation
                $scope.addingAppreciation = false;
                $scope.addNewAppreciation = function () {
                    $scope.addingAppreciation = true;
                };
                $scope.resultAppreciation = {
                    'appreciation_id': '',
                    status: false,
                    message: ''
                };
                $scope.editTheAppreciation = function ($event, appreciation) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['appreciation_id'] = appreciation['appreciation_id'];
                    datasend['appraisal_comment'] = appreciation['appraisal_comments'];
                    datasend['appraisal_by'] = appreciation['appraisal_by'];
                    datasend['appraisal_date'] = appreciation['appraisal_date'];
                    datasend['appreciation_gesture'] = appreciation['appreciation_gesture'];
                    datasend['appreciation_by'] = appreciation['appreciation_by'];
                    datasend['appreciation_date'] = appreciation['appreciation_date'];
                    datasend['remarks'] = appreciation['remarks'];
                    var url = AppAPI.updateAppreciation;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultAppreciation.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultAppreciation['appreciation_id'] = datasend['appreciation_id'];
                            $scope.resultAppreciation.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultAppreciation.message = failMsg;
                        }
                        $timeout(function () {
                            $scope.resultAppreciation.status = false;
                        }, 1000);
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheAppreciation = function ($event, appreciation) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteAppreciation = {};
                        deleteAppreciation['token'] = session.getSession('token');
                        deleteAppreciation['appreciation_id'] = appreciation['appreciation_id'];
                        var url = AppAPI.deleteAppreciation;
                        deleteService.deleteDataService(deleteAppreciation, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del appreciation fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newAppreciation = {
                    'token': session.getSession('token'),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    'user_type': session.getSession('userType'),
                    'appraisal_comment': '',
                    'appraisal_by': '',
                    'appraisal_date': '',
                    'appreciation_gesture': '',
                    'appreciation_by': '',
                    'appreciation_date': '',
                    'remarks': ''
                };
                $scope.copyAppreciation = angular.copy($scope.newAppreciation);
                $scope.submitNewAppreciation = {
                    'submittedAppreciation': false,
                    'message': ''
                };
                $scope.addAppreciation = function () {
                    var url = AppAPI.addAppreciation;
                    dataSubmit.submitData($scope.newAppreciation, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewAppreciation.submittedAppreciation = true;
                            $scope.submitNewAppreciation.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newAppreciation = angular.copy($scope.copyAppreciation);
                            $timeout(function () {
                                $scope.submitNewAppreciation.submittedAppreciation = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingAppreciation = false;
                        } else {
                            $scope.submitNewAppreciation.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //proxy
                $scope.addingProxy = false;
                $scope.addNewProxy = function () {
                    $scope.addingProxy = true;
                };
                $scope.resultProxy = {
                    'proxy_id': '',
                    status: false,
                    message: ''
                };
                $scope.editTheProxy = function ($event, proxy) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['proxy_of'] = session.getSession('contactToDisplayCid');
                    datasend['principal_of'] = selectedProxy.cid;;
                    datasend['proxy_standing'] = proxy['proxy_standing'];
                    datasend['remarks'] = proxy['remarks'];
                    datasend['date_obsolete'] = proxy['date_obsolete'];
                    var url = AppAPI.updateProxy;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultProxy.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultProxy['proxy_id'] = datasend['proxy_of'];
                            $scope.resultProxy.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultProxy.message = failMsg;
                        }
                        $timeout(function () {
                            $scope.resultProxy.status = false;
                        }, 1000);
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheProxy = function ($event, proxy) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteProxy = {};
                        deleteProxy['token'] = session.getSession('token');
                        deleteProxy['proxy_of'] = proxy['proxy_id'];
                        deleteProxy['principal_of'] = proxy['principal_id'];
                        var url = AppAPI.deleteProxy;
                        deleteService.deleteDataService(deleteProxy, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del proxy fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                
                $scope.newProxy = {
                    'token': session.getSession('token'),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    'user_type': session.getSession('userType'),
                    'proxy_of': session.getSession('contactToDisplayCid'),
                    'principal_of': '',
                    'proxy_standing': '',
                    'remarks': '',
                    'date_obsolete': ''
                };
                $scope.copyProxy = angular.copy($scope.newProxy);
                $scope.submitNewProxy = {
                    'submittedProxy': false,
                    'message': ''
                };
                $scope.addProxy = function () {
                    $scope.newProxy['principal_of'] = selectedProxy.cid;
                    var url = AppAPI.addProxy;
                    dataSubmit.submitData($scope.newProxy, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewProxy.submittedProxy = true;
                            $scope.submitNewProxy.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newProxy = angular.copy($scope.copyProxy);
                            $timeout(function () {
                                $scope.submitNewProxy.submittedProxy = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingProxy = false;
                        } else {
                            $scope.submitNewProxy.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //languages
                $scope.addingLanguages = false;
                $scope.addNewLanguages = function () {
                    $scope.addingLanguages = true;
                };
                $scope.resultLanguage = {
                    'language_name': '',
                    status: false,
                    message: ''
                };
                $scope.editTheLanguage = function ($event, language) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['language'] = language['language_name'];
                    datasend['speak_write'] = language['proficiency'];
                    datasend['explain_if_other'] = language['explain_if_other'];
                    datasend['remarks'] = language['remarks'];
                    datasend['date_obsolete'] = language['date_obsolete'];
                    var url = AppAPI.updateLanguage;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultLanguage.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultLanguage['language_name'] = datasend['language'];
                            $scope.resultLanguage.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultLanguage.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheLanguage = function ($event, language) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteLanguage = {};
                        deleteLanguage['token'] = session.getSession('token');
                        deleteLanguage['contact_id'] = session.getSession('contactToDisplayCid');
                        deleteLanguage['language'] = language['language_name'];
                        var url = AppAPI.deleteLanguage;
                        deleteService.deleteDataService(deleteLanguage, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function () {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del language fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newLanguages = {
                    'token': session.getSession('token'),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    'user_type': session.getSession('userType'),
                    'language': '',
                    'speak_write': '',
                    'explain_if_other': '',
                    'remarks': '',
                    'date_obsolete': ''
                };
                $scope.copyLanguages = angular.copy($scope.newLanguages);
                $scope.submitNewLanguages = {
                    'submittedLanguages': false,
                    'message': ''
                };
                $scope.addLanguages = function () {
                    var url = AppAPI.addLanguage;
                    dataSubmit.submitData($scope.newLanguages, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewLanguages.submittedLanguages = true;
                            $scope.submitNewLanguages.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newLanguages = angular.copy($scope.copyLanguages);
                            $timeout(function () {
                                $scope.submitNewLanguages.submittedLanguages = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingLanguages = false;
                        } else {
                            $scope.submitNewLanguages.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                
                //skills and assets
                $scope.addingSkills = false;
                $scope.addNewSkills = function () {
                    $scope.addingSkills = true;
                };
                $scope.resultSkill = {
                    'skill_name': '',
                    status: false,
                    message: ''
                };
                $scope.editTheSkill = function ($event, skill) {
                    var datasend = {};
                    datasend['token'] = session.getSession('token');
                    datasend['contact_id'] = session.getSession('contactToDisplayCid');
                    datasend['user_type'] = session.getSession('userType');
                    datasend['skill_asset'] = skill['skill_name'];
                    datasend['explain_if_other'] = skill['explain_if_other'];
                    datasend['remarks'] = skill['remarks'];
                    datasend['date_obsolete'] = skill['date_obsolete'];
                    var url = AppAPI.updateSkill;
                    dataSubmit.submitData(datasend, url).then(function (response) {
                        $scope.resultSkill.status = true;
                        if (response.data.message == 'success') {
                            $scope.resultSkill['skill_name'] = datasend['skill_asset'];
                            $scope.resultSkill.message = successMsg;
                            $scope.retrieveFunc();
                        } else {
                            $scope.resultSkill.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };
                $scope.deleteTheSkill = function ($event, skill) {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/deletePrompt.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    }).then(function (response) {
                        var deleteSkill = {};
                        deleteSkill['token'] = session.getSession('token');
                        deleteSkill['contact_id'] = session.getSession('contactToDisplayCid');
                        deleteSkill['skill_asset'] = skill['skill_name'];
                        var url = AppAPI.deleteSkill;
                        deleteService.deleteDataService(deleteSkill, url).then(function (response) {
                            if (response.data.message == 'success') {
                                ngDialog.openConfirm({
                                    template: './style/ngTemplate/deleteSuccess.html',
                                    className: 'ngdialog-theme-default',
                                    scope: $scope
                                }).then(function (response) {
                                    $scope.retrieveFunc();
                                });
                            } else {
                                console.log("del skill fail");
                            }
                        }, function () {
                            window.alert("Fail to send request!");
                        });
                    });
                };
                $scope.newSkills = {
                    'token': session.getSession('token'),
                    'contact_id': session.getSession('contactToDisplayCid'),
                    'user_type': session.getSession('userType'),
                    'skill_asset': '',
                    'explain_if_other': '',
                    'remarks': '',
                    'date_obsolete': ''
                };
                $scope.copySkills = angular.copy($scope.newSkills);
                $scope.submitNewSkills = {
                    'submittedSkills': false,
                    'message': ''
                };
                $scope.addSkills = function () {
                    var url = AppAPI.addSkill;
                    dataSubmit.submitData($scope.newSkills, url).then(function (response) {
                        if (response.data.message == 'success') {
                            $scope.submitNewSkills.submittedSkills = true;
                            $scope.submitNewSkills.message = successMsg;
                            $scope.retrieveFunc();
                            $scope.newSkills = angular.copy($scope.copySkills);
                            $timeout(function () {
                                $scope.submitNewSkills.submittedSkills = false;
                            }, 1000);
                            //can set $scope.addingPhone = false if wanting to hide
                            $scope.addingSkills = false;
                        } else {
                            $scope.submitNewSkills.message = failMsg;
                        }
                    }, function () {
                        window.alert("Fail to send request!");
                    });
                };

                //datepickerrrrr
                $scope.today = function () {
                    $scope.dt = new Date();
                };
                $scope.today();

                $scope.clear = function () {
                    $scope.dt = null;
                };

                $scope.inlineOptions = {
                    customClass: getDayClass,
                    showWeeks: true
                };

                $scope.dateOptions = {
                    formatYear: 'yy',
                    formatMonth: 'MMM',
                    formatDay: 'dd',
                    startingDay: 1
                };

                $scope.openedPhone = [];
                $scope.openPhone = function (index) {
                    $timeout(function () {
                        $scope.openedPhone[index] = true;
                    });
                };
                
                $scope.openedEmail = [];
                $scope.openEmail = function(index){
                    $timeout(function(){
                       $scope.openedEmail[index] = true; 
                    });
                };
                
                $scope.openedAddress = [];
                $scope.openAddress = function(index){
                    $timeout(function(){
                        $scope.openedAddress[index] = true;
                    });
                };
                
                $scope.openedMStart = [];
                $scope.openMStart = function(index){
                    $timeout(function(){
                        $scope.openedMStart[index] = true;
                    });
                };
                
                $scope.openedMEnd = [];
                $scope.openMEnd = function(index){
                    $timeout(function(){
                        $scope.openedMEnd[index] = true;
                    });
                };
                
                $scope.openedMembership = [];
                $scope.openMembership = function(index){
                    $timeout(function(){
                        $scope.openedMembership[index] = true;
                    });
                };
                
                $scope.openedOStart = [];
                $scope.openOStart = function(index){
                    $timeout(function(){
                        $scope.openedOStart[index] = true;
                    });
                };
                
                $scope.openedOEnd = [];
                $scope.openOEnd = function(index){
                    $timeout(function(){
                        $scope.openedOEnd[index] = true;
                    });
                };
                
                $scope.openedDonationReceived = [];
                $scope.openDonationReceived = function(index){
                    $timeout(function(){
                        $scope.openedDonationReceived[index] = true;
                    });
                };
                
                $scope.openedDonationReceipt = [];
                $scope.openDonationReceipt = function(index){
                    $timeout(function(){
                        $scope.openedDonationReceipt[index] = true;
                    });
                };
                
                $scope.openedTeamJoin = [];
                $scope.openTeamJoin = function(index){
                    $timeout(function(){
                        $scope.openedTeamJoin[index] = true;
                    });
                };

                $scope.openedAppraisal = [];
                $scope.openAppraisal = function(index){
                    $timeout(function(){
                        $scope.openedAppraisal[index] = true;
                    });
                };
                
                $scope.openedAppreciation = [];
                $scope.openAppreciation = function(index){
                    $timeout(function(){
                        $scope.openedAppreciation[index] = true;
                    });
                };
                
                $scope.openedLanguages = [];
                $scope.openLanguages = function(index){
                    $timeout(function(){
                        $scope.openedLanguages[index] = true;
                    });
                };
                
                $scope.openedSkills = [];
                $scope.openSkills = function(index){
                    $timeout(function(){
                        $scope.openedSkills[index] = true;
                    });
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
                $scope.format = 'dd MMM yyyy';
                $scope.altInputFormats = ['M!/d!/yyyy'];

            }]);

