/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('loadOfficeList',['$rootScope' ,'$http', function($rootScope, $http){    
    this.retrieveOfficeList = function(){
        return $http({
            method: 'POST',
            url: $rootScope.commonUrl + '/officelist'
        });
    }; 
}]);
