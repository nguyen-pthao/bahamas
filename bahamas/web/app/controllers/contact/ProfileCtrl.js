/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('profileCtrl', ['$scope', 'session', '$state', 'retrieveOwnContactInfo', 'ngDialog', 'dataSubmit', 'Upload', 'deleteService', '$uibModal',
    function ($scope, session, $state, retrieveOwnContactInfo, ngDialog, dataSubmit, Upload, deleteService, $uibModal) {

        $scope.ownContact = angular.fromJson(session.getSession('contact'));

        var toRetrieve = {
            'token': session.getSession('token')
        };

        $scope.userType = session.getSession('userType');
        $scope.currentState = $scope.userType + '.profile';
        $scope.showEdit = true;
        if ($scope.userType == 'novice' || $scope.userType == 'associate') {
            $scope.showEdit = false;
        }

        var today = new Date();
        today.setDate(today.getDate() - 1);

        $scope.myPromise = retrieveOwnContactInfo.retrieveContact(toRetrieve).then(function (response) {
            $scope.contactInfo = response.data.contact;
            $scope.contact_id = $scope.contactInfo.cid;
            //user info
            $scope.username = $scope.contactInfo.username;
            $scope.profile = $scope.contactInfo.profile_pic;
            if ($scope.profile == '') {
                $scope.profile = 'images/default.jpg';
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
            if ($scope.phoneInfo != '') {
                for (var i in $scope.phoneInfo) {
                    $scope.phoneInfo[i].isObsolete = false;
                    var dateObs = new Date($scope.phoneInfo[i].date_obsolete);
                    if (dateObs < today) {
                        $scope.phoneInfo[i].isObsolete = true;
                    }
                }
            }

            //email info
            $scope.email = $scope.contactInfo['email'];
            if ($scope.email != '') {
                for (var i in $scope.email) {
                    $scope.email[i].isObsolete = false;
                    var dateObs = new Date($scope.email[i].date_obsolete);
                    if (dateObs < today) {
                        $scope.email[i].isObsolete = true;
                    }
                }
            }

            //address info
            $scope.address = $scope.contactInfo['address'];
            if ($scope.address != '') {
                for (var i in $scope.address) {
                    $scope.address[i].isObsolete = false;
                    var dateObs = new Date($scope.address[i].date_obsolete);
                    if (dateObs < today) {
                        $scope.address[i].isObsolete = true;
                    }
                }
            }

            //membership info
            $scope.membership = $scope.contactInfo['membership'];
            if ($scope.membership != '') {
                for (var i = 0; i < $scope.membership.length; i++) {
                    $scope.membership[i]['subscription_amount'] = (Math.floor($scope.membership[i]['subscription_amount'] * 100) / 100).toFixed(2);
                }
            }

            //office held info
            $scope.officeHeld = $scope.contactInfo['office_held'];

            //donation info
            $scope.donation = $scope.contactInfo['donation'];
            if ($scope.donation != '') {
                for (var i = 0; i < $scope.donation.length; i++) {
                    $scope.donation[i]['donation_amount'] = (Math.floor($scope.donation[i]['donation_amount'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal1'] = (Math.floor($scope.donation[i]['subtotal1'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal2'] = (Math.floor($scope.donation[i]['subtotal2'] * 100) / 100).toFixed(2);
                    $scope.donation[i]['subtotal3'] = (Math.floor($scope.donation[i]['subtotal3'] * 100) / 100).toFixed(2);
                }
            }

            //team info
            $scope.team = $scope.contactInfo['team_join'];
            if ($scope.team != '') {
                for (var i in $scope.team) {
                    $scope.team[i].isObsolete = false;
                    var dateObs = new Date($scope.team[i].date_obsolete);
                    if (dateObs < today) {
                        $scope.team[i].isObsolete = true;
                    }
                }
            }
            //training info
            $scope.training = $scope.contactInfo['training'];

            //languages info
            $scope.languages = $scope.contactInfo['language_assignment'];
            if ($scope.languages != '') {
                for (var i in $scope.languages) {
                    $scope.languages[i].isObsolete = false;
                    var dateObs = new Date($scope.languages[i].date_obsolete);
                    if (dateObs < today) {
                        $scope.languages[i].isObsolete = true;
                    }
                }
            }

            //skills and assets info
            $scope.skills = $scope.contactInfo['skill_assignment'];
            if ($scope.skills != '') {
                for (var i in $scope.skills) {
                    $scope.skills[i].isObsolete = false;
                    var dateObs = new Date($scope.skills[i].date_obsolete);
                    if (dateObs < today) {
                        $scope.skills[i].isObsolete = true;
                    }
                }
            }
        });

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

        $scope.changeProfile = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: './style/ngTemplate/profilePicture.html',
                scope: $scope,
                controller: function () {
                    //Edit profile picture
                    //Upload image uses third-party library and thus require full URL
                    $scope.fileEmpty = false;
                    $scope.file = $scope.profile;
                    $scope.$watch('file', function () {
                        if (!angular.isUndefined($scope.file)) {
                            $scope.fileEmpty = true;
                        }
                    });

                    var url = $scope.commonUrl + AppAPI.uploadImage;
                    $scope.upload = function (file) {
                        $scope.file = file;
                        Upload.imageDimensions(file)
                                .then(function (dimensions) {
                                    console.log(dimensions.width, dimensions.height);
                                });
                        console.log(file);
                        var img = new Image();
                        img = document.getElementById('myProfile');//URL.createObjectURL(file);
                        //console.log(img.src);
                        var canvas = document.createElement('canvas');

                        canvas.width = 400;
                        canvas.height = 400;
                        canvas.getContext('2d').drawImage(img, 0, 0, 400, 400);
                        var dataURL = canvas.toDataURL();
                        //console.log(dataURL);

                        var byteString;
                        if (dataURL.split(',')[0].indexOf('base64') >= 0) {
                            byteString = atob(dataURL.split(',')[1]);
                        } else {
                            byteString = unescape(dataURL.split(',')[1]);
                        }

                        var mimeString = dataURL.split(',')[0].split(':')[1].split(';')[0];

                        var ia = new Uint8Array(byteString.length);
                        for (var i = 0; i < byteString.length; i++) {
                            ia[i] = byteString.charCodeAt(i);
                        }
                        var file = new Blob([ia], {type: mimeString});
                        //console.log(file);
                        if (!file.$error) {
                            Upload.imageDimensions(file)
                                    .then(function (dimensions) {
                                        console.log(dimensions.width, dimensions.height);
                                    });
                            Upload.upload({
                                url: url,
                                data: {
                                    token: session.getSession('token'),
                                    image: file
                                }
                            }).then(function (response) {
                                var resetContact = angular.fromJson(session.getSession('contact'));
                                resetContact.profile_pic = response.data.image;
                                session.setSession('contact', angular.toJson(resetContact));
                                $state.go($scope.currentState, {}, {reload: true});
                            }, function () {
                                window.alert('Fail to send request!');
                            });
                        }
                        modalInstance.dismiss('cancel');
                    };

                    $scope.removeProfile = function () {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deletePrompt.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function (response) {
                            var deleteProfile = {};
                            deleteProfile.token = session.getSession('token');
                            deleteProfile.contact_id = $scope.contact_id;
                            var url = AppAPI.deleteProfilePic;
                            deleteService.deleteDataService(deleteProfile, url).then(function (response) {
                                if (response.data.message == 'Image successfully deleted!') {
                                    ngDialog.openConfirm({
                                        template: './style/ngTemplate/deleteSuccess.html',
                                        className: 'ngdialog-theme-default',
                                        scope: $scope
                                    }).then(function () {
                                        var resetContact = angular.fromJson(session.getSession('contact'));
                                        resetContact.profile_pic = '';
                                        session.setSession('contact', angular.toJson(resetContact));
                                        $state.go($scope.currentState, {}, {reload: true});
                                    });
                                } else {
                                    window.alert("Fail to delete profile picture");
                                }
                            }, function () {
                                window.alert("Fail to send request!");
                            });
                            modalInstance.dismiss('cancel');
                        });
                    };
                    
                    $scope.cancel = function () {
                        modalInstance.dismiss('cancel');
                    };
                },
                backdrop: 'static',
                keyboard: false,
                size: "md"
            });
        }
    }]);