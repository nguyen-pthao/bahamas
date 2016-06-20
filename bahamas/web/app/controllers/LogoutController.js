/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Created by Marcus Ong


var app = angular.module('bahamas');

app.controller('logoutController', ['$scope', '$location', 'session', '$state', 'ngDialog', function ($scope, $location, session, $state, ngDialog) {
        $scope.logoutUser = function () {
            ngDialog.openConfirm({
                template: './style/ngTemplate/logoutPrompt.html',
                className: 'ngdialog-theme-default dialog-logout-prompt'
            }).then(function (response) {
                session.terminateSession();
                $state.go('login');
            });
        }
    }]);