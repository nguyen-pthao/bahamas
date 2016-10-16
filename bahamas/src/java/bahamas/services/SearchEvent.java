package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bahamas.dao.ContactDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.SearchEventDAO;
import bahamas.entity.Contact;
import bahamas.entity.Event;
import bahamas.util.Authenticator;
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tan.si.hao
 */
@WebServlet(urlPatterns = {"/event.search"})
public class SearchEvent extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();
                String token = Validator.containsBlankField(jobject.get("token"));
                String eventTitle = Validator.containsBlankField(jobject.get("event_title"));
                String event_location = Validator.containsBlankField(jobject.get("event_location"));
                Date startDateStr = Validator.isDateValid(jobject.get("start_date"), "start_date");
                Date endDateStr = Validator.isDateValid(jobject.get("end_date"), "end_date");
                String teamAffiliation = Validator.containsBlankField(jobject.get("team_affiliation"));
                String participant = Validator.containsBlankField(jobject.get("participant"));
                String ifLocationOther = Validator.containsBlankField(jobject.get("if_location_other"));
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");

                String username = Authenticator.verifyToken(token);
                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));
                } else {
                    ContactDAO cDAO = new ContactDAO();
                    Contact contact = cDAO.retrieveContactByUsername(username);
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat date2 = new SimpleDateFormat("dd-MMM-yyyy");
                    SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
                    if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager")) {
                        HashMap<Integer, Event> eventHM = null;
                        try {
                            //if (event_location != null && (eventTitle != null || startDateStr != null || endDateStr != null)) {
                                Date startdate = null;
                                Date enddate = null;
                                if (startDateStr != null) {
                                    startdate = date.parse(date.format(startDateStr));
                                }
                                if (endDateStr != null) {
                                    enddate = date.parse(date.format(endDateStr));
                                }
                                if (event_location != null && !event_location.equalsIgnoreCase("other")) {
                                    eventHM = SearchEventDAO.searchEventByTitleLocationDate(eventTitle, event_location, startdate, enddate, true);
                                } else {
                                    eventHM = SearchEventDAO.searchEventByTitleLocationDate(eventTitle, ifLocationOther, startdate, enddate, false);
                                    
                                }
                            //}

                            if (eventHM == null && teamAffiliation != null && !teamAffiliation.equalsIgnoreCase("other")) {
                                eventHM = SearchEventDAO.searchEventByTeam(teamAffiliation);
                            } else if (eventHM != null && teamAffiliation != null) {
                                HashMap<Integer, Event> tempHM = SearchEventDAO.searchEventByTeam(teamAffiliation);
                                eventHM.keySet().retainAll(tempHM.keySet());
                            }

                            if (eventHM == null && participant != null) {
                                eventHM = SearchEventDAO.searchEventByParticipant(participant);
                            } else if (eventHM != null && participant != null) {
                                HashMap<Integer, Event> tempHM = SearchEventDAO.searchEventByParticipant(participant);
                                eventHM.keySet().retainAll(tempHM.keySet());
                            }

                            json.addProperty("message", "success");
                            JsonArray eventtArray = new JsonArray();
                            JsonObject jsonEventObj;
                            Date currentDateTime = new Date();
                            Date currentDate = date2.parse(date2.format(currentDateTime));
                            Date currentTime = time.parse(time.format(currentDateTime));
                            
                            for (int tempEventId : eventHM.keySet()) {
                                Event tempEvent = eventHM.get(tempEventId);
                                jsonEventObj = new JsonObject();
                                jsonEventObj.addProperty("eventid", tempEvent.getEventId());
                                jsonEventObj.addProperty("event_title", tempEvent.getEventTitle());
                                jsonEventObj.addProperty("startdate", sdf.format(tempEvent.getEventStartDate()));
                                Date eventEndDate = date2.parse(date2.format(tempEvent.getEventEndDate()));
                                Date eventEndTime = time.parse(time.format(tempEvent.getEventEndTime()));
                                if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                    jsonEventObj.addProperty("ispast", false);
                                }else{
                                    jsonEventObj.addProperty("ispast", true);
                                }
                                jsonEventObj.addProperty("event_status", tempEvent.getEventStatus());
                                eventtArray.add(jsonEventObj);
                            }

                            json.add("event", eventtArray);
                            out.println(gson.toJson(json));

                        } catch (ParseException ex) {
                            Logger.getLogger(SearchEvent.class.getName()).log(Level.SEVERE, null, ex);
                            json.addProperty("message", "fail");
                            out.println(gson.toJson(json));
                        }

                    } else {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                    }
                }
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
