/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('loadAllContacts',['$http', function($http){ 
        
    this.retrieveAllContacts = function(toRetrieve){
        return $http({
            method: 'GET',
            url: 'http://localhost:8084/bahamas/contact.retrieve',
            data: JSON.stringify(toRetrieve)
        });
    }; 
}]);