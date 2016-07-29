/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('profileCtrl', ['$scope', 'session', '$state', 'retrieveOwnContactInfo', function ($scope, session, $state, retrieveOwnContactInfo) {
        $scope.ownContact = angular.fromJson(session.getSession('contact'));

       
        var toRetrieve = {
            'token': session.getSession('token')
        };
        
        $scope.userType = session.getSession('userType');
        $scope.showEdit = true;
        if($scope.userType == 'novice'){
            $scope.showEdit = false;
        }
        
        $scope.myPromise = retrieveOwnContactInfo.retrieveContact(toRetrieve).then(function (response){
            $scope.contactInfo = response.data.contact;
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
            if($scope.membership != ''){
                for (var i = 0; i < $scope.membership.length; i++) {
                    $scope.membership[i]['subscription_amount'] = (Math.floor($scope.membership[i]['subscription_amount'] * 100) / 100).toFixed(2);
                }
            }
            
            //office held info
            $scope.officeHeld = $scope.contactInfo['office_held'];
            
            //donation info
            $scope.donation = $scope.contactInfo['donation'];
            if($scope.donation != ''){
                for (var i = 0; i < $scope.donation.length; i++) {
                    $scope.donation[i]['donation_amount'] = (Math.floor($scope.donation[i]['donation_amount'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal1'] = (Math.floor($scope.donation[i]['subtotal1'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal2'] = (Math.floor($scope.donation[i]['subtotal2'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal3'] = (Math.floor($scope.donation[i]['subtotal3'] * 100) / 100).toFixed(2);
                }
            }
            
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
        })
        
        var toAllContacts = $scope.userType + '.viewContacts';
        $scope.backHome = function () {
            $state.go($scope.userType);
        };

        $scope.viewContacts = function () {
            $state.go(toAllContacts);
        };

        $scope.editContact = function () {
            var toURL = $scope.userType + ".editContact";
            var contactCid = $scope.contactInfo.cid;
            session.setSession('contactToDisplayCid', contactCid);
            session.setSession('otherContact', 'false');
            $state.go(toURL);
        };
    }]);