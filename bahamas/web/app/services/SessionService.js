/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var app = angular.module('bahamas');

app.factory('session', [function () {

    return {
        setSession: function(key, value) {
            try {
                //if(key == 'token' || key == 'userType' || key == 'username' || key == 'contact_pic' || key == 'contact') {
                    var line = key;
                    line += "=";
                    line += value;
                    // Internet Explorer 6-11
                    if(/*@cc_on!@*/false || !!document.documentMode) {
                        line += "; path=/";
                    } else {
                        line += "; expires=0; path=/";
                    }
                    document.cookie = line;
                    //return true;
                //} else {
//                    if ($window.Storage) {
//                        $window.sessionStorage.setItem(key, value);
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
            } catch (error) {
                window.alert(error, error.message);
            }
        }, 
        getSession: function (key) {
            try {
                //if(key == 'token' || key == 'userType' || key == 'username' || key == 'contact_pic' || key == 'contact') {
                    //console.log(document.cookie);
                    var line = document.cookie;
                    //console.log(key);
                    var keyToFind = (key += "=");
                    //console.log(keyToFind);
                    var indexNumber = line.indexOf(keyToFind);
                    //console.log(indexNumber);
                    if(indexNumber != -1) {
                        indexNumber += keyToFind.length;
                        //console.log(indexNumber);
                        //console.log(line.substring(indexNumber, line.indexOf(";", indexNumber)));
                        var indexNumber2 = line.indexOf(";", indexNumber);
                        var toReturn = '';
                        if (indexNumber2 != -1) {
                            toReturn = line.substring(indexNumber, indexNumber2);
                        } else {
                            toReturn = line.substring(indexNumber, line.length);
                        }
                        //console.log(toReturn);
                        return toReturn;
                        //return line.substring(indexNumber, line.indexOf(";", indexNumber));
                    } else {
                        return null;
                    }
                    //return document.cookie;
//                } else {
//                    if ($window.Storage) {
//                        return $window.sessionStorage.getItem(key);
//                    } else {
//                        return null;
//                    }
//                }
            } catch (error) {
                window.alert(error, error.message);
            }
        },
        removeKey: function (key) {
            try {
                //if(key == 'token' || key == 'userType' || key == 'username' || key == 'contact_pic') {
                var line = document.cookie;
                var indexNumber = line.indexOf(key);
                if(indexNumber != -1) {
                    key += '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/';
                    document.cookie = key;
                    return true;
                } else {
                    return false;
                }    
                    //return true;
//                } else {
//                    if($window.Storage) {
//                        $window.sessionStorage.removeItem(key);
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
            } catch (error) {
                window.alert(error, error.message);
            }  
        },
        terminateSession: function(){
            try {
//                var cookiesStored = document.cookie.split(";");
//                for(var cookie in cookiesStored) {
//                    document.cookie
//                }
//                if ($window.Storage) {
//                    $window.sessionStorage.clear();
//                    return true;
//                } else {
//                    return false;
//                }
                var cookieArray = document.cookie.split(";");
                for(var i in cookieArray) {
                    var cookie = cookieArray[i];
                    var keyIndex = cookie.indexOf("=");
                    var key = cookie.substring(0, keyIndex);
                    key += '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/';
                    document.cookie = key;
                }
                return true;
            } catch (error) {
                window.alert(error, error.message);
            }
        }
    };
}]);