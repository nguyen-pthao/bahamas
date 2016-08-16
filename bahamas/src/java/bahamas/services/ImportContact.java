/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.ContactDAO;
import bahamas.dao.list.ContactTypeListDAO;
import bahamas.entity.Contact;
import bahamas.util.Authenticator;
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
@WebServlet(name = "ImportContact", urlPatterns = {"/contact.import"})
public class ImportContact extends HttpServlet {

    private LinkedHashMap<Integer, ArrayList<String>> errorMsg = new LinkedHashMap<Integer, ArrayList<String>>();
    private ArrayList<String> contactList;
    private String[] COL;
    private static final Logger LOGGER = Logger.getLogger(ImportContact.class.getName());

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

            JsonObject json = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Part filePart = request.getPart("file");
            String token = request.getParameter("token");

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

            if (filePart == null) {
                LOGGER.log(Level.SEVERE, "UploadFile Line70: File is NULL");
                json.addProperty("message", "Import failed due empty file");
                out.println(gson.toJson(json));
                return;

            } else {

                String fileName = filePart.getSubmittedFileName();
                LOGGER.log(Level.INFO, "UploadFile Line75: Filename : " + fileName);

                if (fileName.contains(".csv")) {
                    processCsv(filePart);
                } else if (fileName.contains(".xls") || fileName.contains(".xlsx")) {
                    processExcel(filePart);
                } else {
                    json.addProperty("message", "Import failed due to wrong file format");
                    out.println(gson.toJson(json));
                    return;
                }

                if (contactList.isEmpty()) {
                    json.addProperty("message", "Import failed due to wrong file or data format");
                    out.println(gson.toJson(json));
                    return;
                }

                for (int i = 0; i < contactList.size(); i++) {

                    ArrayList<String> msg = new ArrayList<String>();

                    int lineNum = i + 1;

                    String name = processField(msg, contactList.get(i), "Name", 50);

                    if (name == null) {
                        msg.add("Name cannot be empty");
                    }

                    String altName = processField(msg, contactList.get(++i), "Alt Name", 50);
                    String contactType = processField(msg, contactList.get(++i), "Contact Type", 50);

                    ContactTypeListDAO listDAO = new ContactTypeListDAO();
                    if (!listDAO.retrieveAllContactTypeList().contains(contactType)) {
                        msg.add("Contact Type not referencing to Contact Type List");
                    }

                    String otherExplanation = processField(msg, contactList.get(++i), "Explain if other", 200);
                    String profession = processField(msg, contactList.get(++i), "Profession", 200);
                    String jobTitle = processField(msg, contactList.get(++i), "Job Title", 50);
                    String nric = processField(msg, contactList.get(++i), "NRIC/FIN", 9);

                    String gender = processField(msg, contactList.get(++i), "Gender", 1);
                    if (!(gender.equals("M") || gender.equals("F") || gender.equals("O"))) {
                        msg.add("Invalid Gender Format");
                    }

                    String nationality = processField(msg, contactList.get(++i), "Nationality", 20);
                    String remarks = processField(msg, contactList.get(++i), "Remarks", 1000);

                    String date = contactList.get(++i);
                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    Date dob = null;
                    if (!date.isEmpty()) {
                        try {
                            dob = format.parse(date);
                        } catch (ParseException e) {
                            msg.add("Invalid date format");
                        }
                    }

                    boolean notification = true;
                    String notify = contactList.get(++i);
                    if (notify.equals("false")) {
                        notification = false;
                    } else if (notify.equals("true")) {
                        notification = true;
                    } else {
                        msg.add("Invalid notification boolean format");
                    }

                    if (!msg.isEmpty()) {
                        errorMsg.put(lineNum, msg);
                    } else {

                        //Create new contact object
                        Contact c = new Contact(name, altName, contactType,
                                otherExplanation, profession, jobTitle, nric,
                                gender, nationality, dob, remarks, notification, username);

                        ContactDAO.addContact(c);

                        msg.add("Successfully added");
                        errorMsg.put(lineNum, msg);

                    }

                }

                JsonArray records = new JsonArray();

                Iterator<Integer> iterLine = errorMsg.keySet().iterator();
                while (iterLine.hasNext()) {
                    int num = iterLine.next();
                    ArrayList<String> temp = errorMsg.get(num);

                    JsonObject store = new JsonObject();

                    store.addProperty("line", num);

                    if (temp.isEmpty()) {
                        temp.add("Successfully added");
                    }

                    JsonArray errorArray = new JsonArray();
                    for (String s : temp) {
                        JsonPrimitive o = new JsonPrimitive(s);
                        errorArray.add(o);
                    }

                    store.add("output", errorArray);
                    records.add(store);

                }

                json.add("result", records);
                out.println(gson.toJson(json));
                return;

            }

        }
    }

    private String processField(ArrayList temp, String value, String fieldName, int max) {
        if (value.isEmpty()) {
            return null;
        }

        if (value.length() > max) {
            temp.add("Invalid " + fieldName + " max length");
        }

        return value;
    }

    private void processCsv(Part filePart) throws IOException {
        InputStream istream = filePart.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(istream);
        Scanner sc = new Scanner(bis);
        //String columns = sc.nextLine().trim();
        //COL = columns.split(",");
        sc.useDelimiter(",");
        sc.nextLine();
        contactList = new ArrayList<String>();
        while (sc.hasNext()) {
            String content = sc.next().trim();
            contactList.add(content);
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
        contactList = new ArrayList<>();

        rowIter.next(); //skip the first blank line
        LOGGER.log(Level.INFO, "DEBUG2: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG3: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG4: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG5: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG6: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG7: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG8: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG9: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        rowIter.next();
        LOGGER.log(Level.INFO, "DEBUG11: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        LOGGER.log(Level.INFO, "DEBUG12: " + df.formatCellValue(((Row) rowIter.next()).getCell(1)));
        Row colRow = (Row) rowIter.next();

        ArrayList<String> list = new ArrayList<>();
        Iterator colIter = colRow.cellIterator();
        if (!df.formatCellValue((Cell) colRow.getCell(1)).isEmpty()) {
            while (colIter.hasNext()) {
                Cell cell = (Cell) colIter.next();
                LOGGER.log(Level.INFO, "COLUMN: " + df.formatCellValue(cell));
                String cellVal = df.formatCellValue(cell).toString();
                cellVal = cellVal.replace(".", "\\uFF0E"); //mongoDB doesn't accept key name containing "."
                list.add(cellVal);
            }
            COL = list.toArray(new String[0]);
        }

        while (rowIter.hasNext()) {
            Row row = (Row) rowIter.next();
            Iterator cellIter = row.cellIterator();
            if (cellIter.hasNext()) {
                String content = "";
                while (cellIter.hasNext()) {
                    String tempContent = df.formatCellValue((Cell) cellIter.next());
                    content += tempContent;
                    content += ",";
                }
                if (!content.equals("")) {
                    content = content.substring(0, content.length() - 1);
                    LOGGER.log(Level.INFO, "ROW CONTENT: " + content);
                    contactList.add(content);
                }
            } else {
                LOGGER.log(Level.INFO, "DEBUG: cellIter does NOT have Next Element");
                break;
            }
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