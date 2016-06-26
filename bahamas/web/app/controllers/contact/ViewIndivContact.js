/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewIndivContact', ['$scope', 'session', '$state', 'retrieveContactByCid', function ($scope, session, $state, retrieveContactByCid) {
        $scope.contactToDisplayCid = session.getSession('contactToDisplayCid'); 
        $scope.permission = session.getSession('userType');
        $scope.ownContactCid = angular.fromJson(session.getSession('contact')).cid;
//        console.log($scope.ownContactCid);
        if (session.getSession('teams') === 'undefined') {
            var contactToRetrieve = {
                'token': session.getSession('token'),
                'cid': $scope.ownContactCid,
                'other_cid': $scope.contactToDisplayCid,
                'team_name': "",
                'permission': $scope.permission
            };
        } else {
            var contactToRetrieve = {
                'token': session.getSession('token'),
                'cid': $scope.ownContactCid,
                'other_cid': $scope.contactToDisplayCid,
                'team_name': angular.fromJson(session.getSession('teams'))[0].teamname,
                'permission': $scope.permission
            };
        }
        
        
        
        $scope.isAuthorised = false;
        if ($scope.permission === 'admin') {
            $scope.isAuthorised = true;
        } else if ($scope.permission === 'teammanager') {
            $scope.isAuthorised = true;
        }
        
        retrieveContactByCid.retrieveContact(contactToRetrieve).then(function (response){
            $scope.contactInfo = response;
            console.log($scope.contactInfo);
            
        });

        var toAllContacts = $scope.permission + '.viewContacts';
        var homepage = $scope.permission + '.homepage';
        var editContact = $scope.permission + '.editContact';
        $scope.backHome = function () {
            $state.go(homepage);
        };

        $scope.viewContact = function () {
            $state.go(toAllContacts);
        };

        $scope.editContact = function () {
            $state.go(editContact);
        };
    }]);
