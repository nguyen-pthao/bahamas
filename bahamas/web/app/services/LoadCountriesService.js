/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('loadCountries', ['$http', function($http){
    this.retrieveCountries = function(){
        return $http({
            method: 'GET',
            url: "https://restcountries.eu/rest/v1/all"
        });
    };
}]);
