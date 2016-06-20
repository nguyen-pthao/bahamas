///* 
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
'use strict';

var app = angular.module('bahamas', ['ui.router', 'ngAnimate', 'ui.bootstrap', 'ngDialog']);

app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/notFound");

    $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'login.html',
                controller: 'loginController'
            })
            .state('admin', {
                url: '/admin',
                templateUrl: 'app/views/admin.html',
                controller: 'pageController'
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
            .state('admin.viewIndivContact', {
                url: '/viewIndivContact',
                templateUrl: 'app/views/contact/viewIndividualContact.html',
                controller: 'viewIndivContact'
            })
            .state('novice', {
                url: '/novice',
                templateUrl: 'app/views/novice.html',
                controller: 'pageController'
            })
            .state('novice.homepage', {
                url: '/homepage',
                templateUrl: 'app/views/homepage.html'
            })
            .state('associate', {
                url: '/associate',
                templateUrl: 'app/views/associate.html',
                controller: 'pageController'
            })
            .state('associate.homepage', {
                url: '/homepage',
                templateUrl: 'app/views/homepage.html'
            })
            .state('associate.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
            .state('associate.viewIndivContact', {
                url: '/viewIndivContact',
                templateUrl: 'app/views/contact/viewIndividualContact.html',
                controller: 'viewIndivContact'
            })
            .state('teammanager', {
                url: '/teammanager',
                templateUrl: 'app/views/teammanager.html',
                controller: 'pageController'
            })
            .state('teammanager.homepage', {
                url: '/homepage',
                templateUrl: 'app/views/homepage.html'
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
            .state('teammanager.viewIndivContact', {
                url: '/viewIndivContact',
                templateUrl: 'app/views/contact/viewIndividualContact.html',
                controller: 'viewIndivContact'
            })
            .state('eventleader', {
                url: '/eventleader',
                templateUrl: 'app/views/eventleader.html',
                controller: 'pageController'
            })
            .state('eventleader.homepage', {
                url: '/homepage',
                templateUrl: 'app/views/homepage.html'
            })
            .state('eventleader.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
            })
            .state('eventleader.viewIndivContact', {
                url: '/viewIndivContact',
                templateUrl: 'app/views/contact/viewIndividualContact.html',
                controller: 'viewIndivContact'
            })
            .state('unauthorised', {
                url: '/unauthorised',
                templateUrl: 'unauthorised.html'
            })
            .state('notFound', {
                url: '/notFound',
                templateUrl: 'notFound.html'
            });

});

app.run(['$rootScope', 'session', '$state', function ($rootScope, session, $state) {
        $rootScope.commonUrl = 'http://localhost:8084/bahamas';
        $rootScope.previousState;
        $rootScope.$on('$stateChangeStart', function (event, targetScope, targetParams, fromScope, to, from) {

            var permission = targetScope.name.split('.')[0];

            if (permission == 'notFound' && session.getSession('userType') == null) {
                event.preventDefault();
                $state.go('login');
            }
            if (permission === 'novice') {
                if (session.getSession('userType') !== 'novice' && session.getSession('userType') != null) {
                    event.preventDefault();
                    $state.go('unauthorised');
                } else if (session.getSession('userType') == null) {
                    event.preventDefault();
                    $state.go('login');
                }
            }
            if (permission === 'admin') {
                if (session.getSession('userType') !== 'admin' && session.getSession('userType') != null) {
                    event.preventDefault();
                    $state.go('unauthorised');
                } else if (session.getSession('userType') == null) {
                    event.preventDefault();
                    $state.go('login');
                }
            }
            if (permission === 'teammanager') {
                if (session.getSession('userType') !== 'teammanager' && session.getSession('userType') != null) {
                    event.preventDefault();
                    $state.go('unauthorised');
                } else if (session.getSession('userType') == null) {
                    event.preventDefault();
                    $state.go('login');
                }
            }
            if (permission === 'eventleader') {
                if (session.getSession('userType') !== 'eventleader' && session.getSession('userType') != null) {
                    event.preventDefault();
                    $state.go('unauthorised');
                } else if (session.getSession('userType') == null) {
                    event.preventDefault();
                    $state.go('login');
                }
            }
            if (permission === 'associate') {
                if (session.getSession('userType') !== 'associate' && session.getSession('userType') != null) {
                    event.preventDefault();
                    $state.go('unauthorised');
                } else if (session.getSession('userType') == null) {
                    event.preventDefault();
                    $state.go('login');
                }
            }
            if (permission === 'login' && (session.getSession('userType') !== null)) {
                if (fromScope.url == '/unauthorised') {
                    session.terminateSession();
                } else {
                    event.preventDefault();
                    $state.go(session.getSession('userType'));
                }
            }
            if (permission == 'unauthorised' || permission == 'notFound') {
                $rootScope.previousState = fromScope.name;
            }
        }
        );
    }]);

