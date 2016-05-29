///* 
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */

var app = angular.module('bahamas', ['bahamasLogin', 'Admin', 'User', 'Novice', 'ngRoute']);

app.config(function($routeProvider) {
    $routeProvider.when('/login', {
        templateUrl: '../../login.html',
        controller: 'loginController'
    }).when('/admin', {
        templateUrl: './views/admin.html',
        controller: 'adminController',
        requireAuthentication: true,
        permission: 'admin'
        
    }).when('/user', {
        templateUrl: './views/user.html',
        controller: 'userController',  
        requireAuthentication: true,
        permission: 'user'
        
    }).when ('/novice', {
        templateUrl: './views/novice.html',
        controller: 'noviceController',
        requireAuthentication: true,
        permission: 'novice'
        
    }).when ('/logout', {
        templateUrl: '../../index.html'
        
    }).when ('/unAuthorized', {
        templateUrl: '../../unAuthorized.html'
        
    }).otherwise({
        redirectTo: '404NotFound'
    });
});

app.run(['$rootScope', '$location', 'session', function($rootScope, $location, session) {
    $rootScope.$on('$routeChangeStart', function(event) {
       session.init();
       
       var permission = session.getSession('userType');
       if(permission === "") {
           event.preventDefault();
           $location.path('/unAuthorized');
       }
       
       if(userType === 'admin') {
           $location.path('/admin');
       } else {
           event.preventDefault();
       }
       if (userType === 'user') {
           $location.path('/user');           
       } else {
           event.preventDefault();
       } 
       if (userType === 'novice') {
           $location.path('/novice');
       } else {
           event.preventDefault();
       }
    });
}]);

