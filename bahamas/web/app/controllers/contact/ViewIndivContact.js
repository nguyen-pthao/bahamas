/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('viewIndivContact', ['$scope', 'dataStorage', '$state', function($scope, dataStorage, $state){
        console.log('hellooooo');
        $scope.contact = dataStorage.getData('contact');
        console.log($scope.contact);
//        $scope.displayAllContactInfo = function(){
//           $scope.contact = dataStorage.getData('contact');
//        };
        
}]);
