///* 
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
'use strict';

var app = angular.module('bahamas', ['Admin','User','Novice','ngRoute']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/login', {
        templateUrl: 'login.html',
        controller: 'loginController'
    }).when('/admin', {
        templateUrl: './app/views/admin.html',
        controller: 'AdminController',
        resolve: {
            access: ['session', function(session) {
                var user = session.getSession('userType');
                if (user === 'admin') {
                    return true;
                } else {
                    return false;
                }
            }]
        }
    }).when('/user', {
        templateUrl: './app/views/user.html',
        controller: 'UserController',
        resolve: {
            access: ['session', function(session) {
                var user = session.getSession('userType');
                if (user === 'user') {
                    return true;
                } else {
                    return false;
                }
            }]
        }
        
    }).when ('/novice', {
        templateUrl: './app/views/novice.html',
        controller: 'NoviceController',
        resolve: {
            access: ['session', function(session) {
                var user = session.getSession('userType');
                if (user === 'novice') {
                    return true;
                } else {
                    return false;
                }
            }]
        }
    }).when ('/logout', {
        templateUrl: 'unAuthorized.html',
        controller: 'logoutController'
        
    }).when ('/unAuthorized', {
        templateUrl: 'unAuthorized.html'
        
    }).otherwise({
        redirectTo: '/login'
    });
}]);

app.run(['$rootScope', '$location', '$window', 'session', function($rootScope, $location, $window, session) {
    $rootScope.$on('$routeChangeStart', function(event) {
       //session.init();
       //console.log("start");
    });
}]);

