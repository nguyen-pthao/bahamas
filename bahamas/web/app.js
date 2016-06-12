///* 
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
'use strict';

var app = angular.module('bahamas', ['ui.router', 'ngAnimate', 'ui.bootstrap']);

app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/login");

    $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'login.html',
                controller: 'loginController'
            })
            .state('admin', {
                url: '/admin',
                templateUrl: 'app/views/admin.html'
            })
            .state('user', {
                url: '/user',
                templateUrl: './app/views/user.html',
                controller: 'UserController'
            })
            .state('novice', {
                url: '/novice',
                templateUrl: 'app/views/novice.html',
            })
            .state('unauthorized', {
                url: '/unauthorized',
                templateUrl: 'unAuthorized.html'
            })
            .state('admin.addContact', {
                url: '/addContact',
                templateUrl: 'app/views/contact/createContact.html',
                controller: 'createContact'
            })
            .state('admin.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
            .state('admin.homepage', {
                url: '/homepage',
                templateUrl: 'app/views/homepage.html'
            })
            .state('admin.audit', {
                url: '/auditlog',
                templateUrl: 'app/views/auditlog/viewAuditLog.html',
                controller: 'viewAuditLog'
            })
            .state('novice.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
});

app.run(['$rootScope', '$location', 'session', '$state', function ($rootScope, $location, session, $state) {
        $rootScope.$on('$stateChangeStart', function (event, targetScope) {
            if (targetScope.url === '/novice' && (session.getSession('userType') !== 'novice')) {
                event.preventDefault();
                session.terminateSession();
                $state.go('login');
            }
            if (targetScope.url === '/admin' && (session.getSession('userType') !== 'admin')) {
                event.preventDefault();
                session.terminateSession();
                $state.go('login');
            }
        });
    }]);

