/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('export', ['$scope', 'session', '$state', 'dataSubmit', function ($scope, session, $state, dataSubmit) {
        //to be modified
        $scope.backHome = function () {
            $state.go('admin');
        };
        
        $scope.fileName = '';
        $scope.selectedExport = 'contact';
        $scope.resultData = '';
        $scope.error = false;
        
        $scope.export = function() {
            var url = '/export.' + $scope.selectedExport;
            var datasend = {
                token: session.getSession('token')
            };
            dataSubmit.submitData(datasend, url).then(function (response) {
                var result = response.data;
                $scope.tableHeader = [];
                
                if (result.message == 'success') {
                    $scope.resultData = result.list;
                    if($scope.resultData != '') {
                        $scope.tableHeader = Object.keys($scope.resultData[0]);
                    }
                    console.log($scope.resultData);
                    console.log($scope.tableHeader);
                } else {
                    $scope.error = true;
                    $scope.resultData = result.message;
                }
            }, function(response){
                window.alert("Fail to send request!");
            });
        };

        $scope.exportData = function () {
            var blob = new Blob([document.getElementById('export').innerHTML], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8,%EF%BB%BF"
            });
            if ($scope.fileName != '') {
                saveAs(blob, $scope.fileName + ".xls");
            } else {
                saveAs(blob, 'download.xls');
            }
        };
}]);

