/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('formManagement', ['$scope', 'session', '$state', 'dataSubmit', '$uibModal', '$timeout', 
    function ($scope, session, $state, dataSubmit, $uibModal, $timeout) {
        
        var token = session.getSession('token');
        $scope.permission = session.getSession('userType');
        
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
        
        $scope.code = '';
        $scope.startdate = '';
        $scope.enddate = '';
        $scope.starttime = '';
        $scope.endtime = '';
                
        //general datasend
        var datasend = {
            'token': token,
            'user_type': $scope.permission
        };
        //form retrieve
        $scope.formChoice = 'retrieveAllForm';
        $scope.formRetrieve = function() {
            var url = AppAPI[$scope.formChoice];
            dataSubmit.submitData(datasend, url).then(function (response) {
                console.log(response);
                $scope.result = response.data.Records;
            }, function() {
                window.alert("Fail to send request!");
            });
        };
        //watch for change in form choice
        $scope.$watch('formChoice', function() {
            if($scope.formChoice == 'retrieveAllForm' || $scope.formChoice == 'retrieveForm') {
                $scope.formRetrieve();
            }
        });
        //add new form
        $scope.newForm = function() {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: './style/ngTemplate/registrationForm.html',
                scope: $scope,
                controller: function () {
                    $scope.activate = function () {
                        modalInstance.dismiss('cancel');
                        var url = AppAPI.addForm;
                        var tempStartTime = '';
                        var tempEndTime = '';
                        function activateNewForm() {
                            
                            if ($scope.starttime == null) {
                                tempStartTime = '';
                            } else if ($scope.starttime != null || $scope.starttime != '') {
                                tempStartTime = $scope.starttime.valueOf() + "";
                                $scope.startdate.setTime(tempStartTime);
                            }
                            
                            if ($scope.endtime == null) {
                                tempEndTime = '';
                            } else if ($scope.endtime != null || $scope.endtime != '') {
                                tempEndTime = $scope.endtime.valueOf() + "";
                                $scope.enddate.setTime(tempEndTime);
                            }
                            
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
                            
                            datasend['code'] = $scope.code;
                            dataSubmit.submitData(datasend, url).then(function(response) {
                                console.log(response);
                            }, function() {
                                window.alert("Fail to send request!");
                            });
                        }
                        activateNewForm();
                    };
                    $scope.cancel = function () {
                        modalInstance.dismiss('cancel');
                    };
                },
                backdrop: 'static',
                keyboard: false,
                size: "md"
            });
        };
        
        //generate authentication code
        $scope.generateCode = function () {
            $scope.code = Math.random().toString(36).substring(2, 8);
        };
        
        //edit form
        
        
        //delete form
    }]);
