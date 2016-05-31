/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module('Admin', ['bahamas']);

app.controller('AdminController', ['$scope', '$location', 'session', '$window', '$http', function($scope, $location, session, $window, $http){
    $scope.user = {
        obj : "userInfo",
        username: session.getSession('username'),
        token: session.getSession('token'),
        userType: session.getSession('userType')
        
    };
    
    //console.log($scope.user);
    $scope.contact = {
        obj : "contact",
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
        obj : "phone",
        dateOsoleted: "",
        countryCode: "",
        number: "",
        remarks: ""
    };
    
    $scope.email = {
        obj : "email",
        dateObsoleted: "",
        address: "",
        remarks: ""
    };
    
    $scope.address = {
        obj : "address",
        dateObsoleted: "",
        addressInput: "",
        zipcode: "",
        country: "",
        remarks: ""
    };
    
    $scope.membership = {
        obj : "membership",
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
        obj : "officeHeld",
        office: "",
        startEndDate: "",
        remarks: ""
    };
    
    $scope.donation = {
        obj : "donation",
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
        obj : "teamJoined",
        teamAffiliation: "",
        otherExplanation: "",
        subteam: "",
        remarks: ""
    };
    
    $scope.proxy = {
        obj : "proxy",
        dateObsoleted: "",
        represent: "",
        standing: "",
        remarks: ""
    };
    
    $scope.languages = {
        obj : "languages",
        dateObsoleted: "",
        language: "",
        otherExplanation: "",
        speakWrite: "",
        remarks: ""
    };
    
    $scope.skillsAssets = {
        obj : "skillsAssets",
        dateObsoleted: "",
        skillsAssetsInput: "",
        otherExplanation: "",
        remarks: ""
    };
    
    
    $scope.newContact = 'homepage.html';
    $scope.showContactForm = function() {
        $scope.newContact = 'createContact.html';
    };
    $scope.backHome = function(){
        $scope.newContact = 'homepage.html';
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
    
    //console.log(JSON.stringify($scope.dataSubmit));
    $scope.submitNewContact = function() {
        console.log($scope.user);
        console.log($scope.contact);
        $scope.location = $location.path();
        var url = location.origin + "/bahamas/contact.add?";
        
        $http({
            method: 'POST',
            //URL to be inserted
            url: url,
            headers: {'Content-Type': 'application/json'},
            data: JSON.stringify($scope.dataSubmit)
        }).success(function (response) {
            console.log("success");
            //return "Successful passing";
        }).error(function (response) {
            console.log("fail");
            //return "Fail to pass values";
        });
    };
    
    $scope.logout = function() {
        session.terminateSession();
        console.log("success");
        var url = location.origin + "/bahamas/login.html";
        $window.location.href = url;
    };
 
}]);