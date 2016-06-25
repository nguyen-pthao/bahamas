/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('editContact', ['$scope', 'session', '$state', function ($scope, session, $state) {
        $scope.contact = session.getSession('contactToDisplay');
        $scope.permission = session.getSession('userType');

        $scope.isAuthorised = false;
        if ($scope.permission === 'admin') {
            $scope.isAuthorised = true;
        } else if ($scope.permission === 'teammanager') {
            $scope.isAuthorised = true;
        }

        $scope.contactToEdit = angular.fromJson($scope.contact);

        var toContact = $scope.permission + '.viewIndivContact';
        var homepage = $scope.permission + '.homepage';
        $scope.backHome = function () {
            $state.go(homepage);
        };

        $scope.viewContact = function () {
            $state.go(toContact);
        };
        
        //start declaring edit contact
        
        $scope.editUser = {
            password: ''
        };
        $scope.editContact = {
            
        };
        $scope.editPhone = {
            
        };
        $scope.editEmail = {
            
        };
        $scope.editAddress = {
            
        };
        $scope.editMembership = {
            
        };
        $scope.editOfficeHeld = {
            
        };
        $scope.editDonation = {
            
        };
        $scope.editTeamJoin = {
            
        };
        $scope.editTraining = {
            
        };
        $scope.editAppreciation = {
            
        };
        $scope.editProxy = {
            
        };
        $scope.editLanguages = {
            
        };
        $scope.editSkillsAssets = {
            
        };
    }]);

