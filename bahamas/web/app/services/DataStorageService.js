/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var app = angular.module('bahamas');

app.factory('dataStorage', function() {
    var dataStorage = {};
    
    return {
        setData: function(name, value, reset) {
            dataStorage[name] = value;
            return true;            
        }, 
        getData: function (name) {
            if(dataStorage.hasOwnProperty(name)) {
                return dataStorage[name];
            } else {
                return null;
            };
        },
        removeData: function (name) {
            if(dataStorage.hasOwnProperty(name)) {
                delete dataStorage[name];
                return true;
            } else {
                return false;
            };
        },
        getDataKeys: function () {
            return Object.getOwnPropertyNames(dataStorage);
        },
        checkDataExistence: function (name) {
            if(dataStorage.hasOwnProperty(name)) {
                return true;
            } else {
                return false;
            }
        }
    };
});
