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
        setData: function(name, value) {
            if(!dataStorage.hasOwnProperty(name)) {
                dataStorage[name] = value;
                return true;
            } else {
                return false;
            };            
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
        getDataNames: function () {
            return Object.getOwnPropertyNames(dataStorage);
        }
    };
});
