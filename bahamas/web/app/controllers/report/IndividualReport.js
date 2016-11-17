/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('individualReport', ['$scope', 'session', '$state', 'dataSubmit', 'loadTeamAffiliation', '$timeout', '$uibModal',
    function ($scope, session, $state, dataSubmit, loadTeamAffiliation, $timeout, $uibModal) {

        $scope.permission = session.getSession('userType');
        $scope.backHome = function () {
            $state.go($scope.permission);
        };
        $scope.userViewContact = $scope.permission + "/" + 'viewIndivContact';
        
        if(session.getSession('reportSelection') != null) { 
            $scope.selectedReport = session.getSession('reportSelection');
        } else {
            $scope.selectedReport = '';
        }   
        
        var cid = session.getSession('contactReport');
        $scope.name = session.getSession('contactName');
        
        $scope.team = '';
        $scope.startdate = '';
        $scope.enddate = '';
        $scope.fileName = '';
        
        $scope.result = '';
        $scope.startline = 0;
        $scope.endline = 1000;
        
        $scope.loadTeamAffiliationList = function () {
            loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                $scope.teamAffiliationList = response.data.teamAffiliationList;
            });
        };
        
        //datepicker
        $scope.today = function () {
            $scope.dt = new Date();
        };

        $scope.clear = function () {
            $scope.dt = null;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            formatMonth: 'MMM',
            formatDay: 'dd',
            startingDay: 1
        };

        $scope.format = 'dd MMM yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];

        $scope.openedStart = [];
        $scope.openStart = function (index) {
            $timeout(function () {
                $scope.openedStart[index] = true;
            });
        };

        $scope.openedEnd = [];
        $scope.openEnd = function (index) {
            $timeout(function () {
                $scope.openedEnd[index] = true;
            });
        };
        
        $scope.$watch('selectedReport', function () {
            if ($scope.selectedReport != '') {
                $scope.team = '';
                $scope.startdate = '';
                $scope.enddate = '';
                
                var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: './style/ngTemplate/individualReportSelection.html',
                scope: $scope,
                controller: function () {
                    $scope.ok = function () {
                        modalInstance.dismiss('cancel');
                        //firsttime = false;
                        generateReport();
                    };
                    $scope.cancel = function () {
                        modalInstance.dismiss('cancel');
//                        $scope.selectedReport = '';
                    };
                },
                backdrop: 'static',
                keyboard: false,
                size: "md"
            });
            }
        });
        
        //orderBy for table header
        $scope.predicate = '';
        $scope.reverse = false;

        $scope.order = function (predicate) {
            $scope.reverse = ($scope.predicate === ('\u0022'+ predicate + '\u0022')) ? !$scope.reverse : false;
            console.log($scope.predicate);
            $scope.predicate = ('\u0022'+ predicate + '\u0022');
            $scope.records = orderBy($scope.records, $scope.predicate, $scope.reverse);
        };
        
        var generateReport = function () {
            var datasend = {};
            datasend['contact_id'] = cid;
            datasend['token'] = session.getSession('token');
            datasend['report_type'] = $scope.selectedReport;
            datasend['user_type'] = $scope.permission;
            
            if ($scope.startdate == null) {
                datasend['start_date'] = '';
            } else if (angular.isUndefined($scope.startdate)) {
                datasend['start_date'] = '';
            } else if (isNaN($scope.startdate)) {
                datasend['start_date'] = '';
            } else {
                datasend['start_date'] = $scope.startdate.valueOf() + "";
            }

            if ($scope.enddate == null) {
                datasend['end_date'] = '';
            } else if (angular.isUndefined($scope.enddate)) {
                datasend['end_date'] = '';
            } else if (isNaN($scope.enddate)) {
                datasend['end_date'] = '';
            } else {
                datasend['end_date'] = $scope.enddate.valueOf() + "";
            }

            datasend['team'] = $scope.team;
            datasend['payment_mode'] = $scope.paymentMode;
            var url = AppAPI.generateReport;
            $scope.myPromise = dataSubmit.submitData(datasend, url).then(function (response) {
                //remember to initialize $scope.function in html page
                $scope.result = response.data;
                $scope.header = [];
                $scope.header = Object.keys($scope.result);
                console.log($scope.result);

                $scope.tableHeader = [];

                $scope.records = $scope.result.Records;
                if ($scope.records.length > 0) {
                    $scope.tableHeader = Object.keys($scope.records[0]);
                }
                
                //define start, end lines and formulae 
                if($scope.selectedReport == 'individual_membership_report') {
                    $scope.startline = (3 + $scope.header.length + 6 + 2);
                    $scope.endline = ($scope.startline + $scope.records.length - 1);
                    
                    $scope.totalMAmount = '=SUM(E' + $scope.startline + ':E' + $scope.endline + ')';
                    
                } else if($scope.selectedReport == 'individual_donor_report') {
                    $scope.startline = (3 + $scope.header.length + 5 + $scope.membershipList.length + 2);
                    $scope.endline = ($scope.startline + $scope.records.length - 1);
                    
                    $scope.totalDAmount = '=SUM(C' + $scope.startline + ':C' + $scope.endline + ')';
                }
                
            }, function () {
                window.alert("Fail to send request!");
            });
        };
        
        $scope.exportData = function () {
            var table = document.getElementById($scope.selectedReport).innerHTML;
            table = '\uFEFF' + table;
            var blob = new Blob([table], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8;"
            });
            if ($scope.fileName != '') {
                saveAs(blob, $scope.fileName + ".xls");
            } else {
                saveAs(blob, $scope.selectedReport + ".xls");
            }
        };
}]);
