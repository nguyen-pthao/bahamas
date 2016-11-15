/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

var app = angular.module('registration', ['ui.router', 'ngAnimate', 'ngDialog', 'ui.bootstrap', 'cgBusy']);

app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/loginRegister");

    $stateProvider
            .state('loginRegister', {
                url: '/loginRegister',
                templateUrl: 'loginRegistration.html',
                controller: 'loginRegisterController'
            })
            .state('register', {
                url: '/register',
                templateUrl: 'registrationForm.html',
                controller: 'registrationController'
            })
            .state('register.contact', {
                url: '/contact',
                templateUrl: 'app/views/remoteRegistration/contact.html'
            })
            .state('register.phoneEmail', {
                url: '/phoneEmail',
                templateUrl: 'app/views/remoteRegistration/phoneEmail.html'
            })
            .state('register.languageSkill', {
                url: '/languageSkill',
                templateUrl: 'app/views/remoteRegistration/languageSkill.html'
            })
            .state('register.teamPreferences', {
                url: '/teamPreferences',
                templateUrl: 'app/views/remoteRegistration/teamPreferences.html'
            })
            .state('register.checkbox', {
                url: '/checkbox',
                templateUrl: 'app/views/remoteRegistration/checkbox.html'
            });
});

app.run(['$rootScope', 'session', '$state', function ($rootScope, session, $state) {
        $rootScope.$on('$stateChangeStart', function (event, targetScope, targetParams, fromScope, to, from) {
            var permission = targetScope.name.split('.')[0];
            
            if (permission == 'register' && session.getSession('formId') == null) {
                event.preventDefault();
                $state.go('loginRegister');
            }
        });
    }]);
