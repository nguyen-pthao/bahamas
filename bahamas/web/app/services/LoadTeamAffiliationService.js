/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var app = angular.module('bahamas');

app.service('loadTeamAffiliation',['$http', '$location', function($http, $location){    
//    var location = $location.path();
//    var retrieveUrl = location.origin + "/bahamas/teamaffiliationlist";
    this.retrieveTeamAffiliation = function(){
        return $http({
            method: 'GET',
            url: 'http://localhost:8084/bahamas/teamaffiliationlist'
        });
    }; 
}]);
