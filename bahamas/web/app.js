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
            .state('teammanager.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
            .state('teammanager.addContact', {
                url: '/addContact',
                templateUrl: 'app/views/contact/createContact.html',
                controller: 'createContact'
            })
            .state('eventleader', {
                url: '/eventleader',
                templateUrl: 'app/views/eventleader.html'
            })
            .state('eventleader.viewContacts', {
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
        $rootScope.commonUrl = 'http://localhost:8084/bahamas';
        $rootScope.$on('$stateChangeSuccess', function (event, targetScope) {
            var permission = $location.path().split('/')[1];
            if (targetScope.url === '/unauthorised') {
                //to stop the browser from endless looping
            } else {
                if (permission === 'novice' && (session.getSession('userType') !== 'novice')) {
                    event.preventDefault();
                    $state.go('unauthorised');
                }
                if (permission === 'admin' && (session.getSession('userType') !== 'admin')) {
                    event.preventDefault();
                    $state.go('unauthorised');
                }
                if (permission === 'teammanager' && (session.getSession('userType') !== 'teammanager')) {
                    event.preventDefault();
                    $state.go('unauthorised');
                }
                if (permission === 'eventleader' && (session.getSession('userType') !== 'eventleader')) {
                    event.preventDefault();
                    $state.go('unauthorised');
                }
                if (permission === 'associate' && (session.getSession('userType') !== 'associate')) {
                    event.preventDefault();
                    $state.go('unauthorised');
                }
                if (permission === 'login' && (session.getSession('userType') !== null)){
                    event.preventDefault();
                    $state.go(session.getSession('userType'));
                }
            }
        });
    }]);

