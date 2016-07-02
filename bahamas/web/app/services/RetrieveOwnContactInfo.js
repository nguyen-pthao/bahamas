/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('retrieveOwnContactInfo', ['$rootScope', '$http', function ($rootScope, $http) {

        this.retrieveContact = function (toRetrieve) {
            return $http({
                method: 'POST',
                url: $rootScope.commonUrl + '/contact.retrieve.current',
                data: JSON.stringify(toRetrieve)
            });
        };
    }]);
