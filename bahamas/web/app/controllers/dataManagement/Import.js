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
        
        //to be modified
        $scope.selectedImport = 'contact';
        $scope.downloadFile = function() {
            window.open('https://rms.twc2.org.sg/template/template.xlsx');
        };

        var url = $scope.commonUrl + AppAPI.importContacts;
        $scope.resultData = '';
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
                    
                    result = response.data;
                    $scope.showError = false;
                    if (result.hasOwnProperty('message')) {
                        $scope.resultData = result.message;
                        $scope.showError = true;
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
