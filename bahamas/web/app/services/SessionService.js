/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var app = angular.module('bahamas');

app.factory('session', ['$window', function ($window) {
    var user;
    return {
        setSession: function(key, value) {
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
        }, 
        getSession: function (key) {
            try {
                if ($window.Storage) {
                    return $window.sessionStorage.getItem(key);
                } else {
                    return false;
                }
            } catch (error) {
                window.alert(error, error.message);
            }
        },
        terminateSession: function(){
            try {
                if ($window.Storage) {
                    $window.sessionStorage.clear();
                    return true;
                } else {
                    return false;
                }
            } catch (error) {
                window.alert(error, error.message);
            }
        }
    };
}]);