/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('tokenUpdate', ['$rootScope', '$http', '$location', '$timeout', 'ngDialog', 'localStorageService', function ($rootScope, $http, $location, $timeout, ngDialog, localStorageService) {
        var that = this;
        this.refreshToken = function (token) {
            var tokenToSend = {
                "token": token
            }
            localStorageService.set('tokenStorage', $timeout(function () {
                ngDialog.openConfirm({
                    template: './style/ngTemplate/refreshToken.html',
                    className: 'ngdialog-theme-default',
                    closeByDocument: false
                }).then(function (response) {
                    return $http({
                        method: 'POST',
                        url: $rootScope.commonUrl + '/token.update',
                        data: JSON.stringify(tokenToSend)
                    }).then(function (response) {
                        var newToken = response.data.token;
                        console.log(newToken + "in first");
                        that.anotherRefreshToken(newToken);
                    })
                });
            }, 15000))
        };

        that.anotherRefreshToken = function (token) {
            var tokenToSend = {
                "token": token
            }
            localStorageService.set('tokenStorage', $timeout(function () {
                ngDialog.openConfirm({
                    template: './style/ngTemplate/refreshToken.html',
                    className: 'ngdialog-theme-default',
                    closeByDocument: false
                }).then(function (response) {
                    return $http({
                        method: 'POST',
                        url: $rootScope.commonUrl + '/token.update',
                        data: JSON.stringify(tokenToSend)
                    }).then(function (response) {
                        var newToken = response.data.token;
                        console.log(newToken + "in another");
                        that.refreshToken(newToken);
                    })
                });
            }, 15000))

        }
    }]);
