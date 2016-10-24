//Created by Thao Nguyen

var app = angular.module('bahamas');

app.controller('loginController',
        ['$rootScope', '$scope', '$http', 'session', '$state', 'ngDialog', 'Idle',
            function ($rootScope, $scope, $http, session, $state, ngDialog, Idle) {

//DETECT USER'S BROWSER               
                // Opera 8.0+
                //var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
                // Firefox 1.0+
                //var isFirefox = typeof InstallTrigger !== 'undefined';
                // At least Safari 3+: "[object HTMLElementConstructor]"
                //var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
                // Internet Explorer 6-11
                //var isIE = /*@cc_on!@*/false || !!document.documentMode;
                // Edge 20+
                //var isEdge = !isIE && !!window.StyleMedia;
                // Chrome 1+
                //var isChrome = !!window.chrome && !!window.chrome.webstore;
                // Blink engine detection
                //var isBlink = (isChrome || isOpera) && !!window.CSS;
                
                if (!(!!window.chrome && !!window.chrome.webstore)) {                
                    ngDialog.openConfirm({
                        template: './style/ngTemplate/changeBrowser.html',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }
                 
                $scope.error = false;
//DEFINE AUTHORISED USER OBJECT
                var authorisedUser = {
                    'username': "",
                    'token': "",
                    'userType': "",
                    'contact': {},
                    'teams': []
                };
//DEFINE USER OBJECT
                $scope.user = {
                    'username': '',
                    'password': ''
                };
                
//LOGIN SERVICE
                $scope.loginUser = function () {
                    var url = $rootScope.commonUrl + "/login";
                    $http({
                        method: 'POST',
                        url: url,
                        headers: {'Content-Type': 'application/json'},
                        data: JSON.stringify($scope.user)
                    }).success(function (response) {
                        var returnedUser = response;
//                console.log(response);
                        if (returnedUser.message === "success") {
                            authorisedUser.username = $scope.user.username;
                            authorisedUser.token = returnedUser.token;
                            authorisedUser.userType = returnedUser['user_type'];
                            authorisedUser.contact = returnedUser.contact;
                            authorisedUser.teams = returnedUser.contact.teams;
//STORE AUTHORISED USER INFO IN SESSION SERVICE
                            if (authorisedUser.username !== "") {
//                        console.log(authorisedUser);
                                var storeUsername = session.setSession('username', authorisedUser.username);
                                var storeToken = session.setSession('token', authorisedUser.token);
                                var storeUserType = session.setSession('userType', authorisedUser.userType);
                                var storeContact = session.setSession('contact', angular.toJson(authorisedUser.contact));
                                var storeTeams = session.setSession('teams', angular.toJson(authorisedUser.teams));
                                var getUserType = session.getSession("userType");
                                session.setSession('contact_pic', response.contact['profile_pic']);
                                //tokenUpdate.refreshToken(authorisedUser.token);

                                Idle.watch();
                                $state.go(getUserType.toString());
                            }
                        } else {
                            $scope.error = true;
                        }
                    }).error(function () {
                        window.alert("Fail to send request!");
                    });
                };
            }]);



