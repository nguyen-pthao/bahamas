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
                
                //var textEncoder = new CustomTextEncoder('windows-1252', {NONSTANDARD_allowLegacyEncoding: true });
                //result = textEncoder.encode(result);
                //console.log(result);
                
                if (result.message == 'success') {
                    $scope.resultData = result.list;
                    if($scope.resultData != '') {
                        $scope.tableHeader = Object.keys($scope.resultData[0]);
                    }
                    
                    if($scope.fileName == '') {
                        $scope.fileName += $scope.selectedExport;
                        var currentTime = new Date();
                        var addDate = '_' + currentTime.getFullYear() + '-' + (currentTime.getMonth() + 1) + '-' + currentTime.getDate();
                        var addHour = '_' + currentTime.getHours() + '.' + currentTime.getMinutes() + '.' + currentTime.getSeconds();

                        $scope.fileName += addDate;
                        $scope.fileName += addHour;
                    }
                } else {
                    $scope.error = true;
                    $scope.resultData = result.message;
                }
            }, function(response){
                window.alert("Fail to send request!");
            });
        };

        $scope.exportData = function () {
            //var textEncoder = new CustomTextEncoder('windows-1252', {NONSTANDARD_allowLegacyEncoding: true });
            
            var table = document.getElementById('export').innerHTML;
            //console.log(table);
            table = '\uFEFF' + table;
            //var contentEncoded = textEncoder.encode(table);
            var blob = new Blob([table], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8;"
                //type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=windows-1252;"
            });
            
            saveAs(blob, $scope.fileName + ".xls");
            
        };
}]);

