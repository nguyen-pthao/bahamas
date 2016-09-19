/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('EditUser', ['$scope', 'session', 'ngDialog', 'dataSubmit', 'Upload', '$state', 'deleteService',
    function ($scope, session, ngDialog, dataSubmit, Upload, $state, deleteService) {

        //Edit profile picture
        //Upload image uses third-party library and thus require full URL
        $scope.fileEmpty = false;
        $scope.$watch('file', function () {
            //console.log($scope.file);
            if (!angular.isUndefined($scope.file)) {
                $scope.fileEmpty = true;
            }
        });

        var url = $scope.commonUrl + AppAPI.uploadImage;
        $scope.upload = function (file) {
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
                    $scope.retrieveFunc();
                    $state.reload();
                }, function () {
                    window.alert('Fail to send request!');
                });
            }
        };
        
        $scope.removeProfile = function () {
            ngDialog.openConfirm({
                template: './style/ngTemplate/deletePrompt.html',
                className: 'ngdialog-theme-default',
                scope: $scope
            }).then(function (response) {
                var deleteProfile = {};
                deleteProfile.token = session.getSession('token');
                deleteProfile.contact_id = $scope.contactToEditCID;
                var url = AppAPI.deleteProfilePic;
                deleteService.deleteDataService(deleteProfile, url).then(function (response) {
                    if (response.data.message == 'Image successfully deleted!') {
                        ngDialog.openConfirm({
                            template: './style/ngTemplate/deleteSuccess.html',
                            className: 'ngdialog-theme-default',
                            scope: $scope
                        }).then(function () {
                            $scope.retrieveFunc();
                        });
                    } else {
                        window.alert("Fail to delete profile picture");
                    }
                }, function () {
                    window.alert("Fail to send request!");
                });
            });
        };
        
        //For generating password
        $scope.generatePassword = function () {
            var a = Math.floor((Math.random() * 10) + 10);
            $scope.editUser.password = Math.random().toString(36).substring(2, a);
        };
        //For changing password
        $scope.changePass = false;
        $scope.changePassword = function () {
            $scope.changePass = !$scope.changePass;
        };
        
        var permission = session.getSession('userType');
        $scope.resultUser = {
            status: false,
            message: ''
        };
        $scope.editTheUser = function () {
            var datasend = {};
            if ($scope.editMode == 'true') {
                if ($scope.isUser) {
                    if ($scope.isAdmin) {
                        datasend['token'] = session.getSession('token');
                        datasend['contact_id'] = $scope.editUser['contact_id']; //$scope.contactToEditCID;
                        datasend['deactivated'] = ($scope.editUser['deactivated'] === 'true');
                        datasend['is_admin'] = ($scope.editUser['is_admin'] === 'true');
                        datasend['user_type'] = permission;
                        submitUser(datasend, false);
                    }
                } else {
                    var dataToSend = {};
                    dataToSend['token'] = session.getSession('token');
                    dataToSend['contact_id'] = $scope.contactToEditCID;
                    dataToSend['username'] = $scope.editUser['username'];
                    dataToSend['password'] = $scope.editUser['password'];
                    dataToSend['email'] = $scope.editUser['email'];
                    dataToSend['user_type'] = permission;
                    submitUser(dataToSend, true);
                }
            } else {
                datasend['token'] = session.getSession('token');
                datasend['contact_id'] = $scope.editUser['contact_id']; //$scope.contactToEditCID;
                datasend['current_password'] = $scope.editUser['current_password'];
                datasend['password'] = $scope.editUser['password'];
                datasend['confirm_password'] = $scope.editUser['confirm_password'];
                datasend['user_type']= permission;
                submitUser(datasend, false);
                $scope.changePassword();
            }
        };
        //to update user info
        var submitUser = function (datasend, create) {
            var url = AppAPI.updateUser;
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultUser.status = true;
                if (response.data.message == 'success') {
                    $scope.resultUser.message = $scope.successMsg;
                    $scope.retrieveFunc();
                    if (create) {
                        $scope.isUser = true;
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
        };
    }]);
