/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('loadAllContacts', ['$rootScope', '$http', function ($rootScope, $http) {

        this.retrieveAllContacts = function (toRetrieve) {
            return $http({
                method: 'POST',
                url: $rootScope.commonUrl + '/contact.retrieve',
                data: JSON.stringify(toRetrieve)
            });
        };
    }]);