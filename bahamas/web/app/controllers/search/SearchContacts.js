/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('searchContacts', ['$scope', 'session', '$state', 'dataSubmit', 'loadTeamAffiliation', 'loadLanguage', 'loadLSAClass', '$timeout', 'ngDialog', function ($scope, session, $state, dataSubmit, loadTeamAffiliation, loadLanguage, loadLSAClass, $timeout, ngDialog) {
        var user = session.getSession('userType');
        $scope.isAuthorised = true;
        if(user === 'associate'){
            $scope.isAuthorised = false;
        };
        //FOR CONTACTS

        $scope.searchContact = {
            'name': '',
            'altname': '',
            'nationality': '',
            'team': '',
            'appreciation': '',
            'language': '',
            'skill': '',
            'ifOther': '',
            'if_location_other': '',
            'if_skill_other': '',
            'token': ''
        };

        $scope.retrieveTeamList = function () {
            loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                $scope.teamAffiliationList = response.data.teamAffiliationList;
                $scope.teamAffiliationList.unshift({teamAffiliation: ''})
            });
        };

        $scope.setTeamOther = function () {
            if ($scope.searchContact.team != 'Other') {
                $scope.searchContact.ifOther = '';
            };
        };
        
        $scope.setLanguageOther = function () {
            if ($scope.searchContact.language != 'Other') {
                $scope.searchContact.if_language_other = '';
            };
        };
        
        $scope.setSkillOther = function () {
            if ($scope.searchContact.skill != 'Other') {
                $scope.searchContact.if_skill_other = '';
            };
        };

        $scope.retrieveLanguageList = function () {
            loadLanguage.retrieveLanguage().then(function (response) {
                $scope.languageList = response.data.languageList;
                $scope.languageList.unshift({language: ''});
            });
        };

        $scope.retrieveLSAList = function () {
            loadLSAClass.retrieveLSAClass().then(function (response) {
                $scope.lsaList = response.data.lsaClassList;
                $scope.lsaList.unshift({lsaClass: ''});
            });
        };

        $scope.submitSearchContact = function () {
            $scope.searchContact.token = session.getSession('token');
            var url = '/contact.search';
            $scope.myPromise = dataSubmit.submitData($scope.searchContact, url).then(function (response) {
                if (response.data.message == "success") {
                    $scope.returnContacts = response.data.contact;
                } else {
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/searchFailed.html',
                        className: 'ngdialog-theme-default'
                    });
                };
            });
        };

        $scope.goToContact = function ($event, rc) {
            var toURL = user + ".viewIndivContact";
            var contactCid = rc.contactid;
            session.setSession('contactToDisplayCid', contactCid);
            $state.go(toURL);
        };
    }]);