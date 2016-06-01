/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('Admin', ['bahamas']);

app.controller('AdminController', ['$scope', '$location', 'session', '$window', '$http', function ($scope, $location, session, $window, $http) {
        $scope.user = {
            obj: "userInfo",
            username: session.getSession('username'),
            token: session.getSession('token'),
            userType: session.getSession('userType')

        };

        //console.log($scope.user);
        $scope.contact = {
            obj: "contact",
            name: "",
            altName: "",
            contactType: "",
            otherExplanation: "",
            profession: "",
            jobTitle: "",
            nric: "",
            gender: "",
            nationality: "",
            dateCreated: "",
            remarks: ""
        };

        $scope.phone = {
            obj: "phone",
            dateOsoleted: "",
            countryCode: "",
            number: "",
            remarks: ""
        };

        $scope.email = {
            obj: "email",
            dateObsoleted: "",
            address: "",
            remarks: ""
        };

        $scope.address = {
            obj: "address",
            dateObsoleted: "",
            addressInput: "",
            zipcode: "",
            country: "",
            remarks: ""
        };

        $scope.membership = {
            obj: "membership",
            type: "",
            subscriptionAmount: "",
            otherSubscriptionExplanation: "",
            startEndDate: "",
            paymentMode: "",
            otherPaymentModeExplanation: "",
            externalTransactionRef: "",
            receiptNumber: "",
            modeSendingReceipt: "",
            dateReceipt: "",
            otherModeSendingReceiptExplanation: "",
            remarks: ""
        };

        $scope.officeHeld = {
            obj: "officeHeld",
            office: "",
            startEndDate: "",
            remarks: ""
        };

        $scope.donation = {
            obj: "donation",
            dateReceived: "",
            amount: "",
            paymentMode: "",
            otherPaymentModeExplanation: "",
            externalTransactionRef: "",
            receiptNumber: "",
            modeSendingReceipt: "",
            dateReceipt: "",
            otherModeSendingReceiptExplanation: "",
            donorInstruction: "",
            subscriptionAllocation1: "",
            subscriptionSubamount1: "",
            subscriptionAllocation2: "",
            subscriptionSubamount2: "",
            subscriptionAllocation3: "",
            subscriptionSubamount3: "",
            associatedOccasion: "",
            remarks: ""
        };

        $scope.teamJoined = {
            obj: "teamJoined",
            teamAffiliation: "",
            otherExplanation: "",
            subteam: "",
            remarks: ""
        };

        $scope.proxy = {
            obj: "proxy",
            dateObsoleted: "",
            represent: "",
            standing: "",
            remarks: ""
        };

        $scope.languages = {
            obj: "languages",
            dateObsoleted: "",
            language: "",
            otherExplanation: "",
            speakWrite: "",
            remarks: ""
        };

        $scope.skillsAssets = {
            obj: "skillsAssets",
            dateObsoleted: "",
            skillsAssetsInput: "",
            otherExplanation: "",
            remarks: ""
        };


        $scope.newContact = 'homepage.html';
        $scope.showContactForm = function () {
            $scope.newContact = 'createContact.html';
        };
        $scope.backHome = function () {
            $scope.newContact = 'homepage.html';
        };


        $scope.submitNewContact = function () {
            //console.log($scope.user);
            //console.log($scope.contact);
            $scope.dataSubmit = {
                "token": $scope.user.token,
                "name": $scope.contact.name,
                "altName": $scope.contact.altName,
                "contactType": $scope.contact.contactType,
                "otherExplanation": $scope.contact.otherExplanation,
                "profession": $scope.contact.profession,
                "jobTitle": $scope.contact.jobTitle,
                "nric": $scope.contact.nric,
                "gender": $scope.contact.gender,
                "nationality": $scope.contact.nationality,
                "remarks": $scope.contact.remarks
            };

            $scope.location = $location.path();
            var url = location.origin + "/bahamas/contact.add?";

            $http({
                method: 'POST',
                //URL to be inserted
                url: url,
                headers: {'Content-Type': 'application/json'},
                data: JSON.stringify($scope.dataSubmit)
            }).success(function (response) {
                console.log(response);
                //return "Successful passing";
            }).error(function (response) {
                console.log("fail");
                //return "Fail to pass values";
            });
            console.log(JSON.stringify($scope.dataSubmit));
        };

        $scope.logout = function () {
            session.terminateSession();
            console.log("success");
            var url = location.origin + "/bahamas/login.html";
            $window.location.href = url;
        };

        $scope.loadContactList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/ContactTypeList";
            $http.get(url).then(function (response) {
                $scope.contactTypeListData = response.data.contact;
            });
        };

        $scope.loadMembershipClassList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/membershipClassList";
            $http.get(url).then(function (response) {
                $scope.membershipClassListData = response.data.membershipClassList;
            });
        };
        
        $scope.loadPaymentModeList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/PaymentModeList";
            $http.get(url).then(function (response) {
                $scope.paymentModeListData = response.data.paymentModeList; 
            });
        };
        
         $scope.loadMOSRList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/ModeOfSendingReceiptList";
            $http.get(url).then(function (response) {
                $scope.mosrListData = response.data.mode; 
            });
        };
        
        $scope.loadOfficeHeldList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/OfficeList";
            $http.get(url).then(function (response) {
                $scope.officeHeldListData = response.data.officeList; 
            });
        };
        
        $scope.loadTeamAffiliationList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/TeamAffiliationList";
            $http.get(url).then(function (response) {
                $scope.teamAffiliationListData = response.data.teamAffiliationList; 
            });
        };
        
        $scope.loadLanguageList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/LanguageList";
            $http.get(url).then(function (response) {
                $scope.languageListData = response.data.languageList; 
            });
        };
        
        $scope.loadPermissionList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/PermissionLevelList";
            $http.get(url).then(function (response) {
                $scope.permissionLevelListData = response.data.permissionLevelList; 
            });
        };
        
        $scope.loadLSAClassList = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/LSAClassList";
            $http.get(url).then(function (response) {
                $scope.lsaClassListData = response.data.lsaClassList; 
            });
        };
    }]);
