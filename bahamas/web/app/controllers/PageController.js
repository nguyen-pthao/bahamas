/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('bahamas');

app.controller('pageController', ['$scope', '$location', 'session', '$state', 'ngDialog', 'loadAllContacts', function ($scope, $location, session, $state, ngDialog, loadAllContacts) {
        $scope.populatePage = function () {
            $scope.name = '';
            $scope.userType = '';
            $scope.dateCreated = '';
            if (session.getSession('username') != null) {
                var contact = angular.fromJson(session.getSession('contact'));
                $scope.name = contact.name;
                var dateToParse = contact.datecreated.substring(0, 10);
                $scope.userType = session.getSession('userType');

                var monthArr = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                var dayArr = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
                dateToParse = new Date(dateToParse);

                var dateParsed = dateToParse.getDay();
                var dayParsed = dateToParse.getDate();
                var monthParsed = dateToParse.getMonth();
                var yearParsed = dateToParse.getFullYear();
                $scope.dateCreated = dayArr[dateParsed] + ", " + dayParsed + " " + monthArr[monthParsed] + " " + yearParsed;
            }
        };

        $scope.logout = function () {
            ngDialog.openConfirm({
                template: './style/ngTemplate/logoutPrompt.html',
                className: 'ngdialog-theme-default dialog-logout-prompt',
                scope: $scope
            }).then(function (response) {
                session.terminateSession();
                $state.go('login');
            });
        };

        $scope.retrieveAllContacts = function () {

            if (session.getSession('teams') === 'undefined') {
                var contactToRetrieve = {
                    'token': session.getSession('token'),
                    'cid': angular.fromJson(session.getSession('contact')).cid,
                    'teamname': "",
                    'permission': session.getSession('userType')
                };
            } else {
                var contactToRetrieve = {
                    'token': session.getSession('token'),
                    'cid': angular.fromJson(session.getSession('contact')).cid,
                    'teamname': angular.fromJson(session.getSession('teams'))[0].teamname,
                    'permission': session.getSession('userType')
                };
            }

            loadAllContacts.retrieveAllContacts(contactToRetrieve).then(function (response) {
                $scope.allContactInfo = response.data.contact;
                console.log($scope.allContactInfo);
                $scope.allContactInfoObj = angular.fromJson($scope.allContactInfo);
                $scope.contactname = [];
                angular.forEach($scope.allContactInfo, function(obj){
                    $scope.contactname.push(obj.name);
                });
                $scope.userType = session.getSession('userType');
//
//
//
//                $scope.foo = function ($event, contact) {
//                    var toURL = $scope.userType + ".viewIndivContact";
//                    var contactInfo = angular.toJson(contact);
//                    session.setSession('contactToDisplay', contactInfo);
//                    $state.go(toURL);
//                };
            });

        };

    }]);