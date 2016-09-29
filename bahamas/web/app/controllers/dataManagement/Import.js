/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('import', ['$scope', 'session', '$state', 'Upload', function ($scope, session, $state, Upload) {
        
        $scope.backHome = function () {
            $state.go('admin');
        };
        
        $scope.selectedImport = 'contact';
        $scope.downloadFile = function() {
            console.log($scope.selectedImport);
            if($scope.selectedImport == 'contact') {
                window.open('https://rms.twc2.org.sg/template/contact.xlsx');
            } else if ($scope.selectedImport == 'phone') {
                window.open('https://rms.twc2.org.sg/template/phone.xlsx');
            } else if ($scope.selectedImport == 'email') {
                window.open('https://rms.twc2.org.sg/template/email.xlsx');
            } else if ($scope.selectedImport == 'address') {
                window.open('https://rms.twc2.org.sg/template/address.xlsx');
            } else if ($scope.selectedImport == 'membership') {
                window.open('https://rms.twc2.org.sg/template/membership.xlsx');
            } else if ($scope.selectedImport == 'officeheld') {
                window.open('https://rms.twc2.org.sg/template/officeheld.xlsx');
            } else if ($scope.selectedImport == 'donation') {
                window.open('https://rms.twc2.org.sg/template/donation.xlsx');
            } else if ($scope.selectedImport == 'appreciation') {
                window.open('https://rms.twc2.org.sg/template/appreciation.xlsx');
            } else if ($scope.selectedImport == 'teamjoin') {
                window.open('https://rms.twc2.org.sg/template/teamjoin.xlsx');
            } else if ($scope.selectedImport == 'language') {
                window.open('https://rms.twc2.org.sg/template/language.xlsx');
            } else if ($scope.selectedImport == 'skill') {
                window.open('https://rms.twc2.org.sg/template/skill.xlsx');
            } else if ($scope.selectedImport == 'training') {
                window.open('https://rms.twc2.org.sg/template/training.xlsx');
            }
        };

        var url = $scope.commonUrl + '/import';
        $scope.resultData = '';
        $scope.error = false;
        
        $scope.import = function (file) {
            console.log(file);
            if (!file.$error) {
                $scope.fileUpload = file;
                Upload.upload({
                    url: url,
                    data: {
                        token: session.getSession('token'),
                        file: file,
                        table: $scope.selectedImport
                    }
                }).then(function (response) {
                    console.log(response.data);
                    var result = response.data;
                    
                    if (result.hasOwnProperty('message')) {
                        $scope.resultData = result.message;
                        $scope.error = true;
                    } else {
                        $scope.resultData = result[$scope.selectedImport];
                    }
                }, function (response) {
                    window.alert('Fail to send request!');
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 *
                            evt.loaded / evt.total));
                });
            }
        };
        
    }]);
