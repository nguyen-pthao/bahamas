/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('remoteRegistration', ['$scope', 'session', '$state', 'dataSubmit', '$uibModal', function ($scope, session, $state, dataSubmit, $uibModal) {
        
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
        
        //orderBy for table header
        $scope.predicate = '';
        $scope.reverse = false;

        $scope.order = function (predicate) {
            $scope.reverse = ($scope.predicate === ('\u0022'+ predicate + '\u0022')) ? !$scope.reverse : false;
            $scope.predicate = ('\u0022'+ predicate + '\u0022');
            $scope.records = orderBy($scope.records, $scope.predicate, $scope.reverse);
        };
        
        //general datasend
        var datasend = {
            'token': token,
            'user_type': $scope.permission
        };
        //form retrieve
        if($scope.permission != 'eventleader') {
            $scope.registrationChoice = 'retrieveRegistration';
        } else {
            $scope.registrationChoice = 'retrieveCurrentRegistration';
        }
        
        $scope.registrationRetrieve = function() {
            var url = AppAPI[$scope.registrationChoice];
            $scope.myPromise = dataSubmit.submitData(datasend, url).then(function (response) {
                $scope.resultData = response.data.Records;
                if($scope.resultData != '') {
                    $scope.tableHeader = Object.keys($scope.resultData[0]);
                }
                console.log($scope.tableHeader);
            }, function() {
                window.alert("Fail to send request!");
            });
        };
        
        $scope.form_id = '';
        //watch for change in form choice
        $scope.$watch('registrationChoice', function() {
            if($scope.registrationChoice == 'retrieveCurrentRegistration') {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: './style/ngTemplate/registrationSessionRetrieve.html',
                    scope: $scope,
                    controller: function () {
                        $scope.activate = function () {
                            modalInstance.dismiss('cancel');
                            datasend.form_id = $scope.form_id;
                            $scope.registrationRetrieve();
                        };
                        $scope.cancel = function () {
                            modalInstance.dismiss('cancel');
                        };
                    }
                });
            } else if ($scope.registrationChoice == 'retrieveRegistration') {
                $scope.registrationRetrieve();
            }
        });
        
        
    }]);
