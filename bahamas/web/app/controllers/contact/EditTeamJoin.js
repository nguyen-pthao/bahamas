/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditTeamJoin', ['$scope', 'session', 'ngDialog', '$timeout', 'dataSubmit', 'deleteService',
    function ($scope, session, ngDialog, $timeout, dataSubmit, deleteService) {

        //team join
        $scope.addingTeam = false;
        $scope.addNewTeam = function () {
            $scope.addingTeam = !$scope.addingTeam;
        };
        $scope.resultTeam = {
            'team_name': '',
            status: false,
            message: ''
        };
        $scope.editTheTeam = function ($event, team) {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            if ($scope.editMode == 'true') {
                datasend['contact_id'] = $scope.contactToEditCID;
            } else {
                datasend['contact_id'] = $scope.contactToEditCID;
            }
            datasend['user_type'] = session.getSession('userType');
            datasend['team'] = team['team_name'];
            datasend['permission_level'] = team['permission'];
            datasend['explain_if_other'] = team['explain_if_others'];
            datasend['subteam'] = team['sub_team'];
            if (team['date_obsolete'] == null) {
                datasend['date_obsolete'] = '';
            } else if (isNaN(team['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else if (angular.isUndefined(team['date_obsolete'])) {
                datasend['date_obsolete'] = '';
            } else {
                datasend['date_obsolete'] = team['date_obsolete'].valueOf() + "";
            }
            datasend['remarks'] = team['remarks'];
            var url = AppAPI.updateTeamJoin;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultTeam.status = true;
                if (response.data.message == 'success') {
                    $scope.resultTeam['team_name'] = datasend['team'];
                    $scope.resultTeam.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    $scope.form.editTeamForm.$setValidity();
                } else {
                    if (Array.isArray(response.data.message)) {
                        $scope.errorMessages = response.data.message;
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/errorMessage.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        })
                    } else {
                        $scope.errorMessages = [];
                        $scope.errorMessages.push(response.data.message);
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/errorMessage.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        })
                    }
                }
            }, function () {
                window.alert("Fail to send request!");
            });
        };
        $scope.deleteTheTeam = function ($event, team) {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteTeamjoin = {};
                deleteTeamjoin['token'] = session.getSession('token');
                if ($scope.editMode == 'true') {
                    deleteTeamjoin['contact_id'] = $scope.contactToEditCID;
                } else {
                    deleteTeamjoin['contact_id'] = $scope.contactToEditCID;
                }
                deleteTeamjoin['team'] = team['team_name'];
                var url = AppAPI.deleteTeamJoin;
                deleteService.deleteDataService(deleteTeamjoin, url).then(function (response) {
                    if (response.data.message == 'success') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete teamjoin");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        $scope.newTeam = {
            'token': session.getSession('token'),
            'contact_id': -1,
            'user_type': session.getSession('userType'),
            'team': '',
            'permission_level': '',
            'explain_if_other': '',
            'subteam': '',
            'date_obsolete': '',
            'remarks': ''
        };
        //For team preference
        $scope.teamPref = {
            team1: '',
            team2: '',
            team3: ''
        };
        var teamPreference = {
            token: session.getSession('token'),
            'contact_id': $scope.contactToEditCID,
            'user_type': session.getSession('userType'),
            team: '',
            'permission_level': '',
            'explain_if_other': '',
            'subteam': '',
            'date_obsolete': '',
            'remarks': ''
        };
//DEFINE TEAM LISTS
//watch for change in team list 1
        $scope.teamAffiliationList1 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
        $scope.$watch('teamPref.team1', function () {
            if ($scope.teamPref.team1 !== '') {
                var choice = $scope.teamPref.team1;
                var position = -1;
                for (var i in $scope.teamAffiliationList0) {
                    var teamCheck = $scope.teamAffiliationList0[i];
                    if (teamCheck.teamAffiliation == $scope.teamPref.team1) {
                        position = i;
                    }
                }
                if (position == -1) {
                    $scope.teamAffiliationList1 = angular.copy($scope.teamAffiliationList0);
                } else {
                    var list = angular.copy($scope.teamAffiliationList0);
                    list.splice(position, 1);
                    $scope.teamAffiliationList1 = list;
                    if ($scope.teamPref.team3 != '' && choice == $scope.teamPref.team3) {
                        var list2 = angular.copy($scope.teamAffiliationList1);
                        list2.splice(position, 1);
                        $scope.teamAffiliationList2 = list2;
                    }
                }
            } else {
                $scope.teamAffiliationList1 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
                $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
            }
        });
//watch for change in team list 2
        $scope.teamAffiliationList2 = [{teamAffiliation: '-------------------------------------------------'}, {teamAffiliation: '[Please choose the above option first.]'}];
        $scope.$watch('teamPref.team2', function () {
            if ($scope.teamPref.team2 !== '') {
                var position = -1;
                for (var i in $scope.teamAffiliationList1) {
                    var teamCheck = $scope.teamAffiliationList1[i];
                    if (teamCheck.teamAffiliation == $scope.teamPref.team2) {
                        position = i;
                    }
                }
                if (position == -1) {
                    $scope.teamAffiliationList2 = angular.copy($scope.teamAffiliationList1);
                } else {
                    var list = angular.copy($scope.teamAffiliationList1);
                    list.splice(position, 1);
                    $scope.teamAffiliationList2 = list;
                }
            }
        });
        $scope.copyTeam = angular.copy($scope.newTeam);
        $scope.copyTeamPref = angular.copy($scope.teamPref);
        $scope.submitNewTeam = {
            'submittedTeam': false,
            'message': ''
        };
        $scope.addTeam = function () {
            var url = AppAPI.addTeamJoin;
            $scope.newTeam['contact_id'] = $scope.contactToEditCID;
            if ($scope.editMode == 'true') {
                dataSubmit.submitData($scope.newTeam, url).then(function (response) {
                    if (response.data.message == 'success') {
                        $scope.submitNewTeam.submittedTeam = true;
                        $scope.submitNewTeam.message = $scope.successMsg;
                        $scope.retrieveFunc();
                        $scope.newTeam = angular.copy($scope.copyTeam);
                        $scope.form.editTeamForm.$setValidity();
                        $timeout(function () {
                            $scope.submitNewTeam.submittedTeam = false;
                        }, 1000);
                        //can set $scope.addingPhone = false if wanting to hide
                        $scope.addingTeam = false;
                    } else {
                        if (Array.isArray(response.data.message)) {
                            $scope.errorMessages = response.data.message;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        } else {
                            $scope.errorMessages = [];
                            $scope.errorMessages.push(response.data.message);
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        }
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            } else {
                teamPreference['contact_id'] = $scope.contactToEditCID;
                teamPreference.team = $scope.teamPref.team1;
                dataSubmit.submitData(teamPreference, url).then(function (response) {
                    if (response.data.message == 'success') {
                        if ($scope.teamPref.team2 != '') {
                            teamPreference.team = $scope.teamPref.team2;
                            dataSubmit.submitData(teamPreference, url).then(function (response) {
                                if (response.data.message == 'success') {
                                    if ($scope.teamPref.team3 != '') {
                                        teamPreference.team = $scope.teamPref.team3;
                                        dataSubmit.submitData(teamPreference, url).then(function (response) {
                                            if (response.data.message = 'success') {
                                                $scope.submitNewTeam.submittedTeam = true;
                                                $scope.submitNewTeam.message = $scope.successMsg;
                                                $scope.retrieveFunc();
                                                $scope.teamPref = angular.copy($scope.copyTeamPref);
                                                $scope.form.editTeamForm.$setValidity();
                                                $timeout(function () {
                                                    $scope.submitNewTeam.submittedTeam = false;
                                                }, 1000);
                                                $scope.addingTeam = false;
                                            } else {
                                                if (Array.isArray(response.data.message)) {
                                                    $scope.errorMessages = response.data.message;
                                                    ngDialog.openConfirm({
                                                        template: './style/ngTemplate/errorMessage.html',
                                                        className: 'ngdialog-theme-default',
                                                        scope: $scope
                                                    });
                                                } else {
                                                    $scope.errorMessages = [];
                                                    $scope.errorMessages.push(response.data.message);
                                                    ngDialog.openConfirm({
                                                        template: './style/ngTemplate/errorMessage.html',
                                                        className: 'ngdialog-theme-default',
                                                        scope: $scope
                                                    });
                                                }
                                            }
                                        }, function () {
                                            window.alert("Fail to send request!");
                                        });
                                    } else {
                                        $scope.submitNewTeam.submittedTeam = true;
                                        $scope.submitNewTeam.message = $scope.successMsg;
                                        $scope.retrieveFunc();
                                        $scope.teamPref = angular.copy($scope.copyTeamPref);
                                        $scope.form.editTeamForm.$setValidity();
                                        $timeout(function () {
                                            $scope.submitNewTeam.submittedTeam = false;
                                        }, 1000);
                                        $scope.addingTeam = false;
                                    }
                                } else {
                                    if (Array.isArray(response.data.message)) {
                                        $scope.errorMessages = response.data.message;
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/errorMessage.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        });
                                    } else {
                                        $scope.errorMessages = [];
                                        $scope.errorMessages.push(response.data.message);
                                        ngDialog.openConfirm({
                                            template: './style/ngTemplate/errorMessage.html',
                                            className: 'ngdialog-theme-default',
                                            scope: $scope
                                        });
                                    }
                                }
                            }, function () {
                                window.alert("Fail to send request!");
                            });
                        } else {
                            $scope.submitNewTeam.submittedTeam = true;
                            $scope.submitNewTeam.message = $scope.successMsg;
                            $scope.retrieveFunc();
                            $scope.teamPref = angular.copy($scope.copyTeamPref);
                            $scope.form.editTeamForm.$setValidity();
                            $timeout(function () {
                                $scope.submitNewTeam.submittedTeam = false;
                            }, 1000);
                            $scope.addingTeam = false;
                        }
                    } else {
                        if (Array.isArray(response.data.message)) {
                            $scope.errorMessages = response.data.message;
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        } else {
                            $scope.errorMessages = [];
                            $scope.errorMessages.push(response.data.message);
                            ngDialog.openConfirm({
                                template: './style/ngTemplate/errorMessage.html',
                                className: 'ngdialog-theme-default',
                                scope: $scope
                            });
                        }
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            }
        };
        
        $scope.openedTeamJoin = [];
        $scope.openTeamJoin = function (index) {
            $timeout(function () {
                $scope.openedTeamJoin[index] = true;
            });
        };

    }]);
