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
                        $scope.sendReceitModList = response.data.mode;
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
                        $scope.editUser = {};
                        if ($scope.isAdmin) {
                            $scope.editUser['username'] = contactToEdit.username;
                            $scope.deactivated = contactToEdit.deactivated;
                        } else {
                            $scope.editUser['username'] = '';
                            $scope.deactivated = false;
                        }
                        //contact
                        $scope.editContact = {};
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
                            $scope.editPhone = contactToEdit.phone;
                        } else {
                            $scope.editPhone = '';
                        }
                        //email
                        if(contactToEdit.email.length != 0) {
                            $scope.editEmail = contactToEdit.email;
                        } else {
                            $scope.editEmail = '';
                        }
                        //address
                        if(contactToEdit.address.length != 0) {
                            $scope.editAddress = contactToEdit.address;
                        } else {
                            $scope.editAddress = '';
                        }
                        //membership
                        if(contactToEdit.membership.length != 0) {
                            $scope.editMembership = contactToEdit.membership;
                        } else {
                            $scope.editMembership = '';
                        }
                        //office held
                        if(contactToEdit['office_held'].length != 0) {
                            $scope.editOfficeHeld = contactToEdit['office_held'];
                        } else {
                            $scope.editOfficeHeld = '';
                        }
                        //donation
                        if(contactToEdit.donation.length != 0) {
                            $scope.editDonation = contactToEdit.donation;
                        } else {
                            $scope.editDonation = '';
                        }
                        //team join
                        if(contactToEdit['team_join'].length != 0) {
                            $scope.editTeamJoin = contactToEdit['team_join'];
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
                            $scope.editAppreciation = contactToEdit.appreciation;
                        } else {
                            $scope.editAppreciation = '';
                        }
                        //proxy
                        if(contactToEdit.proxy.length != 0) {
                            $scope.editProxy = contactToEdit.proxy;
                        } else {
                            $scope.editProxy = '';
                        }
                        //languages
                        if(contactToEdit['language_assignment'].length != 0) {
                            $scope.editLanguages = contactToEdit['language_assignment'];
                        } else {
                            $scope.editLanguages = '';
                        }
                        //skills and assets
                        if(contactToEdit['skill_assignment'].length != 0) {
                            $scope.editSkillsAssets = contactToEdit['skill_assignment'];
                        } else {
                            $scope.editSkillsAssets = '';
                        }
                    } else {
                        console.log("Some data went wrong");
                    }
                }, function (response) {
                    window.alert('Fail to connect to server');
                });

            }]);

