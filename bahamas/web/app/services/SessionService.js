/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var app = angular.module('bahamasLogin');
//create window storageSession service to store username, token and userType
//note that window sessionStorage doesn't share data across tabs or windows
//localStorage and cookies will. 
//localStorage stores values even after browser is closed.
//change method accordingly for the requirement.

app.factory('session', ['$window', function ($window) {
    var username = '';
    var isAuthenticated = false;
    var userType = '';
    var token = '';
    
    var setSession = function(key, value) {
        try {
            if ($window.Storage) {
                $window.sessionStorage.setItem(key, value);
                return true;
            } else {
                return false;
            }
        } catch (error) {
            window.alert(error, error.message);
        }
    };
    
    var getSession = function (key) {
        try {
            if ($window.Storage) {
                return $window.sessionStorage.getItem(key);
            } else {
                return false;
            }
        } catch (error) {
            window.alert(error, error.message);
        }
    };
    
    var terminateSession = function() {
        $window.sessionStorage.setItem(username, '');
        $window.sessionStorage.setItem(token, '');
        $window.sessionStorage.setItem(userType, '');
    };
}]);
