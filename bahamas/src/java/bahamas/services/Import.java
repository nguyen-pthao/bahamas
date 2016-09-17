/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AddressDAO;
import bahamas.dao.AppreciationDAO;
import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.DonationDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.LanguageDAO;
import bahamas.dao.MembershipDAO;
import bahamas.dao.OfficeHeldDAO;
import bahamas.dao.PhoneDAO;
import bahamas.dao.SkillDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.dao.TrainingDAO;
import bahamas.dao.list.*;
import bahamas.entity.Address;
import bahamas.entity.Appreciation;
import bahamas.entity.Contact;
import bahamas.entity.Donation;
import bahamas.entity.Email;
import bahamas.entity.LanguageAssignment;
import bahamas.entity.Membership;
import bahamas.entity.OfficeHeld;
import bahamas.entity.Phone;
import bahamas.entity.SkillAssignment;
import bahamas.entity.TeamJoin;
import bahamas.entity.Training;
import bahamas.util.Authenticator;
import bahamas.util.PasswordHash;
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author HUXLEY
 */
@WebServlet(name = "Import", urlPatterns = {"/import"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class Import extends HttpServlet {

    private LinkedHashMap<Integer, ArrayList<String>> logMsg;
    private ArrayList<String> dataList;
    private static final Logger LOGGER = Logger.getLogger(Import.class.getName());
    private static int numOfFields;
    private static String createdBy;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/JSON;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            logMsg = new LinkedHashMap<Integer, ArrayList<String>>();
            JsonObject json = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Part filePart = request.getPart("file");
            String token = request.getParameter("token");
            String table = request.getParameter("table");

            if (token == null || token.isEmpty()) {
                json.addProperty("message", "Import failed due authorized");
                out.println(gson.toJson(json));
                return;
            }

            String username = Authenticator.verifyToken(token);
            ContactDAO cDAO = new ContactDAO();

            if (username == null || cDAO.retrieveContactByUsername(username) == null
                    || !cDAO.retrieveContactByUsername(username).isIsAdmin()) {
                json.addProperty("message", "Import failed due authorized");
                out.println(gson.toJson(json));
                return;
            }

            createdBy = username;

            if (filePart == null) {
                LOGGER.log(Level.SEVERE, "UploadFile Line70: File is NULL");
                json.addProperty("message", "Import failed due empty file");
                out.println(gson.toJson(json));
                return;

            } else {

                String fileName = filePart.getSubmittedFileName();
                LOGGER.log(Level.INFO, "UploadFile Line75: Filename : " + fileName);

                if (table.equalsIgnoreCase("contact")) {
                    numOfFields = 12;
                } else if (table.equalsIgnoreCase("phone")) {
                    numOfFields = 5;
                } else if (table.equalsIgnoreCase("email")) {
                    numOfFields = 4;
                } else if (table.equalsIgnoreCase("address")) {
                    numOfFields = 6;
                } else if (table.equalsIgnoreCase("team join")) {
                    numOfFields = 7;
                } else if (table.equalsIgnoreCase("membership")) {
                    numOfFields = 14;
                } else if (table.equalsIgnoreCase("language")) {
                    numOfFields = 6;
                } else if (table.equalsIgnoreCase("skill")) {
                    numOfFields = 5;
                } else if (table.equalsIgnoreCase("office held")) {
                    numOfFields = 5;
                } else if (table.equalsIgnoreCase("appreciation")) {
                    numOfFields = 8;
                } else if (table.equalsIgnoreCase("donation")) {
                    numOfFields = 19;
                } else if (table.equalsIgnoreCase("training")) {
                    numOfFields = 7;
                } else {
                    json.addProperty("message", "Import failed due invalid table choice");
                    out.println(gson.toJson(json));
                    return;
                }

                if (fileName.contains(".csv")) {
                    processCsv(filePart);
                } else if (fileName.contains(".xls") || fileName.contains(".xlsx")) {
                    processExcel(filePart);
                } else {
                    json.addProperty("message", "Import failed due to wrong file format");
                    out.println(gson.toJson(json));
                    return;
                }

                if (dataList.isEmpty()) {
                    json.addProperty("message", "Import failed due to wrong file or data format");
                    out.println(gson.toJson(json));
                    return;
                }

                try {
                    if (table.equalsIgnoreCase("contact")) {
                        processContact();
                    } else if (table.equalsIgnoreCase("phone")) {
                        processPhone();
                    } else if (table.equalsIgnoreCase("email")) {
                        processEmail();
                    } else if (table.equalsIgnoreCase("address")) {
                        processAddress();
                    } else if (table.equalsIgnoreCase("team join")) {
                        processTeamJoin();
                    } else if (table.equalsIgnoreCase("membership")) {
                        processMembership();
                    } else if (table.equalsIgnoreCase("language")) {
                        processLanguage();
                    } else if (table.equalsIgnoreCase("skill")) {
                        processSkill();
                    } else if (table.equalsIgnoreCase("office held")) {
                        processOfficeHeld();
                    } else if (table.equalsIgnoreCase("appreciation")) {
                        processAppreciation();
                    } else if (table.equalsIgnoreCase("donation")) {
                        processDonation();
                    } else if (table.equalsIgnoreCase("training")) {
                        processTraining();
                    } else {
                        json.addProperty("message", "Import failed due invalid table choice");
                        out.println(gson.toJson(json));
                        return;
                    }

                } catch (Exception ex) {
                    json.addProperty("message", "Import failed due to wrong file or data format");
                    out.println(gson.toJson(json));
                    return;
                }

                JsonArray records = new JsonArray();
                int counter = 0;
                Iterator<Integer> iterLine = logMsg.keySet().iterator();
                while (iterLine.hasNext()) {
                    int num = iterLine.next();
                    ArrayList<String> temp = logMsg.get(num);

                    JsonObject store = new JsonObject();

                    store.addProperty("line", num);

                    if (temp.isEmpty()) {
                        temp.add("Successfully added");
                        counter++;
                    }

                    JsonArray errorArray = new JsonArray();
                    for (String s : temp) {
                        JsonPrimitive o = new JsonPrimitive(s);
                        errorArray.add(o);
                    }

                    store.add("message", errorArray);
                    records.add(store);

                }

                AuditLogDAO.insertAuditLog(username, "IMPORT DATA INTO " + table.toUpperCase(), counter + " number of records added");

                json.add(table.toLowerCase(), records);
                out.println(gson.toJson(json));
                return;

            }

        }
    }

    private void processContact() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();
            String name = processField(msg, dataList.get(i), "Name", 50);

            if (name == null) {
                msg.add("Name cannot be empty");
            }

            String altName = processField(msg, dataList.get(++i), "Alt Name", 50);
            String contactType = processField(msg, dataList.get(++i), "Contact Type", 50);

            ContactTypeListDAO listDAO = new ContactTypeListDAO();
            if (!listDAO.retrieveAllContactTypeList().contains(contactType)) {
                msg.add("Contact Type not referencing to Contact Type List");
            }

            String otherExplanation = processField(msg, dataList.get(++i), "Explain if other", 200);
            String profession = processField(msg, dataList.get(++i), "Profession", 200);
            String jobTitle = processField(msg, dataList.get(++i), "Job Title", 50);
            String nric = processField(msg, dataList.get(++i), "NRIC/FIN", 9);

            String gender = processField(msg, dataList.get(++i), "Gender", 1);
            if (gender != null && !(gender.equals("M") || gender.equals("F") || gender.equals("O"))) {
                msg.add("Invalid Gender Format");
            }

            String nationality = processField(msg, dataList.get(++i), "Nationality", 20);
            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            String date = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date dob = null;
            if (!date.isEmpty()) {
                try {
                    dob = format.parse(date);
                } catch (ParseException e) {
                    msg.add("Invalid date of birth format");
                }
            }

            boolean notification = true;
            String notify = dataList.get(++i);

            if (notify.equalsIgnoreCase("false")) {
                notification = false;
            } else if (notify.equalsIgnoreCase("true")) {
                notification = true;
            } else {
                msg.add("Invalid notification boolean format");
            }

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                //Create new contact object
                Contact c = new Contact(name, altName, contactType,
                        otherExplanation, profession, jobTitle, nric,
                        gender, nationality, dob, remarks, notification, createdBy);

                if (ContactDAO.addContact(c) > 0) {
                } else {
                    msg.add("Error inserting to database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processAddress() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            try {
                ContactDAO cDAO = new ContactDAO();
                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String address = processField(msg, dataList.get(++i), "Address", 1000);

            if (address == null) {
                msg.add("Name cannot be empty");
            }

            String[] locales = Locale.getISOCountries();
            ArrayList<String> countryList = new ArrayList<String>();
            for (String countryCode : locales) {
                Locale obj = new Locale("", countryCode);
                countryList.add(obj.getDisplayCountry());
            }

            String country = processField(msg, dataList.get(++i), "Country", 50);
            if (country != null && !countryList.isEmpty() && !countryList.contains(country)) {
                msg.add("Invalid country choice");
            }

            String zipcode = processField(msg, dataList.get(++i), "Zipcode", 20);

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            String date = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date dateObsolete = null;
            if (!date.isEmpty()) {
                try {
                    dateObsolete = format.parse(date);
                } catch (ParseException e) {
                    msg.add("Invalid date obsolete format");
                }
            }

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                Address newAddress = new Address(c, country, zipcode, address,
                        createdBy, remarks, dateObsolete);

                if (AddressDAO.addAddress(newAddress)) {
                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processEmail() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            try {
                ContactDAO cDAO = new ContactDAO();
                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String email = processField(msg, dataList.get(++i), "Email", 50);

            if (email == null) {
                msg.add("Email cannot be empty");
            } else if (!Validator.validEmail(email)) {
                msg.add("Invalid email format");
            } else if (EmailDAO.emailExist(email)) {
                msg.add("Email already exist");
            }

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            String date = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date dateObsolete = null;
            if (!date.isEmpty()) {
                try {
                    dateObsolete = format.parse(date);
                } catch (ParseException e) {
                    msg.add("Invalid date obsolete format");
                }
            }

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                Email newEmail = new Email(c, email, createdBy, remarks, dateObsolete, false);

                if (EmailDAO.addEmail(newEmail, null)) {
                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processPhone() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            try {
                ContactDAO cDAO = new ContactDAO();
                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String countrycode = processField(msg, dataList.get(++i), "Country Code", 20);

            if (countrycode == null) {
                msg.add("Country code cannot be empty");
            }

            int countryCode = 0;
            try {
                countryCode = Integer.parseInt(countrycode);
            } catch (NumberFormatException e) {
                msg.add("Invalid Country code");
            }

            String phoneNumber = processField(msg, dataList.get(++i), "Phone Number", 20);

            if (c != null && PhoneDAO.phoneExist(c.getContactId(), countryCode, phoneNumber)) {
                msg.add("Phone number already exists");
            }

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            String date = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date dateObsolete = null;
            if (!date.isEmpty()) {
                try {
                    dateObsolete = format.parse(date);
                } catch (ParseException e) {
                    msg.add("Invalid date obsolete format");
                }
            }

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                Phone newPhone = new Phone(c, countryCode, phoneNumber, createdBy, remarks, dateObsolete);

                if (PhoneDAO.addPhone(newPhone)) {

                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processTeamJoin() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            ContactDAO cDAO = new ContactDAO();
            try {

                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String teamName = processField(msg, dataList.get(++i), "Team Name", 50);
            TeamAffiliationListDAO teamListDAO = new TeamAffiliationListDAO();

            if (!teamListDAO.retrieveTeamAffiliationList().contains(teamName)) {
                msg.add("Team Name not referencing to Team Afflialiation List");
            } else if (c != null && TeamJoinDAO.teamJoinExist(c.getContactId(), teamName)) {
                msg.add("Team preference already exists");
            }

            String explainIfOther = processField(msg, dataList.get(++i), "Explain If Other", 200);
            String subTeam = processField(msg, dataList.get(++i), "Sub Team", 50);

            String permission = processField(msg, dataList.get(++i), "Permission", 50);
            PermissionLevelListDAO permissionList = new PermissionLevelListDAO();
            if (permission != null) {
                if (!permissionList.retrievePermissionLevelList().contains(permission)) {
                    msg.add("Permission not referencing to Permission List");
                }
            }

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            String date = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date dateObsolete = null;
            if (!date.isEmpty()) {
                try {
                    dateObsolete = format.parse(date);
                } catch (ParseException e) {
                    msg.add("Invalid date obsolete format");
                }
            }

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                TeamJoin tj = new TeamJoin(c, teamName, createdBy, explainIfOther,
                        subTeam, dateObsolete, remarks, permission);

                if (TeamJoinDAO.addTeamJoin(tj)) {
                    if (permission != null) {
                        cDAO.changeNovicePermission(c, false);
                    }
                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processTraining() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            ContactDAO cDAO = new ContactDAO();
            try {

                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String teamName = processField(msg, dataList.get(++i), "Team Name", 50);
            TeamAffiliationListDAO teamListDAO = new TeamAffiliationListDAO();

            if (!teamListDAO.retrieveTeamAffiliationList().contains(teamName)) {
                msg.add("Team Name not referencing to Team Afflialiation List");
            } else if (c != null && TeamJoinDAO.teamJoinExist(c.getContactId(), teamName)) {
                msg.add("Team preference already exists");
            }

            String explainIfOther = processField(msg, dataList.get(++i), "Explain If Other", 200);

            String trainingCourse = processField(msg, dataList.get(++i), "Training Course", 50);
            String trainingBy = processField(msg, dataList.get(++i), "Training By", 50);

            String date = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date trainingDate = null;
            if (!date.isEmpty()) {
                try {
                    trainingDate = format.parse(date);
                } catch (ParseException e) {
                    msg.add("Invalid training date format");
                }
            }

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                Training newTraining = new Training(c, createdBy, teamName, explainIfOther,
                        trainingCourse, trainingBy, trainingDate, remarks);

                if (TrainingDAO.addTraining(newTraining)) {

                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processMembership() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            ContactDAO cDAO = new ContactDAO();
            try {

                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String membershipClassName = processField(msg, dataList.get(++i), "Membership Class Name", 50);
            MembershipClassListDAO membershipClassListDAO = new MembershipClassListDAO();

            if (membershipClassName == null) {
                msg.add("Membership Class Name cannot be empty");
            } else if (!membershipClassListDAO.retrieveMembershipClassList().contains(membershipClassName)) {
                msg.add("Membership Class Name not referencing to Membership Class Name List");
            }

            String explainIfOtherClass = processField(msg, dataList.get(++i), "Explain If Other Class", 200);

            String startMembershipDate = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date startMembership = null;
            if (!startMembershipDate.isEmpty()) {
                try {
                    startMembership = format.parse(startMembershipDate);
                } catch (ParseException e) {
                    msg.add("Invalid membership start date format");
                }
            } else {
                msg.add("Membership start date cannot be empty");
            }

            String endMembershipDate = dataList.get(++i);
            //SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            //format.setLenient(false);
            Date endMembership = null;
            if (!endMembershipDate.isEmpty()) {
                try {
                    endMembership = format.parse(endMembershipDate);
                } catch (ParseException e) {
                    msg.add("Invalid membership end date format");
                }
            } else {
                msg.add("Membership end date cannot be empty");
            }

            double subAmt = 0;
            String subcriptionAmt = dataList.get(++i);
            if (!subcriptionAmt.isEmpty()) {
                try {
                    subAmt = Validator.truncateDecimal(Double.parseDouble(subcriptionAmt), 2).doubleValue();
                } catch (Exception e) {
                    msg.add("Invalid subscription amount format");
                }
            } else {
                msg.add("Subscription amount cannot be empty");
            }

            String paymentMode = processField(msg, dataList.get(++i), "Payment Mode", 50);
            PaymentModeListDAO paymentModeListDAO = new PaymentModeListDAO();
            if (paymentMode != null && !paymentModeListDAO.retrievePaymentModeList().contains(paymentMode)) {
                msg.add("Payment Mode Name not referencing to Payment Mode Name List");
            }

            String explainIfOtherPayment = processField(msg, dataList.get(++i), "Explain If Other Payment", 200);

            String extTransactionRef = processField(msg, dataList.get(++i), "Ext Transaction Ref", 50);

            String receiptNumber = processField(msg, dataList.get(++i), "Receipt Number", 50);

            String receiptModeName = processField(msg, dataList.get(++i), "Receipt Mode Name", 50);
            ModeOfSendingReceiptListDAO receiptModeDAO = new ModeOfSendingReceiptListDAO();
            if (receiptModeName != null && !receiptModeDAO.retrieveMOSRList().contains(receiptModeName)) {
                msg.add("Receipt Mode Name not referecning to Receipt Mode Name List");
            }

            String receiptDateReceived = dataList.get(++i);
            Date receiptDate = null;
            if (!receiptDateReceived.isEmpty()) {
                try {
                    receiptDate = format.parse(receiptDateReceived);
                } catch (ParseException e) {
                    msg.add("Invalid receipt date format");
                }
            }

            String explainIfOtherReceipt = processField(msg, dataList.get(++i), "Explain If Other Receipt", 200);

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                Membership m = new Membership(c, startMembership, endMembership, receiptDate, subAmt, extTransactionRef,
                        receiptNumber, remarks, receiptModeName, explainIfOtherReceipt, membershipClassName, explainIfOtherClass, paymentMode, explainIfOtherPayment,
                        createdBy);

                if (MembershipDAO.addMembership(m)) {

                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processLanguage() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            ContactDAO cDAO = new ContactDAO();
            try {

                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String language = processField(msg, dataList.get(++i), "Language", 50);
            LanguageListDAO languageListDAO = new LanguageListDAO();
            if (!languageListDAO.retrieveLanguageList().contains(language)) {
                msg.add("Language not referencing to Language List");
            } else if (c != null && LanguageDAO.languageExist(c.getContactId(), language)) {
                msg.add("Language already exists");
            }

            //ArrayList<String> proficiency = new ArrayList<String>();
            //proficiency.add("speak only");
            //proficiency.add("speak and write");
            String speakWrite = processField(msg, dataList.get(++i), "Speak/Write", 50);
            //if (!proficiency.contains(speakWrite)) {
            //    msg.add("Speak/Write not referencing proficiency list");
            //}

            String explainIfOther = processField(msg, dataList.get(++i), "Explain If Other", 200);

            String date = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date dateObsolete = null;
            if (!date.isEmpty()) {
                try {
                    dateObsolete = format.parse(date);
                } catch (ParseException e) {
                    msg.add("Invalid date obsolete format");
                }
            }

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                LanguageAssignment la = new LanguageAssignment(c, language, explainIfOther,
                        dateObsolete, speakWrite, remarks, createdBy);

                if (LanguageDAO.addLanguage(la)) {

                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processSkill() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            ContactDAO cDAO = new ContactDAO();
            try {

                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String skillName = processField(msg, dataList.get(++i), "Skill Name", 50);
            LSAClassListDAO skillListDAO = new LSAClassListDAO();
            if (!skillListDAO.retrieveLSAClassList().contains(skillName)) {
                msg.add("Skill Name not referencing to LSA Class List");
            } else if (c != null && SkillDAO.skillExist(c.getContactId(), skillName)) {
                msg.add("Skill Name already exists");
            }

            String explainIfOther = processField(msg, dataList.get(++i), "Explain If Other", 200);

            String date = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date dateObsolete = null;
            if (!date.isEmpty()) {
                try {
                    dateObsolete = format.parse(date);
                } catch (ParseException e) {
                    msg.add("Invalid date obsolete format");
                }
            }

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                SkillAssignment sa = new SkillAssignment(c, skillName, explainIfOther,
                        dateObsolete, remarks, createdBy);

                if (SkillDAO.addSkill(sa)) {

                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processOfficeHeld() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            ContactDAO cDAO = new ContactDAO();
            try {

                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String startOffice = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date startOfficeDate = null;
            if (!startOffice.isEmpty()) {
                try {
                    startOfficeDate = format.parse(startOffice);
                } catch (ParseException e) {
                    msg.add("Invalid start office date format");
                }
            } else {
                msg.add("Start office date cannot be empty");
            }

            String endOffice = dataList.get(++i);
            Date endOfficeDate = null;
            if (!endOffice.isEmpty()) {
                try {
                    endOfficeDate = format.parse(endOffice);
                } catch (ParseException e) {
                    msg.add("Invalid end office date format");
                }
            } else {
                msg.add("End office date cannot be empty");
            }

            String officeHeld = processField(msg, dataList.get(++i), "Office Held Name", 50);
            OfficeListDAO officeListDAO = new OfficeListDAO();
            if (!officeListDAO.retrieveOfficeList().contains(officeHeld)) {
                msg.add("Office Held Name not referencing to Office Held Name List");
            }

            if (startOfficeDate != null && endOfficeDate != null) {
                if (endOfficeDate.before(startOfficeDate) || !startOfficeDate.before(endOfficeDate)) {
                    msg.add("Start office date must be before end office date");
                }

                if (c != null && OfficeHeldDAO.officeHeldExist(c.getContactId(), startOfficeDate, endOfficeDate, officeHeld)) {
                    msg.add("Office Held already exists");
                }
            }

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                OfficeHeld o = new OfficeHeld(c, startOfficeDate, endOfficeDate, remarks, createdBy, officeHeld);

                if (OfficeHeldDAO.addOfficeHeld(o)) {

                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processAppreciation() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            ContactDAO cDAO = new ContactDAO();
            try {

                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String appraisalComment = processField(msg, dataList.get(++i), "Appraisal Comments", 500);
            String appraisalBy = processField(msg, dataList.get(++i), "Appraisal By", 50);

            String appraisalD = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date appraisalDate = null;
            if (!appraisalD.isEmpty()) {
                try {
                    appraisalDate = format.parse(appraisalD);
                } catch (ParseException e) {
                    msg.add("Invalid appraisal date format");
                }
            }

            String appreciationGesture = processField(msg, dataList.get(++i), "Appreciation gesture", 500);
            String appreciationBy = processField(msg, dataList.get(++i), "Appreciation by", 50);

            String appreciationD = dataList.get(++i);
            Date appreciationDate = null;
            if (!appreciationD.isEmpty()) {
                try {
                    appreciationDate = format.parse(appreciationD);
                } catch (ParseException e) {
                    msg.add("Invalid end office date format");
                }
            }

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                Appreciation appreciation = new Appreciation(c, appraisalComment, appraisalBy, appraisalDate, appreciationGesture,
                        appreciationBy, appreciationDate, remarks, createdBy);

                if (AppreciationDAO.addAppreciation(appreciation)) {

                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    private void processDonation() throws Exception {

        int lineNum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            ArrayList<String> msg = new ArrayList<>();

            String contactId = processField(msg, dataList.get(i), "Contact Id", 11);

            Contact c = null;
            ContactDAO cDAO = new ContactDAO();
            try {

                c = cDAO.retrieveContactById(Integer.parseInt(contactId));
                if (c == null) {
                    msg.add("Invalid contact id reference");
                }
            } catch (NumberFormatException | NullPointerException e) {
                msg.add("Invalid contact id reference");
            }

            String dateR = dataList.get(++i);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            format.setLenient(false);
            Date dateReceived = null;
            if (!dateR.isEmpty()) {
                try {
                    dateReceived = format.parse(dateR);
                } catch (ParseException e) {
                    msg.add("Invalid date received format");
                }
            } else {
                msg.add("Date recevied cannot be empty");
            }

            double donationAmt = 0;
            String dAmt = dataList.get(++i);
            if (!dAmt.isEmpty()) {
                try {
                    donationAmt = Validator.truncateDecimal(Double.parseDouble(dAmt), 2).doubleValue();
                } catch (Exception e) {
                    msg.add("Invalid donation amount format");
                }
            } else {
                msg.add("Donation amount cannot be empty");
            }

            String paymentMode = processField(msg, dataList.get(++i), "Payment Mode", 50);
            PaymentModeListDAO paymentModeListDAO = new PaymentModeListDAO();
            if (paymentMode != null && !paymentModeListDAO.retrievePaymentModeList().contains(paymentMode)) {
                msg.add("Payment Mode Name not referencing to Payment Mode Name List");
            }

            String explainIfOtherPayment = processField(msg, dataList.get(++i), "Explain If Other Payment", 200);
            String extTransactionRef = processField(msg, dataList.get(++i), "Ext Transaction Ref", 50);

            String receiptNumber = processField(msg, dataList.get(++i), "Receipt Number", 50);
            String receiptDateReceived = dataList.get(++i);
            Date receiptDate = null;
            if (!receiptDateReceived.isEmpty()) {
                try {
                    receiptDate = format.parse(receiptDateReceived);
                } catch (ParseException e) {
                    msg.add("Invalid receipt date format");
                }
            }

            String receiptModeName = processField(msg, dataList.get(++i), "Receipt Mode Name", 50);
            ModeOfSendingReceiptListDAO receiptModeDAO = new ModeOfSendingReceiptListDAO();
            if (receiptModeName != null && !receiptModeDAO.retrieveMOSRList().contains(receiptModeName)) {
                msg.add("Receipt Mode Name not referecning to Receipt Mode Name List");
            }
            String explainIfOtherReceipt = processField(msg, dataList.get(++i), "Explain If Other Receipt", 200);
            String donorInstruction = processField(msg, dataList.get(++i), "Donor Instructions", 1000);

            String allocation1 = processField(msg, dataList.get(++i), "Allocation 1", 200);
            double subAmount1 = 0;
            String subAmt1 = dataList.get(++i);
            if (!subAmt1.isEmpty()) {
                try {
                    subAmount1 = Validator.truncateDecimal(Double.parseDouble(subAmt1), 2).doubleValue();
                } catch (Exception e) {
                    msg.add("Invalid sub amount 1 format");
                }
            }

            String allocation2 = processField(msg, dataList.get(++i), "Allocation 2", 200);
            double subAmount2 = 0;
            String subAmt2 = dataList.get(++i);
            if (!subAmt2.isEmpty()) {
                try {
                    subAmount2 = Validator.truncateDecimal(Double.parseDouble(subAmt2), 2).doubleValue();
                } catch (Exception e) {
                    msg.add("Invalid sub amount 2 format");
                }
            }

            String allocation3 = processField(msg, dataList.get(++i), "Allocation 3", 200);
            double subAmount3 = 0;
            String subAmt3 = dataList.get(++i);
            if (!subAmt3.isEmpty()) {
                try {
                    subAmount3 = Validator.truncateDecimal(Double.parseDouble(subAmt3), 2).doubleValue();
                } catch (Exception e) {
                    msg.add("Invalid sub amount 3 format");
                }
            }

            String associatedOccasion = processField(msg, dataList.get(++i), "Associated Occasion", 500);

            String remarks = processField(msg, dataList.get(++i), "Remarks", 1000);

            lineNum++;

            if (!msg.isEmpty()) {
                logMsg.put(lineNum, msg);
            } else {

                Donation d = new Donation(c, createdBy, dateReceived, donationAmt, paymentMode, explainIfOtherPayment,
                        extTransactionRef, receiptModeName, receiptNumber, receiptDate, explainIfOtherReceipt, donorInstruction, allocation1, subAmount1,
                        allocation2, subAmount2, allocation3, subAmount3, associatedOccasion, remarks);

                if (DonationDAO.addDonation(d)) {

                } else {
                    msg.add("Error inserting into database");
                }
                logMsg.put(lineNum, msg);

            }

        }
    }

    //Methods to process fields | csv | excel 
    private String processField(ArrayList<String> temp, String value, String fieldName, int max) {
        if (value.isEmpty()) {
            return null;
        }

        if (value.length() > max) {
            temp.add("Invalid " + fieldName + ", max length of " + max);
        }

        return value;
    }

    private void processCsv(Part filePart) throws IOException {
        InputStream istream = filePart.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(istream);
        Scanner sc = new Scanner(bis);
        //String columns = sc.nextLine().trim();
        //COL = columns.split(",");
        sc.useDelimiter(",|\r\n");
        sc.nextLine();
        dataList = new ArrayList<String>();
        while (sc.hasNext()) {
            String content = sc.next().trim();
            dataList.add(content);
            LOGGER.log(Level.INFO, content);
        }
    }

    private void processExcel(Part filePart) throws IOException {
        InputStream istream = filePart.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(istream);
        Workbook workbook;

        LOGGER.log(Level.INFO, "DEBUG0: " + filePart.getSubmittedFileName());
        if (filePart.getSubmittedFileName().contains(".xlsx")) {
            workbook = new XSSFWorkbook(bis);
        } else {
            workbook = new HSSFWorkbook(bis);
        }

        Sheet sheet = workbook.getSheetAt(0);

        Iterator rowIter = sheet.rowIterator();

        DataFormatter df = new DataFormatter();
        dataList = new ArrayList<>();

        rowIter.next(); //skip the first blank line
        /*
        LOGGER.log(Level.INFO, "DEBUG2: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG3: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG4: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG5: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG6: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG7: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG8: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG9: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        //rowIter.next();
        LOGGER.log(Level.INFO, "DEBUG11: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG12: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
         */
        //Row colRow = (Row) rowIter.next();

        /*
        ArrayList<String> list = new ArrayList<>();
        Iterator colIter = colRow.cellIterator();
        //if (!df.formatCellValue((Cell) colRow.getCell(1)).isEmpty()) {
            while (colIter.hasNext()) {
                Cell cell = (Cell) colIter.next();
                LOGGER.log(Level.INFO, "COLUMN: " + df.formatCellValue(cell));
                String cellVal = df.formatCellValue(cell).toString();
                //cellVal = cellVal.replace(".", "\\uFF0E"); //mongoDB doesn't accept key name containing "."
                list.add(cellVal);
            }
            COL = list.toArray(new String[0]);
        //}
         */
        while (rowIter.hasNext()) {
            Row row = (Row) rowIter.next();
            //Iterator cellIter = row.cellIterator();
            int counter = 0;
            //if (cellIter.hasNext() ) {
            //String content = "";
            while (counter < numOfFields) {

                String tempContent = df.formatCellValue(row.getCell(counter));
                //content += tempContent;
                //content += ",";
                dataList.add(tempContent);

                counter++;
            }
            /*
                if (!content.equals("")) {
                    content = content.substring(0, content.length() - 1);
                    LOGGER.log(Level.INFO, "ROW CONTENT: " + content);
                    dataList.add(content);
                }
             */

            //} else {
            //LOGGER.log(Level.INFO, "DEBUG: cellIter does NOT have Next Element");
            //break;
            //}
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
