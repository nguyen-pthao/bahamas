/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*Created by Marcus Ong*/

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
            $scope.contactInfo = response.data.contact[0];
            console.log($scope.contactInfo);
            //user info
            $scope.username = $scope.contactInfo.username;
            $scope.isUser = false;
            if($scope.username != ""){
                $scope.isUser = true;
            }
            //contact info
            $scope.dateCreated = $scope.contactInfo['date_created'];
            $scope.name = $scope.contactInfo['name'];
            $scope.altName = $scope.contactInfo['alt_name'];
            $scope.contactType = $scope.contactInfo['contact_type'];
            $scope.explainIfOther = $scope.contactInfo['explain_if_other'];
            $scope.profession = $scope.contactInfo['profession'];
            $scope.jobTitle = $scope.contactInfo['job_title'];
            $scope.nric = $scope.contactInfo['nric_fin'];
            $scope.gender = $scope.contactInfo['gender'];
            $scope.nationality = $scope.contactInfo['nationality'];
            $scope.dateOfBirth = $scope.contactInfo['date_of_birth'];
            $scope.remarks = $scope.contactInfo['remarks'];
            $scope.createdBy = $scope.contactInfo['created_by'];
            
            //phone info
            $scope.phoneInfo = $scope.contactInfo['phone'];
            
            //email info
            $scope.email = $scope.contactInfo['email'];
            
            //address info
            $scope.address = $scope.contactInfo['address'];
            
            //membership info
            $scope.membership = $scope.contactInfo['membership'];
            
            //office held info
            $scope.officeHeld = $scope.contactInfo['office_held'];
            
            //donation info
            $scope.donation = $scope.contactInfo['donation'];
            
            //team info
            $scope.team = $scope.contactInfo['team_join'];
            
            //training info
            //$scope.training = $scope.contactInfo['training'];
            
            //appreciation info
            $scope.appreciation = $scope.contactInfo['appreciation'];
            
            //proxy info
            $scope.proxy = $scope.contactInfo['proxy'];
            
            //languages info
            $scope.languages = $scope.contactInfo['language_assignment'];
            
            //skills and assets info
            $scope.skills = $scope.contactInfo['skill_assignment'];
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
