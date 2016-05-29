/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas', ['bahamasLogin', 'Admin', 'User', 'Novice', 'ngRoute']);

app.constant('AUTH_EVENTS', {
   notAuthenticated: 'auth-not-authenticated',
   notAuthorized: 'auth-not-authorized'
});

app.constant('USER_ROLES', {
   admin: 'admin',
   user: 'user',
   novice: 'novice'
});

app.config(function($routeProvider) {
    $routeProvider.when('/bahamas/login', {
       templateUrl: '../login.html',
       controller: 'loginController'
       
    }).when('/bahamas/app/views/admin', {
        templateUrl: './views/admin.html',
        controller: 'adminController',
        requireAuthentication: true,
        permission: 'admin'
        
    }).when('/bahamas/app/views/user', {
        templateUrl: '.views/user.html',
        controller: 'userController',  
        requireAuthentication: true,
        permission: 'user'
        
    }).when ('/bahamas/app/views/novice', {
        templateUrl: '.views/novice.html',
        controller: 'noviceController',
        requireAuthentication: true,
        permission: 'novice'
        
    }).when ('bahamas/logout', {
        templateUrl: '../index.html'
        
    }).when ('bahamas/unAuthorized', {
        templateUrl: '../unAuthorized.html'
        
    }).otherwise({
        redirectTo: '/404NotFound'
    });
});

app.run(['$rootScope', '$location', 'loginController', function($rootScope, $location, loginController) {
    $rootScope.$on('$routeChangeStart', function(event) {
 //      loginController.init();
       var permission = loginController.getSession('userType');
       
       if(permission === "") {
           event.preventDefault();
           $location.path('bahamas/unAuthorized');
       }
       
       if(userType === 'admin') {
           $location.path('bahamas/app/views/admin.html');
       } else {
           event.preventDefault();
       }
       if (userType === 'user') {
           $location.path('bahamas/app/views/user.html');           
       } else {
           event.preventDefault();
       } 
       if (userType === 'novice') {
           $location.path('bahamas/app/views/novice.html');
       } else {
           event.preventDefault();
       }
    });
}]);



