/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//author: Marcus Ong

var app = angular.module('bahamas');

app.controller('viewAuditLog',
        ['$scope', '$http', '$location', 'session', '$window', '$state', '$log', 'loadAllAudit',
            function ($scope, $http, $location, session, $window, $state, $log, loadAllAudit) {

                $scope.backHome = function () {
                    $state.go('admin.homepage');
                };

                $scope.retrieveAllAuditLog = function () {
                    var auditToRetrieve = {
                        'token': session.getSession('token'),
                        'cid': angular.fromJson(session.getSession('contact')).cid     
                    };
                    var allAuditObjKey = [];
                    loadAllAudit.retrieveAllAudit(auditToRetrieve).then(function (response) {
                        $scope.allAuditInfo = response.data.auditlog;
                        var firstAuditObject = $scope.allAuditInfo[0];
                        for (auditHeader in firstAuditObject) {
                            allAuditObjKey.push(auditHeader);
                        }
                        $scope.allAuditObjectKeys = allAuditObjKey;
                        $scope.totalItems = $scope.allAuditInfo.length;
                        $scope.currentPage = 1;
                        $scope.itemsPerPage = $scope.allAuditInfo.length;

                        $scope.itemsPerPageChanged = function () {
                            var total = $scope.totalItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);
                                $scope.filteredAudit = $scope.allAuditInfo.slice(begin, end);
                            });
                        };
                        var total = $scope.totalItems / $scope.itemsPerPage;

                        $scope.totalPages = Math.ceil(total);
                        $scope.$watch('currentPage + itemsPerPage', function () {
                            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                            var end = begin + parseInt($scope.itemsPerPage);

                            $scope.filteredAudit = $scope.allAuditInfo.slice(begin, end);
                        });

                        $scope.pageChanged = function () {
                            var total = $scope.totalItems / $scope.itemsPerPage;
                            $scope.totalPages = Math.ceil(total);
                            $scope.$watch('currentPage + itemsPerPage', function () {
                                var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
                                var end = begin + parseInt($scope.itemsPerPage);

                                $scope.filteredAudit = $scope.allContactInfo.slice(begin, end);
                            });
                        };
                    });
                };
            }]);