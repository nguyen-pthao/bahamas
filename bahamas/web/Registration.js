/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';

var app = angular.module('registration', ['ui.router', 'ngAnimate', 'ngDialog', 'ui.bootstrap', 'cgBusy']);

app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/login");

    $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'loginRegistration.html',
                controller: 'loginController'
            })
            .state('register', {
                url: '/register',
                templateUrl: 'registrationForm.html',
                controller: 'registrationController'
            });
});

app.factory('session', [function () {
        return {
            setSession: function (key, value) {
                try {
                    var line = key;
                    line += "=";
                    line += value;
                    // Internet Explorer 6-11
                    if (/*@cc_on!@*/false || !!document.documentMode) {
                        line += "; path=/";
                    } else {
                        line += "; expires=0; path=/";
                    }
                    document.cookie = line;
                } catch (error) {
                    window.alert(error, error.message);
                }
            },
            getSession: function (key) {
                try {
                    var line = document.cookie;
                    var keyToFind = (key += "=");
                    var indexNumber = line.indexOf(keyToFind);
                    if (indexNumber != -1) {
                        indexNumber += keyToFind.length;
                        var indexNumber2 = line.indexOf(";", indexNumber);
                        var toReturn = '';
                        if (indexNumber2 != -1) {
                            toReturn = line.substring(indexNumber, indexNumber2);
                        } else {
                            toReturn = line.substring(indexNumber, line.length);
                        }
                        return toReturn;
                    } else {
                        return null;
                    }
                } catch (error) {
                    window.alert(error, error.message);
                }
            },
            removeKey: function (key) {
                try {
                    var line = document.cookie;
                    var indexNumber = line.indexOf(key);
                    if (indexNumber != -1) {
                        key += '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/';
                        document.cookie = key;
                        return true;
                    } else {
                        return false;
                    }
                } catch (error) {
                    window.alert(error, error.message);
                }
            },
            terminateSession: function () {
                try {
                    var cookieArray = document.cookie.split(";");
                    for (var i in cookieArray) {
                        var cookie = cookieArray[i];
                        var keyIndex = cookie.indexOf("=");
                        var key = cookie.substring(0, keyIndex);
                        key += '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/';
                        document.cookie = key;
                    }
                    return true;
                } catch (error) {
                    window.alert(error, error.message);
                }
            }
        };
    }]);

app.run(['$rootScope', 'session', '$state', function ($rootScope, session, $state) {
        $rootScope.$on('$stateChangeStart', function (event, targetScope, targetParams, fromScope, to, from) {
            var permission = targetScope.name.split('.')[0];
            
            if (permission == 'register' && session.getSession('code') == null) {
                event.preventDefault();
                $state.go('login');
            }
        });
    }]);
