/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.service('loadContactType', ['$rootScope', '$http', function ($rootScope, $http) {
        this.retrieveContactType = function () {
            return $http({
                method: 'POST',
                url: $rootScope.commonUrl + '/contacttypelist'
            });
        };
    }]);
