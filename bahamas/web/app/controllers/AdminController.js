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
    console.log($scope.user);
    
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
        dateOfBirth: "",
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
    
    
    $scope.submitNewContact = function() {
        //console.log($scope.user);
        //console.log($scope.contact);
        $scope.dataSubmit = {
            "token":   $scope.user.token, 
            "name":    $scope.contact.name,
            "altName":    $scope.contact.altName,
            "contactType":    $scope.contact.contactType,
            "otherExplanation":    $scope.contact.otherExplanation,
            "profession":    $scope.contact.profession,
            "jobTitle":    $scope.contact.jobTitle,
            "nric":    $scope.contact.nric,
            "gender":   $scope.contact.gender,
            "nationality":    $scope.contact.nationality,
            "dateOfBirth": $scope.contact.dateOfBirth,
            "remarks":    $scope.contact.remarks
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
        console.log($scope.dataSubmit);
        console.log(JSON.stringify($scope.dataSubmit));
    };
    
    $scope.logout = function() {
        session.terminateSession();
        console.log("success");
        var url = location.origin + "/bahamas/login.html";
        $window.location.href = url;
    };
    
    
    //MULTI-STEP FORM
    $scope.noOfPages = [
        'Page 1',
        'Page 2',
        'Page 3',
        'Page 4',
        'Page 5',
        'Page 6'
    ];
    
    $scope.selection = $scope.noOfPages[0];
    
    $scope.getCurrentPage = function() {
        return _.indexOf($scope.noOfPages, $scope.selection);
    };
    
    if($scope.getCurrentPage === 5) {
        
    }
    $scope.hasNextPage = function(){
        var pageIndex = $scope.getCurrentPage();
        var nextPageIndex = pageIndex + 1;
        return !_.isUndefined($scope.noOfPages[nextPageIndex]);
    };
    
    $scope.hasPreviousPage = function() {
        var pageIndex = $scope.getCurrentPage();
        var previousPageIndex = pageIndex - 1;
        return !_.isUndefined($scope.noOfPages[previousPageIndex]);
    };
    
    $scope.nextStep = function() {
        if($scope.hasNextPage()) {
            var pageIndex = $scope.getCurrentPage();
            var next = pageIndex + 1;
            $scope.selection = $scope.noOfPages[next];
        }
    };
    
    $scope.previousStep = function() {
        if($scope.hasPreviousPage()) {
            var pageIndex = $scope.getCurrentPage();
            var previous = pageIndex - 1;
            $scope.selection = $scope.noOfPages[previous];
        }
    };
    
    //END
    
}]);
