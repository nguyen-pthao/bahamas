/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.ReportDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.Contact;
import bahamas.util.Authenticator;
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HUXLEY
 */
@WebServlet(name = "Report", urlPatterns = {"/report"})
public class Report extends HttpServlet {

    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            JsonObject json = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            //Retrieve the json string as a reader 
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = request.getReader();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));
                return;
            }

            String jsonLine = sb.toString();
            if (jsonLine == null || jsonLine.isEmpty()) {
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));

            } else {
                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();

                String token = Validator.containsBlankField(jobject.get("token"));
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));

                } else {

                    ContactDAO cDAO = new ContactDAO();
                    Contact user = cDAO.retrieveContactByUsername(username);
                    String userType = Validator.containsBlankField(jobject.get("user_type"));
                    if (!user.isIsAdmin() && !userType.equals("teammanager") && !RoleCheckDAO.checkRole(user.getContactId(), userType)) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    }

                    String reportType = Validator.containsBlankField(jobject.get("report_type"));

                    if (reportType.equalsIgnoreCase("summary_of_team_participants")) {
                        String team = Validator.containsBlankField(jobject.get("team"));
                        Date startDate = Validator.isDateValid(jobject.get("start_date"), "start date");
                        Date endDate = Validator.isDateValid(jobject.get("end_date"), "end date");

                        if (startDate != null && endDate != null) {
                            if (endDate.before(startDate) || !startDate.before(endDate)) {
                                Validator.getErrorList().add("Membership start date must be before membership end date");
                            }
                        }

                        if (!Validator.getErrorList().isEmpty()) {
                            JsonArray errorArray = new JsonArray();
                            for (String s : Validator.getErrorList()) {
                                JsonPrimitive o = new JsonPrimitive(s);
                                errorArray.add(o);
                            }
                            Validator.getErrorList().clear();
                            json.add("message", errorArray);
                            out.println(gson.toJson(json));
                            return;
                        }

                        HashMap<Integer, ArrayList<String>> resultMap = ReportDAO.summaryTeamParticipants(team, startDate, endDate);
                        json.addProperty("Team", team);
                        json.addProperty("Start Date", format.format(startDate));
                        json.addProperty("End Date", format.format(endDate));
                        
                        processSummaryTeamParticipants(resultMap, json);
                    }

                    AuditLogDAO.insertAuditLog(username, "Generate Report", reportType);
                    out.println(gson.toJson(json));

                }
            }
        }
    }

    private void processSummaryTeamParticipants(HashMap<Integer, ArrayList<String>> resultMap, JsonObject json) {

        JsonArray records = new JsonArray();
        int counter = 0;
        Iterator<Integer> iterLine = resultMap.keySet().iterator();
        while (iterLine.hasNext()) {
            int num = iterLine.next();
            ArrayList<String> temp = resultMap.get(num);

            JsonObject store = new JsonObject();
            store.addProperty("Serial Number", String.valueOf(++counter));
            store.addProperty("Contact Name", temp.get(0));
            store.addProperty("Username", temp.get(1));
            store.addProperty("Event Date of 1st participation", temp.get(2));
            store.addProperty("Date of last participation *unverified", temp.get(3));
            store.addProperty("Number of sign-ups within this period", temp.get(4));
            store.addProperty("Cumulative number of awarded hours within this period", temp.get(5));

            records.add(store);
        }

        json.add("Records", records);

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
