/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('search', ['$scope', 'session', '$state', 'dataSubmit', 'loadTeamAffiliation', 'loadLanguage', 'loadLSAClass', function ($scope, session, $state, dataSubmit, loadTeamAffiliation, loadLanguage, loadLSAClass) {
        $scope.searchContact = {
            'name': '',
            'altname': '',
            'nationality': '',
            'team': '',
            'appreciation': '',
            'language': '',
            'skill': '',
            'ifOther': '',
            'token': ''
        };

        $scope.retrieveTeamList = function () {
            loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                $scope.teamAffiliationList = response.data.teamAffiliationList;
                $scope.teamAffiliationList.unshift({teamAffiliation: ''})
            });
        };

        $scope.setOther = function () {
            if ($scope.searchContact.team != 'Other') {
                $scope.searchContact.ifOther = '';
            }
            ;
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
            dataSubmit.submitData($scope.searchContact, url).then(function (response) {
                if (response.data.message == "success") {
                    $scope.returnContacts = response.data.contact;
                } else {
                    alert('Unable to establish connection!');
                }
                ;
            });
        };

        $scope.goToContact = function ($event, rc) {
            var toURL = $scope.userType + ".viewIndivContact";
            var contactCid = rc.contactid;
            session.setSession('contactToDisplayCid', contactCid);
            $state.go(toURL);
        };
    }]);