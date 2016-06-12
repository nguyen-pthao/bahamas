/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('loadAllAudit',['$http', function($http){ 
        
    this.retrieveAllAudit = function(toRetrieve){
        return $http({
            method: 'POST',
            url: 'http://localhost:8084/bahamas/retrieve.auditlog',
            data: JSON.stringify(toRetrieve)
        });
    }; 
}]);
