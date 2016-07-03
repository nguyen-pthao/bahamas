/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('tokenUpdate', ['$rootScope', '$http', '$location', '$timeout', 'ngDialog', function ($rootScope, $http, $location, $timeout, ngDialog) {
//        this.refreshToken = function (token) {
//            $timeout(function () {
//                ngDialog.openConfirm({
//                    template: './style/ngTemplate/refreshToken.html',
//                    className: 'ngdialog-theme-default'
//                }).then(function (response) {
//                    return $http({
//                        method: 'POST',
//                        url: $rootScope.commonUrl + '/token.update',
//                        data: JSON.stringify(token)
//                    }).then(function (response){
//                        var newToken = response.data.token;
//                        console.log(newToken);
//                        tokenUpdate.refreshToken(newToken);
//                    })
//                });
//            }, 15000);
//            
//        };
    }]);
