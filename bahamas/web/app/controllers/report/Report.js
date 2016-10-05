/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('report', ['$scope', 'session', '$state', 'dataSubmit', function ($scope, session, $state, dataSubmit) {
        $scope.exportData = function() {
            var table = document.getElementById('report').innerHTML;
            //console.log(table);
            table = '\uFEFF' + table;
            //var contentEncoded = textEncoder.encode(table);
            var blob = new Blob([table], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8;"
                //type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=windows-1252;"
            });
            saveAs(blob, "trial.xls");
        };
}]);