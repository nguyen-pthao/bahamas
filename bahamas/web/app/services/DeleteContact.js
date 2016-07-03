/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('deleteContact', ['$rootScope', '$http', function ($rootScope, $http) {

        this.deleteContactWithCid = function (toDelete) {
            return $http({
                method: 'POST',
                url: $rootScope.commonUrl + '/contact.delete',
                data: JSON.stringify(toDelete)
            });
        };
    }]);
