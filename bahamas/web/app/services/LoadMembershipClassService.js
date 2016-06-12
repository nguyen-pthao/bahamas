/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('loadMembershipClass',['$http', function($http){    
    this.retrieveMembershipClass = function(){
        return $http({
            method: 'POST',
            url: 'http://localhost:8084/bahamas/membershipclasslist'
        });
    }; 
}]);
