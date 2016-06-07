///* 
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
'use strict';

var app = angular.module('bahamas', ['ui.router', 'ngAnimate']);

app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/login");

    $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'login.html',
                controller: 'loginController',
                resolve: {
                    access: [function () {
                            console.log('login');
                            //session.terminateSession();
                            return true;
                        }
                    ]
                }
            })
            .state('admin', {
                url: '/admin',
                templateUrl: 'app/views/admin.html',
//                controller: 'AdminController',
                resolve: {
                    access: ['session', function (session) {
                            var user = session.getSession('userType');
                            if (user === 'admin') {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    ]
                }
            })
            .state('user', {
                url: '/user',
                templateUrl: './app/views/user.html',
                controller: 'UserController',
                resolve: {
                    access: ['session', function (session) {
                            var user = session.getSession('userType');
                            if (user === 'novice') {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    ]
                }
            })
            .state('novice', {
                url: '/novice',
                templateUrl: './app/views/novice.html',
                controller: 'NoviceController',
                resolve: {
                    access: ['session', function (session) {
                            var user = session.getSession('userType');
                            if (user === 'user') {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    ]
                }
            })
            .state('unauthorized', {
                url: '/unauthorized',
                templateUrl: 'unAuthorized.html'
            })
            .state('admin.addContact', {
                url: '/addContact',
                templateUrl: 'app/views/contact/createContact.html',
                controller: 'createContact'
            });
});

app.run(['$rootScope', '$location', 'session', '$state', function ($rootScope, $location, session, $state) {

        var user = session.getSession('userType');
        console.log(user);
        $rootScope.$on('$stateChangeStart', function (event) {
            console.log('state change');

        });
    }]);

