/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('generalReport', ['$scope', 'session', '$state', 'dataSubmit', 'loadTeamAffiliation', '$timeout', 'loadPaymentMode', 'loadMembershipClass', '$uibModal', 'orderByFilter', 'loadEventStatus',
    function ($scope, session, $state, dataSubmit, loadTeamAffiliation, $timeout, loadPaymentMode, loadMembershipClass, $uibModal, orderBy, loadEventStatus) {

        $scope.selectedReport = '';
        $scope.team = '';
        $scope.paymentMode = '';
        $scope.membershipType = '';

        $scope.startdate = '';
        $scope.enddate = '';
        $scope.refdate = '';
        $scope.fileName = '';
        
        $scope.result = '';
        $scope.startline = 0;
        $scope.endline = 1000;
        
        $scope.permission = session.getSession('userType');
        $scope.backHome = function () {
            $state.go($scope.permission);
        };
        
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
                $scope.paymentModeList.unshift({"paymentMode": "All"});
            });
        };
        $scope.loadMembershipList = function () {
            loadMembershipClass.retrieveMembershipClass().then(function (response) {
                $scope.membershipList = response.data.membershipClassList;
                $scope.membershipList.unshift({"membershipClass": "All"});
            });
        };
        $scope.loadEventStatusList = function () {
            loadEventStatus.retrieveEventStatus().then(function (response) {
                $scope.eventStatusList = response.data.eventStatusList;
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

        $scope.openedRef = [];
        $scope.openRef = function (index) {
            $timeout(function () {
                $scope.openedRef[index] = true;
            });
        };
        
        //orderBy for table header
        $scope.predicate = '';
        $scope.reverse = false;

        $scope.order = function (predicate) {
            $scope.reverse = ($scope.predicate === ('\u0022'+ predicate + '\u0022')) ? !$scope.reverse : false;
            console.log($scope.predicate);
            $scope.predicate = ('\u0022'+ predicate + '\u0022');
            $scope.records = orderBy($scope.records, $scope.predicate, $scope.reverse);
        };

        //watch for change in the report selected
        $scope.$watch('selectedReport', function () {
            if ($scope.selectedReport != '') {
                $scope.team = '';
                $scope.paymentMode = '';
                $scope.membershipType = '';
                $scope.startdate = '';
                $scope.enddate = '';
                $scope.refdate = '';
                var template = './style/ngTemplate/reportSelection.html';
                
                if($scope.selectedReport == 'summary_report_of_team_participants') {
                    template = './style/ngTemplate/reportSelectionTeamParticipants.html';
                } else if ($scope.selectedReport == 'summary_report_of_events') {
                    template = './style/ngTemplate/reportSelectionEvents.html';
                } else if ($scope.selectedReport == 'summary_report_of_memberships_by_time_period') {
                    template = './style/ngTemplate/reportSelectionMemberships.html';
                } else if ($scope.selectedReport == 'summary_report_of_donations_by_time_period') {
                    template = './style/ngTemplate/reportSelectionDonations.html';
                } else if ($scope.selectedReport == 'summary_report_of_current_memberships') {
                    template = './style/ngTemplate/reportSelectionCurrentMemberships.html';
                }
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: template,
                    scope: $scope,
                    controller: function () {
                        $scope.ok = function(){
                            modalInstance.dismiss('cancel');
                            generateReport();
                        };
                        $scope.cancel = function () {
                            modalInstance.dismiss('cancel');
//                            $scope.selectedReport = '';
                        };
                    },
                    backdrop: 'static',
                    keyboard: false,
                    size: "md"
                });
            }
        });

        var generateReport = function () {
            var datasend = {};
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
            
            if ($scope.refdate == null) {
                datasend['ref_date'] = '';
            } else if (angular.isUndefined($scope.refdate)) {
                datasend['ref_date'] = '';
            } else if (isNaN($scope.refdate)) {
                datasend['ref_date'] = '';
            } else {
                datasend['ref_date'] = $scope.refdate.valueOf() + "";
            }

            datasend['team'] = $scope.team;
            if($scope.paymentMode != 'All') {
                datasend['payment_mode'] = $scope.paymentMode;
            } else {
                datasend['payment_mode'] = '';
            }
            if($scope.membershipType != '') {
                datasend['membership_type'] = $scope.membershipType;
            } else {
                datasend['membership_type'] = '';
            }
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
                if($scope.selectedReport == 'summary_report_of_team_participants') {
                    $scope.startline = (3 + $scope.header.length + 9 + 2);
                    $scope.endline = ($scope.startline + $scope.records.length - 1);
                    
                    $scope.zero = ('=COUNTIF(F' + $scope.startline + ':F' + $scope.endline + ', "0")');
                    $scope.one = ('=COUNTIF(F' + $scope.startline + ':F' + $scope.endline + ', "1")');
                    $scope.two = ('=COUNTIF(F' + $scope.startline + ':F' + $scope.endline + ', "2")');
                    $scope.three = ('=COUNTIF(F' + $scope.startline + ':F' + $scope.endline + ', "3")');
                    $scope.four = ('=COUNTIF(F' + $scope.startline + ':F' + $scope.endline + ', "4")');
                    $scope.five = ('=COUNTIF(F' + $scope.startline + ':F' + $scope.endline + ', ">=5")');
                } else if($scope.selectedReport == 'summary_report_of_events') {
                    $scope.startline = (3 + $scope.header.length + 4 + $scope.eventStatusList.length + 2);
                    $scope.endline = ($scope.startline + $scope.records.length - 1);
                    
                    for(var obj in $scope.eventStatusList) {
                        var eventObj = $scope.eventStatusList[obj];
                        eventObj['eventFormula'] = ('=COUNTIF(F' + $scope.startline + ':F' + $scope.endline + ', "*' + eventObj['eventStatus'] + '*")');
                    }
                    
                } else if($scope.selectedReport == 'summary_report_of_memberships_by_time_period') {
                    $scope.startline = (3 + $scope.header.length + 5 + $scope.membershipList.length + 2);
                    $scope.endline = ($scope.startline + $scope.records.length - 1);
                    
                    $scope.uniqueContact = '=SUMPRODUCT(1/COUNTIF(B' + $scope.startline + ':B' + $scope.endline + ',B' + $scope.startline + ':B' + $scope.endline + '&""))';
                    
                    for(var obj in $scope.membershipList) {
                        var membershipObj = $scope.membershipList[obj];
                        membershipObj['membershipFormula'] = ('=COUNTIF(C' + $scope.startline + ':C' + $scope.endline + ', "*' + membershipObj['membershipClass'] + '*")');
                    }
                    
                } else if($scope.selectedReport == 'summary_report_of_donations_by_time_period') {
                    $scope.startline = (3 + $scope.header.length + 4 + 2);
                    $scope.endline = ($scope.startline + $scope.records.length - 1);
                    $scope.totalAmount = '=SUM(D' + $scope.startline + ':D' + $scope.endline + ')';
                    
                } else if($scope.selectedReport == 'summary_report_of_current_memberships') {
                    $scope.startline = (3 + $scope.header.length + 5 + $scope.membershipList.length + 2);
                    $scope.endline = ($scope.startline + $scope.records.length - 1);
                    
                    $scope.uniqueContact = '=SUMPRODUCT(1/COUNTIF(B' + $scope.startline + ':B' + $scope.endline + ',B' + $scope.startline + ':B' + $scope.endline + '&""))';
                    
                    for(var obj in $scope.membershipList) {
                        var membershipObj = $scope.membershipList[obj];
                        membershipObj['membershipFormula'] = ('=COUNTIF(C' + $scope.startline + ':C' + $scope.endline + ', "*' + membershipObj['membershipClass'] + '*")');
                    }
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