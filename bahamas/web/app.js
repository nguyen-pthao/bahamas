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
            .state('admin.homepage', {
                url: '/homepage',
                templateUrl: 'app/views/homepage.html'
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
            .state('admin.audit', {
                url: '/auditlog',
                templateUrl: 'app/views/auditlog/viewAuditLog.html',
                controller: 'viewAuditLog'
            })
            .state('novice', {
                url: '/novice',
                templateUrl: 'app/views/novice.html',
            })
            .state('novice.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
            .state('associate', {
                url: '/associate',
                templateUrl: 'app/views/associate.html'
            })
            .state('associate.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
            .state('teammanager', {
                url: '/teammanager',
                templateUrl: 'app/views/teammanager.html'
            })
            .state('tm.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
            .state('tm.addContact', {
                url: '/addContact',
                templateUrl: 'app/views/contact/createContact.html',
                controller: 'createContact'
            })
            .state('eventleader', {
                url: '/eventleader',
                templateUrl: 'app/views/eventleader.html'
            })
            .state('el.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
            .state('unauthorised', {
                url: '/unauthorised',
                templateUrl: 'unauthorised.html'
            });

});

app.run(['$rootScope', '$location', 'session', '$state', function ($rootScope, $location, session, $state) {
        $rootScope.$on('$stateChangeStart', function (event, targetScope) {
            if (targetScope.url === '/novice' && (session.getSession('userType') !== 'novice')) {
                event.preventDefault();
//                session.terminateSession();
                $state.go('unauthorised');
            }
            if (targetScope.url === '/admin' && (session.getSession('userType') !== 'admin')) {
                event.preventDefault();
//                session.terminateSession();
                $state.go('unauthorised');
            }
        });
    }]);

