/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('generalReport', ['$scope', 'session', '$state', 'dataSubmit', 'loadTeamAffiliation', '$timeout', 'loadPaymentMode', 'loadMembershipClass', function ($scope, session, $state, dataSubmit, loadTeamAffiliation, $timeout, loadPaymentMode, loadMembershipClass) {

        $scope.selectedReport = '';
        $scope.team = '';
        $scope.paymentMode = '';
        $scope.membershipType = '';
        
        $scope.startdate = '';
        $scope.enddate = '';
        $scope.refdate = '';
        
        $scope.loadTeamAffiliationList = function () {
            loadTeamAffiliation.retrieveTeamAffiliation().then(function (response) {
                $scope.teamAffiliationList = response.data.teamAffiliationList;
                var other;
                for (var obj in $scope.teamAffiliationList) {
                    if ($scope.teamAffiliationList[obj].teamAffiliation == 'Others' || $scope.teamAffiliationList[obj].teamAffiliation == 'Other') {
                        other = $scope.teamAffiliationList.splice(obj, 1);
                    }
                }
                if (other != null && !angular.isUndefined(other)) {
                    $scope.teamAffiliationList.push(other[0]);
                }
            });
        };
        
        $scope.loadPaymentModeList = function () {
            loadPaymentMode.retrievePaymentMode().then(function (response) {
                $scope.paymentModeList = response.data.paymentModeList;
            });
        };
        $scope.loadMembershipList = function () {
            loadMembershipClass.retrieveMembershipClass().then(function (response) {
                $scope.membershipList = response.data.membershipClassList;
            });
        };
        //datepicker
        $scope.today = function () {
            $scope.dt = new Date();
        };
        $scope.today();

        $scope.clear = function () {
            $scope.dt = null;
        };

        $scope.inlineOptions = {
            customClass: getDayClass,
            showWeeks: true
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            formatMonth: 'MMM',
            formatDay: 'dd',
            startingDay: 1
        };

        function getDayClass(data) {
            var date = data.date,
                    mode = data.mode;
            if (mode === 'day') {
                var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

                for (var i = 0; i < $scope.events.length; i++) {
                    var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

                    if (dayToCheck === currentDay) {
                        return $scope.events[i].status;
                    }
                }
            }
            return '';
        }
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
        
        $scope.openedRef = [];
        $scope.openRef = function (index) {
            $timeout(function () {
                $scope.openedRef[index] = true;
            });
        };
        
        //watch for change
        $scope.$watch('selectedReport', function() {
            $scope.team = '';
            $scope.paymentMode = '';
            $scope.membershipType = '';
            $scope.startdate = '';
            $scope.enddate = '';
            $scope.refdate = '';
        });
        
        $scope.result = '';
        
        $scope.generateReport = function() {
            var datasend = {};
            datasend['token'] = session.getSession('token');
            datasend['report_type'] = $scope.selectedReport;
            
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
            dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.result = response.data;
                $scope.header = [];
                $scope.header = Object.keys($scope.result);
                console.log($scope.result);
                //console.log($scope.header);
                
                $scope.tableHeader = [];
                
                $scope.records = $scope.result.Records;
                //console.log(records);
                if ($scope.records.length > 0) {
                    $scope.tableHeader = Object.keys($scope.records[0]);
                }
                //console.log($scope.tableHeader);
                
            }, function() {
                window.alert("Fail to send request!");
            });
        };
        
        
        $scope.exportData = function () {
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