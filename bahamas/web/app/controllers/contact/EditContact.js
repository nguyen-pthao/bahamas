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
        require: '?ngModel',
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

app.directive('empty', function () {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, elem, attrs, ngModel) {
            if (!ngModel) {
                return;
            }
            scope.$watch(attrs.ngModel, function () {
                validate();
            });
            var validate = function () {
                var val1 = ngModel.$viewValue;
                ngModel.$setValidity('empty', val1 != null && val1.length != 0);
            };
        }
    };
});
//
//app.directive('setDecimal',['$timeout', function($timeout){
//    return {
//        restrict: 'A',
//        require: '?ngModel',
//        link: function(scope, elem, attrs, ngModel) {
//            ngModel.$parsers.push(function(data){
//                if(data != '' && data != null) {
//                    data = parseFloat(data).toFixed(2);
//                    $timeout(function(){
//                        elem.val(data);
//                    }, 5000);    
//                    return data;
//                } else {
//                    return '';
//                }
//            });
//        }
//    };
//}]);

app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

app.controller('editContact',
        ['$scope', '$state', 'session',
            'loadCountries', 'loadTeamAffiliation', 'loadPaymentMode', 'loadModeOfSendingReceipt',
            'retrieveContactByCid', 'retrieveOwnContactInfo',
            function ($scope, $state, session,
                    loadCountries, loadTeamAffiliation, loadPaymentMode, loadModeOfSendingReceipt,
                    retrieveContactByCid, retrieveOwnContactInfo) {

                var permission = session.getSession('userType');

                //viewing mode: false for own contact and true for other people contact
                $scope.editMode = session.getSession('otherContact');

//DEFINE REGEX
                $scope.nationalityRegex = '[A-Za-z ]{0,20}';
                $scope.nricRegex = '[A-Za-z][0-9]\\d{6}[A-Za-z]'; //notice that \d won't work but \\d
                $scope.phoneRegex = '[0-9-]{0,20}';
                $scope.emailRegex = '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}';
                $scope.decimalRegex = new RegExp('^(\\d+)\\.{0,1}\\d+$');

//GET USER PERMISSION
                if ($scope.editMode == null) {
                    $scope.editMode = 'false';
                }

                $scope.authorised = false;
                if (permission != 'associate' && permission != 'novice') {
                    $scope.authorised = true;
                } else {
                    if ($scope.editMode == 'true') {
                        $scope.viewAllContacts();
                    }
                }

                //only admin is allowed to view username and to deactivate user
                $scope.isAdmin = false;
                $scope.isEventLeader = false;
                $scope.isTeamManager = false;
                var contactToRetrieve = {};
                var other_cid = -1;
                if ($scope.authorised && $scope.editMode == 'true') {
                    if (permission === 'admin') {
                        $scope.isAdmin = true;
                    } else if (permission === 'eventleader') {
                        $scope.isEventLeader = true;
                    } else if (permission == 'teammanager') {
                        $scope.isTeamManager = true;
                    }

//CONTACT TO BE EDITTED
                    other_cid = parseInt(session.getSession('contactToDisplayCid'));
                    if (session.getSession('teams') === 'undefined') {
                        contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'other_cid': other_cid,
                            'team_name': '',
                            'permission': permission
                        };
                    } else {
                        contactToRetrieve = {
                            'token': session.getSession('token'),
                            'cid': angular.fromJson(session.getSession('contact')).cid,
                            'other_cid': other_cid,
                            'team_name': angular.fromJson(session.getSession('teams'))[0].teamname,
                            'permission': permission
                        };
                    }
                } else {
                    if (permission === 'admin') {
                        $scope.isAdmin = true;
                    } else if (permission === 'eventleader') {
                        $scope.isEventLeader = true;
                    } else if (permission == 'teammanager') {
                        $scope.isTeamManager = true;
                    }
                    contactToRetrieve = {
                        'token': session.getSession('token')
                    };
                }

//PAGES TRANSITION
                var toContact = '';
                if ($scope.authorised && $scope.editMode == 'true') {
                    toContact = permission + '.viewIndivContact';
                } else {
                    toContact = permission + '.profile';
                }
                var homepage = permission;
                var toContacts = permission + '.viewContacts';

                $scope.backHome = function () {
                    $state.go(homepage);
                };
                $scope.viewContact = function () {
                    $state.go(toContact);
                };
                $scope.viewAllContacts = function () {
                    $state.go(toContacts);
                };

//CALL DROPDOWN LIST SERVICES (shared by different tabs)
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
                $scope.loadTeamAffiliationList = function () {
                    loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                        $scope.teamAffiliationList = response.data.teamAffiliationList;
                        var other;
                        for (var obj in $scope.teamAffiliationList) {
                            if ($scope.teamAffiliationList[obj].teamAffiliation == 'Others' || $scope.teamAffiliationList[obj].teamAffiliation == 'Other') {
                                other = $scope.teamAffiliationList.splice(obj, 1);
                            }
                        }
                        if(other != null && !angular.isUndefined(other)) {
                            $scope.teamAffiliationList.push(other[0]);
                            $scope.teamAffiliationList0 = angular.copy($scope.teamAffiliationList);
                            $scope.teamAffiliationList0.pop();
                        }
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
                                retrieveUserInfo(contactToEdit);
                                retrievePersonalInfo(contactToEdit);
                                retrievePhoneInfo(contactToEdit);
                                retrieveEmailInfo(contactToEdit);
                                retrieveVerifiedEmailInfo(contactToEdit);
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
                                $scope.contactToEditCID = contactToEdit['other_cid'];
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
                                retrieveVerifiedEmailInfo(contactToEdit);
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
                                $scope.contactToEditCID = contactToEdit['cid'];
                            } else {
                                console.log("Wrong data passed");
                                return contactToEdit;

                            }
                        }, function (response) {
                            window.alert('Fail to connect to server');
                        });
                    }
                };

//DECLARE OBJECTS FOR EDIT CONTACT
                $scope.editUser = {
                    'username': '',
                    'profile_pic' : ''
                };
                $scope.editContact = {};
                $scope.isUser = true;
                var today = new Date();
                today.setDate(today.getDate() - 1);
                //user
                var retrieveUserInfo = function (contactToEdit) {
                    if ($scope.editMode == 'true') {
                        if ($scope.isAdmin) {
                            if (contactToEdit.username != '') {
                                $scope.editUser['username'] = contactToEdit.username;
                                $scope.editUser.deactivated = contactToEdit.deactivated;
                                $scope.editUser['is_admin'] = contactToEdit['is_admin'];
                                $scope.editUser['contact_id'] = contactToEdit['other_cid'];
                                $scope.editUser.profile_pic = contactToEdit['profile_pic'];
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
                        $scope.editUser['contact_id'] = contactToEdit['cid'];
                        $scope.editUser.profile_pic = contactToEdit['profile_pic'];
                    }
                    if($scope.editUser.profile_pic == '') {
                        $scope.editUser.profile_pic = 'images/default.jpg';
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
                    $scope.editContact['notification'] = false;
                };
                //phone
                var retrievePhoneInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit.phone) && contactToEdit.phone != '') {
                        $scope.editPhone = contactToEdit.phone;
                        
                        for (var i = 0; i < contactToEdit.phone.length; i++) {
                            $scope.editPhone[i].isObsolete = false;
                            $scope.editPhone[i]['date_obsolete'] = new Date(contactToEdit.phone[i]['date_obsolete']);
                            
                            if($scope.editPhone[i]['date_obsolete'] != 'Invalid Date' && $scope.editPhone[i]['date_obsolete'] < today) {
                                $scope.editPhone[i].isObsolete = true;
                            }
                        }
                        
                    } else {
                        $scope.editPhone = '';
                    }
                };
                //email
                var retrieveEmailInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit.email) && contactToEdit.email != '') {
                        $scope.editEmail = contactToEdit.email;
                        for (var i = 0; i < contactToEdit.email.length; i++) {
                            $scope.editEmail[i].isObsolete = false;
                            $scope.editEmail[i]['date_obsolete'] = new Date(contactToEdit.email[i]['date_obsolete']);
                            
                            if($scope.editEmail[i]['date_obsolete'] != 'Invalid Date' && $scope.editEmail[i]['date_obsolete'] < today) {
                                $scope.editEmail[i].isObsolete = true;
                            }
                            
                            if (contactToEdit.email[i]['verified'] == 'true') {
                                $scope.editEmail[i]['is_verified'] = 'Yes';
                            } else {
                                $scope.editEmail[i]['is_verified'] = 'No';
                            }
                            $scope.editEmail[i]['resend'] = false;
                        }
                    } else {
                        $scope.editEmail = '';
                    }
                };
                //verified_email
                var retrieveVerifiedEmailInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit['verified_email']) && contactToEdit['verified_email'] != '') {
                        $scope.editVerifiedEmail = contactToEdit['verified_email'];
                        $scope.emailList = [];
                        for (var i = 0; i < contactToEdit['verified_email'].length; i++) {
                            $scope.emailList.push({'email': $scope.editVerifiedEmail[i]['verified_email']});
                        }
                    } else {
                        $scope.editVerifiedEmail = '';
                    }
                };
                //address
                var retrieveAddressInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit.address) && contactToEdit.address != '') {
                        $scope.editAddress = contactToEdit.address;
                        for (var i = 0; i < contactToEdit.address.length; i++) {
                            $scope.editAddress[i].isObsolete = false;
                            $scope.editAddress[i]['date_obsolete'] = new Date(contactToEdit.address[i]['date_obsolete']);
                            
                            if($scope.editAddress[i]['date_obsolete'] != 'Invalid Date' && $scope.editAddress[i]['date_obsolete'] < today) {
                                $scope.editAddress[i].isObsolete = true;
                            }
                        }
                    } else {
                        $scope.editAddress = '';
                    }
                };
                //membership
                var retrieveMembershipInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit.membership) && contactToEdit.membership != '') {
                        $scope.editMembership = contactToEdit.membership;
                        for (var i = 0; i < contactToEdit.membership.length; i++) {
                            $scope.editMembership[i]['start'] = contactToEdit.membership[i]['start_date'];
                            $scope.editMembership[i]['end'] = contactToEdit.membership[i]['end_date'];
                            $scope.editMembership[i]['date_obsolete'] = new Date(contactToEdit.membership[i]['date_obsolete']);
                            $scope.editMembership[i]['start_date'] = new Date(contactToEdit.membership[i]['start_date']);
                            $scope.editMembership[i]['end_date'] = new Date(contactToEdit.membership[i]['end_date']);
                            $scope.editMembership[i]['receipt_date'] = new Date(contactToEdit.membership[i]['receipt_date']);
                            $scope.editMembership[i]['subscription_amount'] = (Math.floor(contactToEdit.membership[i]['subscription_amount'] * 100) / 100).toFixed(2);
                        }
                    } else {
                        $scope.editMembership = '';
                    }
                };
                //office held
                var retrieveOfficeInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit['office_held']) && contactToEdit['office_held'] != '') {
                        $scope.editOfficeHeld = contactToEdit['office_held'];
                        for (var i = 0; i < contactToEdit['office_held'].length; i++) {
                            $scope.editOfficeHeld[i]['start'] = contactToEdit['office_held'][i]['start_office'];
                            $scope.editOfficeHeld[i]['end'] = contactToEdit['office_held'][i]['end_office'];
                            $scope.editOfficeHeld[i]['start_office'] = new Date(contactToEdit['office_held'][i]['start_office']);
                            $scope.editOfficeHeld[i]['end_office'] = new Date(contactToEdit['office_held'][i]['end_office']);
                        }
                    } else {
                        $scope.editOfficeHeld = '';
                    }
                };
                //donation
                var retrieveDonationInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit.donation) && contactToEdit.donation != '') {
                        $scope.editDonation = contactToEdit.donation;
                        for (var i = 0; i < contactToEdit.donation.length; i++) {
                            $scope.editDonation[i]['date_shown'] = contactToEdit.donation[i]['date_received'];
                            $scope.editDonation[i]['date_received'] = new Date(contactToEdit.donation[i]['date_received']);
                            $scope.editDonation[i]['receipt_date'] = new Date(contactToEdit.donation[i]['receipt_date']);
                            $scope.editDonation[i]['donation_amount'] = (Math.floor(contactToEdit.donation[i]['donation_amount'] * 100) / 100).toFixed(2);
                            $scope.editDonation[i]['subtotal1'] = (Math.floor(contactToEdit.donation[i]['subtotal1'] * 100) / 100).toFixed(2);
                            $scope.editDonation[i]['subtotal2'] = (Math.floor(contactToEdit.donation[i]['subtotal2'] * 100) / 100).toFixed(2);
                            $scope.editDonation[i]['subtotal3'] = (Math.floor(contactToEdit.donation[i]['subtotal3'] * 100) / 100).toFixed(2);
                        }
                    } else {
                        $scope.editDonation = '';
                    }
                };
                //team join
                var retrieveTeamInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit['team_join']) && contactToEdit['team_join'] != '') {
                        $scope.editTeam = contactToEdit['team_join'];
                        for (var i = 0; i < contactToEdit['team_join'].length; i++) {
                            $scope.editTeam[i].isObsolete = false;
                            $scope.editTeam[i]['date_obsolete'] = new Date(contactToEdit['team_join'][i]['date_obsolete']);
                            
                            if($scope.editTeam[i]['date_obsolete'] != 'Invalid Date' && $scope.editTeam[i]['date_obsolete'] < today) {
                                $scope.editTeam[i].isObsolete = true;
                            }
                            $scope.editTeam[i].permissionCheck = contactToEdit['team_join'][i]['permission'];
                        }
                    } else {
                        $scope.editTeam = '';
                    }
                };
                //training
                var retrieveTrainingInfo = function (contactToEdit) {
                    if (contactToEdit.hasOwnProperty('training')) {
                        if (!angular.isUndefined(contactToEdit.training) && contactToEdit.training != '') {
                            $scope.editTraining = contactToEdit.training;
                            for(var i = 0; i < contactToEdit.training.length; i++) {
                                $scope.editTraining[i]['training_date'] = new Date(contactToEdit.training[i]['training_date']);
                            }
                        } else {
                            $scope.editTraining = '';
                        }
                    } else {
                        $scope.editTraining = '';
                    }
                };
                //appreciation
                var retrieveAppreciationInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit.appreciation) && contactToEdit.appreciation != '') {
                        $scope.editAppreciation = contactToEdit.appreciation;
                        for (var i = 0; i < contactToEdit.appreciation.length; i++) {
                            $scope.editAppreciation[i]['appraisal_date'] = new Date(contactToEdit.appreciation[i]['appraisal_date']);
                            $scope.editAppreciation[i]['appreciation_date'] = new Date(contactToEdit.appreciation[i]['appreciation_date']);
                        }
                    } else {
                        $scope.editAppreciation = '';
                    }
                };
                //proxy
                var retrieveProxyInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit.proxy) && contactToEdit.proxy != '') {
                        $scope.editProxy = contactToEdit.proxy;
                        for (var i = 0; i < contactToEdit.proxy.length; i++) {
                            $scope.editProxy[i].isObsolete = false;
                            $scope.editProxy[i]['date_obsolete'] = new Date(contactToEdit.proxy[i]['date_obsolete']);
                            
                            if($scope.editProxy[i]['date_obsolete'] != 'Invalid Date' && $scope.editProxy[i]['date_obsolete'] < today) {
                                $scope.editProxy[i].isObsolete = true;
                            }
                        }
                    } else {
                        $scope.editProxy = '';
                    }
                };
                //languages
                var retrieveLanguagesInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit['language_assignment']) && contactToEdit['language_assignment'] != '') {
                        $scope.editLanguages = contactToEdit['language_assignment'];
                        for (var i = 0; i < contactToEdit['language_assignment'].length; i++) {
                            $scope.editLanguages[i].isObsolete = false;
                            $scope.editLanguages[i]['date_obsolete'] = new Date(contactToEdit['language_assignment'][i]['date_obsolete']);
                            
                            if($scope.editLanguages[i]['date_obsolete'] != 'Invalid Date' && $scope.editLanguages[i]['date_obsolete'] < today) {
                                $scope.editLanguages[i].isObsolete = true;
                            }
                        }
                    } else {
                        $scope.editLanguages = '';
                    }
                };
                //skills and assets
                var retrieveSkillsInfo = function (contactToEdit) {
                    if (!angular.isUndefined(contactToEdit['skill_assignment']) && contactToEdit['skill_assignment'] != '') {
                        $scope.editSkillsAssets = contactToEdit['skill_assignment'];
                        for (var i = 0; i < contactToEdit['skill_assignment'].length; i++) {
                            $scope.editSkillsAssets[i].isObsolete = false;
                            $scope.editSkillsAssets[i]['date_obsolete'] = new Date(contactToEdit['skill_assignment'][i]['date_obsolete']);
                            
                            if($scope.editSkillsAssets[i]['date_obsolete'] != 'Invalid Date' && $scope.editSkillsAssets[i]['date_obsolete'] < today) {
                                $scope.editSkillsAssets[i].isObsolete = true;
                            }
                        }
                    } else {
                        $scope.editSkillsAssets = '';
                    }
                };

//DECLARE FORM FOR VALIDITY RESET                
                $scope.form = {};

//HTTP REQUEST TO EDIT CONTACT
                //define general return message
                $scope.successMsg = "Successfully saved!";

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
