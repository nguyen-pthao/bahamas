/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*Created by Marcus Ong*/

var app = angular.module('bahamas');

app.filter('verifiedFilter', function () {
    return function (text, length, end) {
        if (text) {
            return 'Yes';
        } else {
            return 'No';
        }
    };
});

app.controller('viewIndivContact', ['$scope', 'session', '$state', 'retrieveContactByCid', function ($scope, session, $state, retrieveContactByCid) {
        $scope.contactToDisplayCid = session.getSession('contactToDisplayCid');
        $scope.permission = session.getSession('userType');
        
        $scope.ownContactCid = angular.fromJson(session.getSession('contact')).cid;
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

        $scope.isAdmin = false;
        $scope.isTM = false;
        $scope.isEL = false;
        $scope.isAssociate = false;
        $scope.editable = false;
        $scope.permissionViewNricDob = false;
        if ($scope.permission === 'admin') {
            $scope.isAdmin = true;
            $scope.editable = true;
            $scope.permissionViewNricDob = true;
        } else if ($scope.permission === 'teammanager') {
            $scope.isTM = true;
            $scope.editable = true;
            $scope.permissionViewNricDob = true;
        } else if ($scope.permission === 'eventleader') {
            $scope.isEL = true;
            $scope.editable = true;
            $scope.permissionViewNricDob = false;
        } else if ($scope.permission === 'associate') {
            $scope.isAssociate = true;
            $scope.permissionViewNricDob = false;
        }

        var today = new Date();
        today.setDate(today.getDate() - 1);
        
        $scope.myPromise = retrieveContactByCid.retrieveContact(contactToRetrieve).then(function (response) {
            $scope.contactInfo = response.data.contact[0];
            //user info
            $scope.username = $scope.contactInfo.username;
            $scope.isUser = false;
            if ($scope.username != "") {
                if ($scope.permission === 'admin') {
                    $scope.isUser = true;
                    $scope.profile = $scope.contactInfo.profile_pic;
                }
            }

            //contact info
            $scope.dateCreated = $scope.contactInfo['date_created'];
            $scope.name = $scope.contactInfo['name'];
            $scope.cid = $scope.contactInfo['other_cid'];
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
            $scope.hasPhone = true;
            if (angular.isUndefined($scope.phoneInfo)) {
                $scope.hasPhone = false;
            } else {
                if($scope.phoneInfo != '') {
                    for(var i in $scope.phoneInfo) {
                        $scope.phoneInfo[i].isObsolete = false;
                        var dateObs = new Date($scope.phoneInfo[i].date_obsolete);
                        if(dateObs < today) {
                            $scope.phoneInfo[i].isObsolete = true;
                        }
                    }
                }
            }

            //email info
            $scope.email = $scope.contactInfo['email'];
            $scope.hasEmail = true;
            if (angular.isUndefined($scope.email)) {
                $scope.hasEmail = false;
            } else {
                if($scope.email != '') {
                    for(var i in $scope.email) {
                        $scope.email[i].isObsolete = false;
                        var dateObs = new Date($scope.email[i].date_obsolete);
                        if(dateObs < today) {
                            $scope.email[i].isObsolete = true;
                        }
                    }
                }
            }

            //address info
            $scope.address = $scope.contactInfo['address'];
            $scope.hasAddress = true;
            if (angular.isUndefined($scope.address)) {
                $scope.hasAddress = false;
            } else {
                if($scope.address != '') {
                    for(var i in $scope.address) {
                        $scope.address[i].isObsolete = false;
                        var dateObs = new Date($scope.address[i].date_obsolete);
                        if(dateObs < today) {
                            $scope.address[i].isObsolete = true;
                        }
                    }
                }
            }
            
            //membership info
            $scope.membership = $scope.contactInfo['membership'];
            $scope.hasMembership = true;
            if (angular.isUndefined($scope.membership)) {
                $scope.hasMembership = false;
            } else {
                for (var i = 0; i < $scope.membership.length; i++) {
                    $scope.membership[i]['subscription_amount'] = (Math.floor($scope.membership[i]['subscription_amount'] * 100) / 100).toFixed(2);
                }
            }
            
            //office held info
            $scope.officeHeld = $scope.contactInfo['office_held'];
            $scope.hasOffice = true;
            if (angular.isUndefined($scope.officeHeld)) {
                $scope.hasOffice = false;
            }
            //donation info
            $scope.donation = $scope.contactInfo['donation'];
            $scope.hasDonation = true;
            if (angular.isUndefined($scope.donation)) {
                $scope.hasDonation = false;
            }
            else{
                for (var i = 0; i < $scope.donation.length; i++) {
                    $scope.donation[i]['donation_amount'] = (Math.floor($scope.donation[i]['donation_amount'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal1'] = (Math.floor($scope.donation[i]['subtotal1'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal2'] = (Math.floor($scope.donation[i]['subtotal2'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal3'] = (Math.floor($scope.donation[i]['subtotal3'] * 100) / 100).toFixed(2);
                }
            }
            //team info
            $scope.team = $scope.contactInfo['team_join'];
            $scope.hasTeam = true;
            if (angular.isUndefined($scope.team)) {
                $scope.hasTeam = false;
            } else {
                if($scope.team != '') {
                    for(var i in $scope.team) {
                        $scope.team[i].isObsolete = false;
                        var dateObs = new Date($scope.team[i].date_obsolete);
                        if(dateObs < today) {
                            $scope.team[i].isObsolete = true;
                        }
                    }
                }
            }
            //training info
            $scope.training = $scope.contactInfo['training'];
            $scope.hasTraining = true;
            if (angular.isUndefined($scope.training)) {
                $scope.hasTraining = false;
            }

            //appreciation info
            $scope.appreciation = $scope.contactInfo['appreciation'];
            $scope.hasAppreciation = true;
            if (angular.isUndefined($scope.appreciation)) {
                $scope.hasAppreciation = false;
            }
            //proxy info
            $scope.proxy = $scope.contactInfo['proxy'];
            $scope.hasProxy = true;
            if (angular.isUndefined($scope.proxy)) {
                $scope.hasProxy = false;
            } else {
                if($scope.proxy != '') {
                    for(var i in $scope.proxy) {
                        $scope.proxy[i].isObsolete = false;
                        var dateObs = new Date($scope.proxy[i].date_obsolete);
                        if(dateObs < today) {
                            $scope.proxy[i].isObsolete = true;
                        }
                    }
                }
            }
            
            //languages info
            $scope.languages = $scope.contactInfo['language_assignment'];
            $scope.hasLanguages = true;
            if (angular.isUndefined($scope.languages)) {
                $scope.hasLanguages = false;
            } else {
                if($scope.languages != '') {
                    for(var i in $scope.languages) {
                        $scope.languages[i].isObsolete = false;
                        var dateObs = new Date($scope.languages[i].date_obsolete);
                        if(dateObs < today) {
                            $scope.languages[i].isObsolete = true;
                        }
                    }
                }
            }
            
            //skills and assets info
            $scope.skills = $scope.contactInfo['skill_assignment'];
            $scope.hasSkills = true;
            if (angular.isUndefined($scope.skills)) {
                $scope.hasSkills = false;
            } else {
                if($scope.skills != '') {
                    for(var i in $scope.skills) {
                        $scope.skills[i].isObsolete = false;
                        var dateObs = new Date($scope.skills[i].date_obsolete);
                        if(dateObs < today) {
                            $scope.skills[i].isObsolete = true;
                        }
                    }
                }
            }
        });

        var toAllContacts = $scope.permission + '.viewContacts';
        var editContact = $scope.permission + '.editContact';
        $scope.backHome = function () {
            $state.go($scope.permission);
        };

        $scope.viewContacts = function () {
            $state.go(toAllContacts);
        };
        $scope.userViewContacts = $scope.permission + "/" + 'viewContacts';
        $scope.userViewContact = $scope.permission + "/" + 'viewIndivContact';
        $scope.editContact = function () {
            session.setSession('otherContact', 'true');
            $state.go(editContact);
        };
    }]);
