/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var AppAPI = {
// Login API     
    'login': '/login',
// Token update API 
    'tokenUpdate': '/token.update',
// Add Contact Info API
    'addAddress' : '/address.add',
    'addAppreciation': '/appreciation.add',
    'addContact': '/contact.add',
    'addDonation': '/donation.add',
    'addEmail': '/email.add',
    'addLanguage': '/language.add',
    'addMembership': '/membership.add',
    'addOfficeHeld': '/officeheld.add',
    'addPhone': '/phone.add',
    'addProxy': '/proxy.add',
    'addSkill': '/skill.add',
    'addTeamJoin': '/teamjoin.add',
// Delete Contact Info API    
    'deleteAddress': '/address.delete',
    'deleteAppreciation': '/appreciation.delete',
    'deleteContact': '/contact.delete',
    'deleteDonation': '/donation.delete',
    'deleteEmail': '/email.delete',
    'deleteLanguage': '/language.delete',
    'deleteMembership': '/membership.delete',
    'deleteOfficeHeld': '/officeheld.delete',
    'deletePhone': '/phone.delete',
    'deleteProxy': '/proxy.delete',
    'deleteSkill': '/skill.delete',
    'deleteTeamJoin': '/teamjoin.delete',
// Retrieve Info API     
    'retrieveAuditLog': '/retrieve.auditlog',
    'retrieveContact': '/contact.retrieve',
    'retrieveContactCurrent': '/contact.retrieve.current',
    'retrieveContactIndiv': '/contact.retrieve.indiv',
// Update Contact Info API 
    'updateAddress': '/address.update',
    'updateAppreciation': '/appreciation.update',
    'updateContact': '/contact.update',
    'updateDonation': '/donation.update',
    'updateEmail': '/email.update',
    'updateLanguage': '/language.update',
    'updateMembership': '/membership.update',
    'updateOfficeHeld': '/officeheld.update',
    'updatePhone': '/phone.update',
    'updateProxy': '/proxy.update',
    'updateSkill': '/skill.update',
    'updateTeamJoin': '/teamjoin.update',
    'updateUser': '/user.update',
// Username/NRIC Validation API 
    'usernameCheck': '/user.check',
// Retrieve dropdown list API 
    'contactTypeList': '/contacttypelist',
    'lsaClassList': '/lsaclasslist',
    'languageList': '/languagelist',
    'membershipClassList': '/membershipclasslist',
    'modeOfSendingReceiptList': '/modeofsendingreceiptList',
    'officeList': '/officelist',
    'paymentModeList': '/paymentmodelist',
    'permissionLevelList': '/permissionlevellist',
    'teamAffiliationList': '/teamaffiliationlist',
    'eventClassList': '/eventclasslist',
    'eventLocationList': '/eventlocationlist'
};
