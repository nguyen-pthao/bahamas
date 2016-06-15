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
            .state('novice', {
                url: '/novice',
                templateUrl: 'app/views/novice.html',
                controller: 'pageController'
            })
            .state('novice.viewContacts', {
                url: '/viewContacts',
                templateUrl: 'app/views/contact/viewContacts.html',
                controller: 'viewContacts'
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
            .state('unauthorised', {
                url: '/unauthorised',
                templateUrl: 'unauthorised.html'
            });

});

app.run(['$rootScope', '$location', 'session', '$state', function ($rootScope, $location, session, $state) {
        $rootScope.commonUrl = 'http://localhost:8084/bahamas';
        $rootScope.previousState;
        $rootScope.stateToRevert;
        $rootScope.$on('$stateChangeSuccess', function (event, targetScope, targetParams, fromScope, to, from) {
            var permission = $location.path().split('/')[1];
//            if (targetScope.url === '/unauthorised') {
//                //to stop the browser from endless looping
//            } else {

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
            if (permission == '/unauthorised') {
                //to be modified
            }
            $rootScope.previousState = $location.path();
        }

//        });
        );

        $rootScope.$on('$stateChangeStart', function (event, targetScope, to, from) {
            if (targetScope.url == 'unauthorised') {
                //keep previous state
            } else {
                $rootScope.stateToRevert = $rootScope.previousState;
            }
        })
    }]);

