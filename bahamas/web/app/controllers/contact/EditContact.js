/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*Created by Marcus Ong*/

var app = angular.module('bahamas');

app.controller('editContact',
        ['$scope', '$http', '$state', 'session', 'loadCountries', 'loadContactType', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass', 'loadMembershipClass', 'loadPaymentMode', 'loadModeOfSendingReceipt', 'loadOfficeList', 'retrieveContactByCid',
            function ($scope, $http, $state, session, loadCountries, loadContactType, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass, loadMembershipClass, loadPaymentMode, loadModeOfSendingReceipt, loadOfficeList, retrieveContactByCid) {
//        $scope.contact = session.getSession('contactToDisplay');
                var permission = session.getSession('userType');

//GET USER PERMISSION
                //only admin is allowed to view username and to deactivate user
                $scope.isAdmin = false;
                if (permission === 'admin') {
                    $scope.isAdmin = true;
                } else if (permission === 'teammanager') {
                    $scope.isAdmin = false;
                }

//CONTACT TO BE EDITTED
                var contactToRetrieve = {};
                if (session.getSession('teams') === 'undefined') {
                    var contactToRetrieve = {
                        'token': session.getSession('token'),
                        'cid': angular.fromJson(session.getSession('contact')).cid,
                        'other_cid': session.getSession('contactToDisplayCid'),
                        'team_name': '',
                        'permission': session.getSession('userType')
                    };
                } else {
                    var contactToRetrieve = {
                        'token': session.getSession('token'),
                        'cid': angular.fromJson(session.getSession('contact')).cid,
                        'other_cid': session.getSession('contactToDisplayCid'),
                        'team_name': angular.fromJson(session.getSession('teams'))[0].teamname,
                        'permission': $scope.permission
                    };
                }

//PAGES TRANSITION
                var toContact = permission + '.viewIndivContact';
                var homepage = permission + '.homepage';
                $scope.backHome = function () {
                    $state.go(homepage);
                };

                $scope.viewContact = function () {
                    $state.go(toContact);
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

//RETRIEVE CONTACT OBJECTS
                var contactToEdit = {};
                $scope.loadContactInfo = retrieveContactByCid.retrieveContact(contactToRetrieve).then(function (response) {
                    if (response.data.message == 'success') {
                        contactToEdit = response.data.contact[0];
                        
                        //DECLARE OBJECTS FOR EDIT CONTACT
                        //user
                        $scope.editContact = {};
                        $scope.isUser = true;
                        if ($scope.isAdmin) {
                            $scope.editContact['username'] = contactToEdit.username;
                            $scope.editContact.deactivated = contactToEdit.deactivated;
                        } else {
                            $scope.isUser = false;
                            $scope.editContact['username'] = '';
                            $scope.editContact.deactivated = false; //to be changed
                            $scope.editContact['password'] = ''; //to be changed
                        }
                        //contact
                        $scope.editContact['date_created'] = contactToEdit['date_created'];
                        $scope.editContact['created_by'] = contactToEdit['created_by'];
                        $scope.editContact['name'] = contactToEdit.name;
                        $scope.editContact['alt_name'] = contactToEdit['alt_name'];
                        $scope.editContact['contact_type'] = contactToEdit['contact_type'];
                        $scope.editContact['explain_if_other'] = contactToEdit['explain_if_other'];
                        $scope.editContact['profession'] = contactToEdit.profession;
                        $scope.editContact['job_title'] = contactToEdit['job_title'];
                        $scope.editContact['nric_fin'] = contactToEdit['nric_fin'];
                        $scope.editContact.nationality = contactToEdit.nationality;
                        $scope.editContact['gender'] = contactToEdit.gender;
                        $scope.editContact['date_of_birth'] = contactToEdit['date_of_birth'];
                        $scope.editContact['remarks'] = contactToEdit.remarks;
                        
                        //phone
                        if(contactToEdit.phone.length != 0) {
                            //$scope.editPhone = contactToEdit.phone;
                            var phoneEdit = contactToEdit.phone;
                            $scope.editPhone['country_code'] = phoneEdit['country_code'];
                            $scope.editPhone['phone_number'] = phoneEdit['phone'];
                            $scope.editPhone['phone_remarks'] = phoneEdit['remarks'];
                            $scope.editPhone['date_obsolete'] = phoneEdit['date_obsolete'];
                            $scope.editPhone['created_by'] = phoneEdit['created_by'];
                            $scope.editPhone['date_created'] = phoneEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editPhone = '';
                        }
                        //email
                        if(contactToEdit.email.length != 0) {
                            //$scope.editEmail = contactToEdit.email;
                            var emailEdit = contactToEdit.email;
                            $scope.editEmail['email'] = emailEdit['email'];
                            $scope.editEmail['email_remarks'] = emailEdit['remarks'];
                            $scope.editEmail['date_obsolete'] = emailEdit['date_obsolete'];
                            $scope.editEmail['created_by'] = emailEdit['created_by'];
                            $scope.editEmail['date_created'] = emailEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editEmail = '';
                        }
                        //address
                        if(contactToEdit.address.length != 0) {
                            //$scope.editAddress = contactToEdit.address;
                            var addressEdit = contactToEdit.address;
                            $scope.editAddress['country'] = addressEdit['country'];
                            $scope.editAddress['address'] = addressEdit['address'];
                            $scope.editAddress['zipcode'] = addressEdit['zipcode'];
                            $scope.editAddress['address_remarks'] = addressEdit['remarks'];
                            $scope.editAddress['date_obsolete'] = addressEdit['date_obsolete'];
                            $scope.editAddress['created_by'] = addressEdit['created_by'];
                            $scope.editAddress['date_created'] = addressEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editAddress = '';
                        }
                        //membership
                        if(contactToEdit.membership.length != 0) {
                            //$scope.editMembership = contactToEdit.membership;
                            var membershipEdit = contactToEdit.membership;
                            //to be modified
                            $scope.editMembership['membership_id'] = membershipEdit['id'];
                            $scope.editMembership['start_membership'] = membershipEdit['start_date'];
                            $scope.editMembership['end_membership'] = membershipEdit['end_date'];
                            $scope.editMembership['receipt_date'] = membershipEdit['receipt_date'];
                            $scope.editMembership['subscription_amount'] = membershipEdit['subscription_amount'];
                            $scope.editMembership['ext_transaction_ref'] = membershipEdit['ext_transaction_ref'];
                            $scope.editMembership['receipt_number'] = membershipEdit['receipt_number'];
                            $scope.editMembership['remarks'] = membershipEdit['remarks'];
                            $scope.editMembership['receipt_mode'] = membershipEdit['receipt_mode_name'];
                            $scope.editMembership['explain_if_other_receipt'] = membershipEdit['explain_if_other_receipt'];
                            $scope.editMembership['membership_class'] = membershipEdit['membership_class_name'];
                            $scope.editMembership['explain_if_other_class'] = membershipEdit['explain_if_other_class'];
                            $scope.editMembership['payment_mode'] = membershipEdit['payment_mode_name'];
                            $scope.editMembership['explain_if_other_payment'] = membershipEdit['explain_if_other_payment'];
                            $scope.editMembership['created_by'] = membershipEdit['created_by'];
                            $scope.editMembership['date_created'] = membershipEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editMembership = '';
                        }
                        //office held
                        if(contactToEdit['office_held'].length != 0) {
                            //$scope.editOfficeHeld = contactToEdit['office_held'];
                            var officeHeldEdit = contactToEdit['office_held'];
                            $scope.editOfficeHeld['office_held_name'] = officeHeldEdit['office_held'];
                            $scope.editOfficeHeld['start_office'] = officeHeldEdit['start_office'];
                            $scope.editOfficeHeld['end_office'] = officeHeldEdit['end_office'];
                            $scope.editOfficeHeld['remarks'] = officeHeldEdit['remarks'];
                            $scope.editOfficeHeld['created_by'] = officeHeldEdit['created_by'];
                            $scope.editOfficeHeld['date_created'] = officeHeldEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editOfficeHeld = '';
                        }
                        //donation
                        if(contactToEdit.donation.length != 0) {
                            //$scope.editDonation = contactToEdit.donation;
                            var donationEdit = contactToEdit.donation;
                            $scope.editDonation['donation_id'] = donationEdit['donation_id'];
                            $scope.editDonation['date_received'] = donationEdit['date_received'];
                            $scope.editDonation['donation_amount'] = donationEdit['donation_amount'];
                            $scope.editDonation['payment_mode'] = donationEdit['payment_mode'];
                            $scope.editDonation['explain_if_other_payment'] = donationEdit['explain_if_other_payment'];
                            $scope.editDonation['ext_transaction_ref'] = donationEdit['ext_transaction_ref'];
                            $scope.editDonation['receipt_number'] = donationEdit['receipt_number'];
                            $scope.editDonation['receipt_date'] = donationEdit['receipt_date'];
                            $scope.editDonation['receipt_mode'] = donationEdit['receipt_mode_name'];
                            $scope.editDonation['explain_if_other_receipt'] = donationEdit['explain_if_other_receipt'];
                            $scope.editDonation['donor_instruction'] = donationEdit['donor_instructions'];
                            $scope.editDonation['allocation1'] = donationEdit['allocation1'];
                            $scope.editDonation['subamount1'] = donationEdit['subtotal1'];
                            $scope.editDonation['allocation2'] = donationEdit['allocation2'];
                            $scope.editDonation['subamount2'] = donationEdit['subtotal2'];
                            $scope.editDonation['allocation3'] = donationEdit['allocation3'];
                            $scope.editDonation['subamount3'] = donationEdit['subtotal3'];
                            $scope.editDonation['associated_occasion'] = donationEdit['associated_occasion'];
                            $scope.editDonation['remarks'] = donationEdit['remarks'];
                            $scope.editDonation['created_by'] = donationEdit['created_by'];
                            $scope.editDonation['date_created'] = donationEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editDonation = '';
                        }
                        //team join
                        if(contactToEdit['team_join'].length != 0) {
                            //$scope.editTeamJoin = contactToEdit['team_join'];
                            var teamjoinEdit = contactToEdit['team_join'];
                            $scope.editTeamJoin['team'] = teamjoinEdit['team_name'];
                            $scope.editTeamJoin['permission_level'] = teamjoinEdit['permission'];
                            $scope.editTeamJoin['explain_if_other'] = teamjoinEdit['explain_if_others'];
                            $scope.editTeamJoin['subteam'] = teamjoinEdit['sub_team'];
                            $scope.editTeamJoin['date_obsolete'] = teamjoinEdit['date_obsolete'];
                            $scope.editTeamJoin['remarks'] = teamjoinEdit['remarks'];
                            $scope.editTeamJoin['created_by'] = teamjoinEdit['created_by'];
                            $scope.editTeamJoin['date_created'] = teamjoinEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editTeamJoin = '';
                        }
                        //training
                        if(contactToEdit.hasOwnProperty('training')) {
                            if(contactToEdit.training.length != 0) {
                                $scope.editTraining = contactToEdit.training;
                            } else {
                                $scope.editTraining = '';
                            }
                        } else {
                            $scope.editTraining = '';
                        }
                        //appreciation
                        if(contactToEdit.appreciation.length != 0) {
                            //$scope.editAppreciation = contactToEdit.appreciation;
                            var appreciationEdit = contactToEdit.appreciation;
                            $scope.editAppreciation['appreciation_id'] = appreciationEdit['appreciation_id'];
                            $scope.editAppreciation['appraisal_comment'] = appreciationEdit['appraisal_comments'];
                            $scope.editAppreciation['appraisal_by'] = appreciationEdit['appraisal_by'];
                            $scope.editAppreciation['appraisal_date'] = appreciationEdit['appraisal_date'];
                            $scope.editAppreciation['appreciation_gesture'] = appreciationEdit['appreciation_gesture'];
                            $scope.editAppreciation['appreciation_by'] = appreciationEdit['appreciation_by'];
                            $scope.editAppreciation['appreciation_date'] = appreciationEdit['appreciation_date'];
                            $scope.editAppreciation['remarks'] = appreciationEdit['remarks'];
                            $scope.editAppreciation['created_by'] = appreciationEdit['created_by'];
                            $scope.editAppreciation['date_created'] = appreciationEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editAppreciation = '';
                        }
                        //proxy
                        if(contactToEdit.proxy.length != 0) {
                            //$scope.editProxy = contactToEdit.proxy;
                            var proxyEdit = contactToEdit.proxy;
                            $scope.editProxy['proxy_of'] = proxyEdit['proxy_name'];
                            $scope.editProxy['principal_of'] = proxyEdit['principal_name'];
                            $scope.editProxy['proxy_standing'] = proxyEdit['proxy_standing'];
                            $scope.editProxy['remarks'] = proxyEdit['remarks'];
                            $scope.editProxy['date_obsolete'] = proxyEdit['date_obsolete'];
                            $scope.editProxy['created_by'] = proxyEdit['created_by'];
                            $scope.editProxy['date_created'] = proxyEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editProxy = '';
                        }
                        //languages
                        if(contactToEdit['language_assignment'].length != 0) {
                            //$scope.editLanguages = contactToEdit['language_assignment'];
                            var languageEdit = contactToEdit['language_assignment'];
                            $scope.editLanguages['language'] = languageEdit['language_name'];
                            $scope.editLanguages['speak_write'] = languageEdit['proficiency'];
                            $scope.editLanguages['explain_if_other'] = languageEdit['explain_if_other'];
                            $scope.editLanguages['remarks'] = languageEdit['remarks'];
                            $scope.editLanguages['date_obsolete'] = languageEdit['date_obsolete'];
                            $scope.editLanguages['created_by'] = languageEdit['created_by'];
                            $scope.editLanguages['date_created'] = languageEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editLanguages = '';
                        }
                        //skills and assets
                        if(contactToEdit['skill_assignment'].length != 0) {
                            //$scope.editSkillsAssets = contactToEdit['skill_assignment'];
                            var skillEdit = contactToEdit['skill_assignment'];
                            //to check again
                            $scope.editSkillsAssets['skill_asset'] = skillEdit['skill_name'];
                            $scope.editSkillsAssets['explain_if_other'] = skillEdit['explain_if_other'];
                            $scope.editSkillsAssets['remarks'] = skillEdit['remarks'];
                            $scope.editSkillsAssets['date_obsolete'] = skillEdit['date_obsolete'];
                            $scope.editSkillsAssets['created_by'] = skillEdit['created_by'];
                            $scope.editSkillsAssets['date_created'] = skillEdit['date_created'];
                            //might want to trim off the hour:minute:second
                        } else {
                            $scope.editSkillsAssets = '';
                        }
                        
                        //to be modified for generating password
                        $scope.generatePassword = function() {
                            
                        };
                        
//HTTP request to edit contact
                        //user
                        
                        //contact
                        
                        //phone
                        
                        //email
                        
                        //address
                        
                        //membership
                        
                        //office held
                        
                        //donation
                        
                        //team join
                        
                        //appreciation
                        
                        //proxy
                        
                        //languages
                        
                        //skills and assets
                        
                        
                    } else {
                        console.log("Some data went wrong");
                    }
                }, function (response) {
                    window.alert('Fail to connect to server');
                });

            }]);

