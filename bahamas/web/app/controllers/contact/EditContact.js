/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*Created by Marcus Ong*/

var app = angular.module('bahamas');

app.controller('editContact', 
    ['$scope', '$http', '$state', 'session', 'loadCountries', 'loadContactType', 'loadTeamAffiliation', 'loadPermissionLevel', 'loadLanguage', 'loadLSAClass', 'loadMembershipClass', 'loadPaymentMode', 'loadModeOfSendingReceipt', 'loadOfficeList',
        function ($scope, $http, $state, session, loadCountries, loadContactType, loadTeamAffiliation, loadPermissionLevel, loadLanguage, loadLSAClass, loadMembershipClass, loadPaymentMode, loadModeOfSendingReceipt, loadOfficeList) {
        $scope.contact = session.getSession('contactToDisplay');
        $scope.permission = session.getSession('userType');
//GET USER PERMISSION
        $scope.isAuthorised = false;
        if ($scope.permission === 'admin') {
            $scope.isAuthorised = true;
        } else if ($scope.permission === 'teammanager') {
            $scope.isAuthorised = true;
        }
//CONTACT TO BE EDITTED
        $scope.contactToEdit = angular.fromJson($scope.contact);
//PAGES TRANSITION
        var toContact = $scope.permission + '.viewIndivContact';
        var homepage = $scope.permission + '.homepage';
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
                
                $scope.loadOfficeHoldList = function() {
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
    
//DECLARE OBJECTS FOR EDIT CONTACT
    //user
        $scope.editUser['username'] = 'Thao Nguyen';
        $scope.editUser['password'] = 'NGUYEN NGUYEN';
    //contact
        $scope.editContact = {};
        $scope.editContact['date_created'] = '23 Jun 2015';
        $scope.editContact['created_by'] = '';
        $scope.editContact['name'] = '';
        $scope.editContact['alt_name'] = '';
        $scope.editContact['contact_type'] = '';
        $scope.editContact['other_explanation'] = '';
        $scope.editContact['profession'] = '';
        $scope.editContact['job_title'] = '';
        $scope.editContact['nric_fin'] = '';
        $scope.editContact['gender'] = '';
        $scope.editContact['dob'] = '';
        $scope.editContact['remarks'] = '';
    //phone
        $scope.editPhone = {};
        $scope.editPhone['date_created'] = '';
        $scope.editPhone['created_by'] = '';
        $scope.editPhone['country_code'] = '';
        $scope.editPhone['phone_number'] = '';
        $scope.editPhone['remarks'] = '';
        $scope.editPhone['date_obsolete'] = '';
    //email
        $scope.editEmail = {};
        $scope.editEmail['date_created'] = '';
        $scope.editEmail['created_by'] = '';
        $scope.editEmail['email'] = '';
        $scope.editEmail['remarks'] = '';
        $scope.editEmail['date_obsolete'] = '';
    //address
        $scope.editAddress = {};
        $scope.editAddress['date_created'] = '';
        $scope.editAddress['created_by'] = '';
        $scope.editAddress['address'] = '';
        $scope.editAddress['country'] = '';
        $scope.editAddress['zipcode'] = '';
        $scope.editAddress['remarks'] = '';
        $scope.editAddress['date_obsolete'] = '';
    //membership
        $scope.editMembership = {};
        $scope.editMembership['date_created'] = '';
        $scope.editMembership['created_by'] = '';
        $scope.editMembership['membership_type'] = '';
        $scope.editMembership['subscription_amount'] = '';
        $scope.editMembership['explain_if_other'] = '';
        $scope.editMembership['start_date'] = '';
        $scope.editMembership['end_date'] = '';
        $scope.editMembership['payment_mode'] = '';
        $scope.editMembership[''] = '';
    //office held
        $scope.editOfficeHeld = {};
    //donation
        $scope.editDonation = {};
    //team join
        $scope.editTeamJoin = {};
    //training
        $scope.editTraining = {};
    //appreciation
        $scope.editAppreciation = {};
    //proxy
        $scope.editProxy = {};
    //languages
        $scope.editLanguages = {};
    //skills and assets
        $scope.editSkillsAssets = {};
    }]);

