/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('Admin', ['bahamas']);

app.controller('AdminController', ['$scope', '$location', 'session', function ($scope, $location, session) {
        $scope.user = {
            name: "userInfo",
            username: session.getSession('username'),
            token: session.getSession('token'),
            userType: session.getSession('userType')

        };
        //console.log($scope.user);
        $scope.contact = {
            name: "contact",
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
            name: "phone",
            dateOsoleted: "",
            countryCode: "",
            number: "",
            remarks: ""
        };

        $scope.email = {
            name: "email",
            dateObsoleted: "",
            address: "",
            remarks: ""
        };

        $scope.address = {
            name: "address",
            dateObsoleted: "",
            addressInput: "",
            country: "",
            remarks: ""
        };

        $scope.membership = {
            name: "membership",
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
            name: "officeHeld",
            office: "",
            startEndDate: "",
            remarks: ""
        };

        $scope.donation = {
            name: "donation",
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
            name: "teamJoined",
            teamAffiliation: "",
            otherExplanation: "",
            subteam: "",
            remarks: ""
        };

        $scope.proxy = {
            name: "proxy",
            dateObsoleted: "",
            rep: "",
            standing: "",
            remarks: ""
        };

        $scope.languages = {
            name: "languages",
            dateObsoleted: "",
            language: "",
            otherExplanation: "",
            speakWrite: "",
            remarks: ""
        };

        $scope.skillsAssets = {
            name: "skillsAssets",
            dateObsoleted: "",
            skillsAssetsInput: "",
            otherExplanation: "",
            remarks: ""
        };

        $scope.dataSubmit = [
            $scope.user,
            $scope.contact,
            $scope.phone,
            $scope.email,
            $scope.address,
            $scope.membership,
            $scope.officeHeld,
            $scope.donation,
            $scope.teamJoined,
            $scope.proxy,
            $scope.languages,
            $scope.skillsAssets
        ];

        $scope.submitNewContact = function () {
            $scope.location = $location.path();
            var url = location.origin + "/bahamas/contact.add?";

            $http({
                method: 'POST',
                //URL to be inserted
                url: '',
                headers: {'Content-Type': 'application/json'},
                data: dataSubmit
            }).success(function (response) {
                console.log("success");
                //return "Successful passing";
            }).error(function (response) {
                console.log("fail");
                //return "Fail to pass values";
            });
        };

    }]);
