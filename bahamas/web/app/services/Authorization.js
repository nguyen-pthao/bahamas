/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Created by Marcus Ong

var app = angular.module('bahamas');

app.service('authorization', function ($state) {

    this.isAdmin = false;
    this.memorizedState = null;

    var
            clear = function () {
                this.isAdmin = false;
                this.memorizedState = null;
            },
            setAdmin = function (fallback) {
                this.isAdmin = true;
                var targetState = this.memorizedState ? this.memorizedState : fallback;
                $state.go(targetState);
            };

    return {
        isAdmin: this.isAdmin,
        memorizedState: this.memorizedState,
        clear: clear,
        setAdmin: setAdmin
    };
});