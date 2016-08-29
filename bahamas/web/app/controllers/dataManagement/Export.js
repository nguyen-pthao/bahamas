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
        
        $scope.fileName = 'contact';
        $scope.selectedExport = 'contact';
        
        $scope.export = function() {
            var url = '/export.' + $scope.selectedExport;
            var datasend = {
                token: session.getSession('token')
            };
            dataSubmit.submitData(datasend, url).then(function (response) {
                var result = response.data;
                $scope.resultData = '';
                $scope.tableHeader = [];
                
                if (result.message == 'success') {
                    //to be modified
                    $scope.exportData = function () {
                        var blob = new Blob([document.getElementById('export').innerHTML], {
                            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                        });
                        saveAs(blob, $scope.fileName+".xls");
                    };
                    var check = function() {
                        $scope.tableHeader = Object.keys($scope.resultData[0]);
                        console.log($scope.tableHeader);
                        $scope.exportData();
                    };
                    
                    //to be modified
                    if ($scope.selectedExport == 'contact') {
                        $scope.resultData = response.data.contactlist;
                        //$scope.tableHeader = Object.keys($scope.resultData[0]);
                        console.log($scope.resultData);
                        check();
                    } else if ($scope.selectedExport == 'phone') {
                        $scope.resultData = response.data.phonelist;
                        
                    } else if ($scope.selectedExport == 'email') {
                        $scope.resultData = response.data.emaillist;
                        
                    } else if ($scope.selectedExport == 'address') {
                        $scope.resultData = response.data.addresslist;
                        
                    } else if ($scope.selectedExport == 'membership') {
                        $scope.resultData = response.data.membershiplist;
                        
                    } else if ($scope.selectedExport == 'officeheld') {
                        $scope.resultData = response.data.officeheldlist;
                        
                    } else if ($scope.selectedExport == 'donation') {
                        $scope.resultData = response.data.donationlist;
                        
                    } else if ($scope.selectedExport == 'teamjoin') {
                        $scope.resultData = response.data.teamjoinlist;
                        
                    } else if ($scope.selectedExport == 'language') {
                        $scope.resultData = response.data.languagelist;
                        
                    } else if ($scope.selectedExport == 'skill') {
                        $scope.resultData = response.data.skilllist;
                        
                    }
                    
                } else if(result.message == 'list empty'){
                    
                }
            }, function(response){
                window.alert("Fail to send request!");
            });
        };
}]);

