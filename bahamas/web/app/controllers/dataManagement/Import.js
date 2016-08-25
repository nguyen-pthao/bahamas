/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('import', ['$scope', 'session', '$state', function ($scope, session, $state) {
        //to be modified

        var url = $scope.commonUrl + AppAPI.importContacts;
        $scope.import = function (file) {
            console.log(file);
            if (!file.$error) {
                $scope.fileUpload = file;
                Upload.upload({
                    url: url,
                    data: {
                        token: session.getSession('token'),
                        file: file
                    }
                }).then(function (response) {
                    console.log(response.data);
                }, function (response) {
                    window.alert('Fail to send request!');
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 *
                            evt.loaded / evt.total));
                });
            }
        };
    }]);
