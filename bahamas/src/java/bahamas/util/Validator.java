/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.util;

import com.mysql.jdbc.StringUtils;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Darryl Mok
 */
public class Validator {

    private static Pattern pattern;
    private static Matcher matcher;

    private static final String DATE_FORMAT = "dd-MMMMM-yyyy HH:mm:ss";
    private static final SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);

    /*
     This class should contain all the validation rules for every unique fields present in the DB
     */
    /**
     * <p>
     * Return a boolean value on whether inputted email is valid
     * </p>
     *
     * @param email the email address
     * @return boolean value, true if email is valid
     */
    public static boolean validEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * <p>
     * Return a boolean value on whether inputted password is valid
     * </p>
     *
     * @param password the password
     * @return boolean value, true if password is valid
     */
    public static boolean validPassword(String password) {
        return (password.length() >= 8 && !password.contains(" ") && password.length() <= 20);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted gender is valid
     * </p>
     *
     * @param gender the gender
     * @return boolean value, true if gender is valid
     */
    public static boolean validGender(String gender) {
        if (gender.length() == 1) {
            if (gender.charAt(0) == 'M' || gender.charAt(0) == 'F' || gender.charAt(0) == 'm' || gender.charAt(0) == 'f') {
                return true;
            }
        }
        return false;
    }

    /*
    public static boolean validTimestamp(String dateTimestamp) {

        String[] arr = dateTimestamp.split(" ");
        if (arr.length == 2) {
            if (isDateValid(arr[0]) && isTimeValid(arr[1])) {
                return true;
            }
        }
        return false;
    }
    */
    /**
     * <p>
     * Return a boolean value on whether inputted String contains blank field
     * </p>
     *
     * @param str the inputted String
     * @return boolean value, true if string is valid
     */
    public static boolean containsBlankField(String str) {

        if (str == null || str.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * <p>
     * Return a boolean value on whether inputted date is valid
     * </p>
     *
     * @param date the date
     * @return boolean value, true if date is valid
     */
    public static boolean isDateValid(String date) {
        if (date.length() == DATE_FORMAT.length()) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(date);
                return true;
            } catch (ParseException e) {
            }
        }
        return false;

    }

    /**
     * <p>
     * Return a boolean value on whether inputted time is valid
     * </p>
     *
     * @param time the email address
     * @return boolean value, true if time is valid
     */
    /*
    public static boolean isTimeValid(String time) {
        if (time.length() == TIME_FORMAT.length()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
                format.setLenient(false);
                format.parse(time);
                return true;
            } catch (ParseException e) {
            }
        }
        return false;
    }
    */
    /**
     * <p>
     * Return a boolean value on whether inputted name is valid
     * </p>
     *
     * @param name the name
     * @return boolean value, true if name is valid
     */
    public static boolean validName(String name) {
        return (name.length() >= 0 && name.length() <= 20);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted nric is valid
     * </p>
     *
     * @param nric the nric
     * @return boolean value, true if nric is valid
     */
    public static boolean validNric(String nric) {
        return (nric.length() != 9);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted nationality is valid
     * </p>
     *
     * @param nationality the nationality
     * @return boolean value, true if nationality is valid
     */
    public static boolean validNationality(String nationality) {
        return (nationality.length() >= 1 && nationality.length() <= 20);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted remarks is valid
     * </p>
     *
     * @param remarks the remarks
     * @return boolean value, true if remarks is valid
     */
    public static boolean validRemarks(String remarks) {
        return (remarks.length() >= 1 && remarks.length() <= 1000);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted address is valid
     * </p>
     *
     * @param address the address
     * @return boolean value, true if address is valid
     */
    public static boolean validAddress(String address) {
        return (address.length() >= 1 && address.length() <= 1000);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted country is valid
     * </p>
     *
     * @param country the country
     * @return boolean value, true if country is valid
     */
    public static boolean validCountry(String country) {
        return (country.length() >= 0 && country.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted profession is valid
     * </p>
     *
     * @param profession the profession
     * @return boolean value, true if profession is valid
     */
    public static boolean validProfession(String profession) {
        return (profession.length() >= 0 && profession.length() <= 200);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted jobTitle is valid
     * </p>
     *
     * @param jobTitle the jobTitle
     * @return boolean value, true if jobTitle is valid
     */
    public static boolean validJobTitle(String jobTitle) {
        return (jobTitle.length() >= 0 && jobTitle.length() <= 200);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted zipcode is valid
     * </p>
     *
     * @param zipcode the zipcode
     * @return boolean value, true if zipcode is valid
     */
    public static boolean validZipcode(String zipcode) {
        return (zipcode.length() >= 0 && !zipcode.contains(" ") && zipcode.length() <= 20);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted paymentmode is valid
     * </p>
     *
     * @param paymentmode the paymentmode
     * @return boolean value, true if paymentmode is valid
     */
    public static boolean validPaymentMode(String paymentmode) {
        return (paymentmode.length() >= 1 && paymentmode.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted explainIfOther is valid
     * </p>
     *
     * @param explainIfOther the explainIfOther
     * @return boolean value, true if explainIfOther is valid
     */
    public static boolean validExplainIfOther(String explainIfOther) {
        return (explainIfOther.length() >= 0 && explainIfOther.length() <= 20);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted transactionRef is valid
     * </p>
     *
     * @param transactionRef the transactionRef
     * @return boolean value, true if transactionRef is valid
     */
    public static boolean validTransactionRef(String transactionRef) {
        return (transactionRef.length() >= 0 && transactionRef.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted receiptNumber is valid
     * </p>
     *
     * @param receiptNumber the receiptNumber
     * @return boolean value, true if receiptNumber is valid
     */
    public static boolean validReceiptNumber(String receiptNumber) {
        return (receiptNumber.length() >= 0 && receiptNumber.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted receiptMode is valid
     * </p>
     *
     * @param receiptMode the receiptMode
     * @return boolean value, true if receiptMode is valid
     */
    public static boolean validReceiptMode(String receiptMode) {
        return (receiptMode.length() >= 0 && receiptMode.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted officeHeld is valid
     * </p>
     *
     * @param officeHeld the officeHeld
     * @return boolean value, true if officeHeld is valid
     */
    public static boolean validOfficeHeld(String officeHeld) {
        return (officeHeld.length() >= 0 && officeHeld.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted amount is valid
     * </p>
     *
     * @param amount the amount
     * @return boolean value, true if amount is valid
     */
    public static boolean validAmount(Double amount) {
        String[] splitter = amount.toString().split("\\.");
        splitter[0].length();   // Before Decimal Count
        int decimalLength = splitter[1].length();  // After Decimal Count

        if (decimalLength == 2) {
            return true;
        }
        return false;
    }

}
