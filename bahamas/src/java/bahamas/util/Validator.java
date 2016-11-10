/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.util;

import bahamas.dao.ContactDAO;
import bahamas.dao.EventAffiliationDAO;
import bahamas.dao.EventDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.Contact;
import bahamas.entity.Event;
import bahamas.entity.EventAffiliation;
import com.google.gson.JsonElement;
import com.mysql.jdbc.StringUtils;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
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

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static ArrayList<String> errorList = new ArrayList<String>();

    public static ArrayList<String> getErrorList() {
        return errorList;
    }

    public static void setErrorList(ArrayList<String> errorList) {
        Validator.errorList = errorList;
    }

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
        String emailPattern = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
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
    public static String containsBlankField(JsonElement e) {
        if (e != null && !e.isJsonNull()) {
            String str = e.getAsString();
            if (str == null || str.trim().isEmpty()) {
                return null;
            } else {
                return str;
            }

        }
        return null;
    }

    public static int isIntValid(JsonElement e) {
        if (e != null && !e.isJsonNull()) {
            try {
                return e.getAsInt();
            } catch (Exception ex) {
                return 0;
            }
        }
        return 0;
    }

    public static BigDecimal truncateDecimal(double x, int numberofDecimals) throws Exception {
        if (x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }

    public static double isDoubleValid(JsonElement e) {
        if (e != null && !e.isJsonNull()) {

            try {

                String value = e.getAsString();
                String pattern1 = "^[0-9]*\\.[0-9]{2}$";
                String pattern2 = "^[0-9]*\\.[0-9][0-9]$";
                String pattern3 = "^[0-9]*\\.[0-9]{1}$";
                String pattern4 = "^[0-9]*\\.[0-9]$";

                Pattern p1 = Pattern.compile(pattern1);
                Pattern p2 = Pattern.compile(pattern2);
                Pattern p3 = Pattern.compile(pattern3);
                Pattern p4 = Pattern.compile(pattern4);

                if (value.contains(".")) {
                    boolean twoDP = p1.matcher(value).matches() || p2.matcher(value).matches();
                    boolean oneDP = p3.matcher(value).matches() || p4.matcher(value).matches();
                    if (twoDP) {
                        value = e.getAsString().substring(0, e.getAsString().indexOf(".") + 3);
                    } else if (oneDP) {
                        value = e.getAsString().substring(0, e.getAsString().indexOf(".") + 2);
                    }
                }

                double x = Double.parseDouble(value);
                return truncateDecimal(x, 2).doubleValue();
            } catch (Exception ex) {
                errorList.add("Please enter a valid value");
                return 0;
            }
        }
        errorList.add("Please enter a valid value");
        return 0;
    }

    public static boolean isBooleanValid(JsonElement e) {
        if (e != null && !e.isJsonNull()) {
            try {
                return e.getAsBoolean();
            } catch (Exception ex) {
                return false;
            }
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
    public static Date isDateValid(JsonElement e, String field) {

        if (e != null && !e.isJsonNull()) {
            try {
                if (!e.getAsString().isEmpty()) {
                    long date = Long.parseLong(e.getAsString());

                    if (date < 0) {
                        date += TimeUnit.DAYS.toMillis(1);
                    }

                    return new Date(date);
                } else {
                    return null;
                }
            } catch (ClassCastException | IllegalStateException | NumberFormatException exp) {
                errorList.add(field + " field error!");
                return null;
            }
        }
        errorList.add(field + " field error!");
        return null;
    }

    /**
     * <p>
     * Return a boolean value on whether inputted time is valid
     * </p>
     *
     * @param time the email address
     * @return boolean value, true if time is valid
     */
    public static Date isDateTimeValid(String dateTime) {
        if (dateTime.length() == DATE_TIME_FORMAT.length()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
                //format.setLenient(false);
                return format.parse(dateTime);
            } catch (ParseException | NullPointerException e) {
                return null;
            }
        }
        return null;
    }

    public static Date isBirthdayValid(JsonElement e, String field) {

        if (e != null && !e.isJsonNull()) {
            try {
                if (!e.getAsString().isEmpty()) {
                    String birthday = e.getAsString();
                    if (birthday.length() == DATE_FORMAT.length()) {

                        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                        format.setLenient(false);
                        return format.parse(birthday);
                    } else {
                        errorList.add(field + " field error!");
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (ParseException | NullPointerException | ClassCastException | IllegalStateException | NumberFormatException exp) {
                errorList.add(field + " field error!");
                return null;
            }
        }
        errorList.add(field + " field error!");
        return null;

    }

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

    /**
     * <p>
     * Return a boolean value on whether inputted occasion is valid
     * </p>
     *
     * @param occasion the occasion
     * @return boolean value, true if occasion is valid
     */
    public static boolean validOccasion(String occasion) {
        return (occasion.length() >= 0 && occasion.length() <= 500);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted subteam is valid
     * </p>
     *
     * @param subteam the subteam
     * @return boolean value, true if subteam is valid
     */
    public static boolean validSubteam(String subteam) {
        return (subteam.length() >= 0 && subteam.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted permission is valid
     * </p>
     *
     * @param permission the permission
     * @return boolean value, true if permission is valid
     */
    public static boolean validPermission(String permission) {
        return (permission.length() >= 1 && permission.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted team is valid
     * </p>
     *
     * @param team the team
     * @return boolean value, true if team is valid
     */
    public static boolean validTeam(String team) {
        return (team.length() >= 1 && team.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted course is valid
     * </p>
     *
     * @param course the course
     * @return boolean value, true if course is valid
     */
    public static boolean validCourse(String course) {
        return (course.length() >= 0 && course.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted trainer is valid
     * </p>
     *
     * @param trainer the trainer
     * @return boolean value, true if trainer is valid
     */
    public static boolean validTrainer(String trainer) {
        return (trainer.length() >= 0 && trainer.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted comments is valid
     * </p>
     *
     * @param comments the comments
     * @return boolean value, true if comments is valid
     */
    public static boolean validComments(String comments) {
        return (comments.length() >= 0 && comments.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted gesture is valid
     * </p>
     *
     * @param gesture the gesture
     * @return boolean value, true if gesture is valid
     */
    public static boolean validGesture(String gesture) {
        return (gesture.length() >= 0 && gesture.length() <= 500);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted appreciationBy is valid
     * </p>
     *
     * @param appreciationBy the appreciationBy
     * @return boolean value, true if appreciationBy is valid
     */
    public static boolean validAppreciationBy(String appreciationBy) {
        return (appreciationBy.length() >= 0 && appreciationBy.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted proxyStanding is valid
     * </p>
     *
     * @param proxyStanding the proxyStanding
     * @return boolean value, true if proxyStanding is valid
     */
    public static boolean validProxyStanding(String proxyStanding) {
        return (proxyStanding.length() >= 0 && proxyStanding.length() <= 500);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted proxyOf is valid
     * </p>
     *
     * @param proxyOf the proxyOf
     * @return boolean value, true if proxyOf is valid
     */
    public static boolean validProxyOf(String proxyOf) {
        return (proxyOf.length() >= 0 && proxyOf.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted language is valid
     * </p>
     *
     * @param language the language
     * @return boolean value, true if language is valid
     */
    public static boolean validLanguage(String language) {
        return (language.length() >= 1 && language.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted speakWrite is valid
     * </p>
     *
     * @param speakWrite the speakWrite
     * @return boolean value, true if speakWrite is valid
     */
    public static boolean validSpeakWrite(String speakWrite) {
        return (speakWrite.length() >= 0 && speakWrite.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted skill is valid
     * </p>
     *
     * @param skill the skill
     * @return boolean value, true if skill is valid
     */
    public static boolean validSkill(String skill) {
        return (skill.length() >= 1 && skill.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted eventTitle is valid
     * </p>
     *
     * @param eventTitle the eventTitle
     * @return boolean value, true if eventTitle is valid
     */
    public static boolean validEventTitle(String eventTitle) {
        return (eventTitle.length() >= 1 && eventTitle.length() <= 200);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted eventClass is valid
     * </p>
     *
     * @param eventClass the eventClass
     * @return boolean value, true if eventClass is valid
     */
    public static boolean validEventClass(String eventClass) {
        return (eventClass.length() >= 1 && eventClass.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted eventRole is valid
     * </p>
     *
     * @param eventRole the eventRole
     * @return boolean value, true if eventRole is valid
     */
    public static boolean validEventRole(String eventRole) {
        return (eventRole.length() >= 1 && eventRole.length() <= 50);
    }

    /**
     * <p>
     * Return a boolean value on whether inputted eventRoleDescription is valid
     * </p>
     *
     * @param eventRoleDescription the eventRoleDescription
     * @return boolean value, true if eventRoleDescription is valid
     */
    public static boolean validEventRoleDescription(String eventRoleDescription) {
        return (eventRoleDescription.length() >= 0 && eventRoleDescription.length() <= 200);
    }

    public static boolean validEventLeaderPosition(int contactId, int eventId) {
        ContactDAO contactDAO = new ContactDAO();
        Contact contact = contactDAO.retrieveContactById(contactId);
        boolean isEventLeader = false;
        EventAffiliation eventAffiliation = EventAffiliationDAO.retrieveAllEventAffiliation(eventId);
        if (contact != null && eventAffiliation != null) {
            ArrayList<String> teamNameList = eventAffiliation.getTeamArray();
            Iterator iter = teamNameList.iterator();
            while (iter.hasNext()) {
                String teamName = (String) iter.next();
                if (RoleCheckDAO.checkRole(contactId, teamName, "eventleader")) {
                    isEventLeader = true;
                }
            }
        }
        return isEventLeader;
    }

}
