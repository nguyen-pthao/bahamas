package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bahamas.dao.ContactDAO;
import bahamas.dao.EventAffiliationDAO;
import bahamas.dao.EventDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.Contact;
import bahamas.entity.Event;
import bahamas.entity.EventAffiliation;
import bahamas.entity.EventParticipant;
import bahamas.entity.TeamJoin;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
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
@WebServlet(urlPatterns = {"/event.retrieve.homepage"})
public class RetrieveEventsHomePage extends HttpServlet {

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
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));
                } else {
                    //Verified token
                    ContactDAO cDAO = new ContactDAO();
                    Contact contact = cDAO.retrieveContactByUsername(username);

                    if (contact == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {

                        EventDAO eventDAO = new EventDAO();
                        ArrayList<Event> eventList = eventDAO.retrieveAllEvents();

                        ArrayList<EventParticipant> eventParticipantList = EventParticipantDAO.retrieveParticipantbyContactID(contact.getContactId());

                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
                        JsonArray eventsJoinedArray = new JsonArray();
                        JsonArray eventsCreatedArray = new JsonArray();
                        HashMap<Integer, Integer> eventIdJoinedHM = new HashMap<Integer, Integer>();

                        JsonObject jsonContactObj;

                        if (eventParticipantList != null) {
                            for (EventParticipant eventParticipant : eventParticipantList) {
                                int tempEventId = eventParticipant.getEventID();
                                Event tempEvent = eventDAO.retrieveEventById(tempEventId);
                                Date currentDateTime = new Date();
                                try {
                                    Date currentDate = date.parse(date.format(currentDateTime));
                                    Date currentTime = time.parse(time.format(currentDateTime));
                                    Date eventStartDate = date.parse(date.format(tempEvent.getEventStartDate()));
                                    Date eventEndDate = date.parse(date.format(tempEvent.getEventEndDate()));
                                    Date eventEndTime = time.parse(time.format(tempEvent.getEventEndTime()));

                                    Calendar calStartDate = Calendar.getInstance();
                                    calStartDate.setTime(tempEvent.getEventStartDate());
                                    calStartDate.set(Calendar.HOUR_OF_DAY, 0);
                                    calStartDate.set(Calendar.MINUTE, 0);
                                    calStartDate.set(Calendar.SECOND, 0);

                                    Calendar calEndDate = Calendar.getInstance();
                                    calEndDate.setTime(tempEvent.getEventEndDate());
                                    calEndDate.set(Calendar.HOUR_OF_DAY, 0);
                                    calEndDate.set(Calendar.MINUTE, 0);
                                    calEndDate.set(Calendar.SECOND, 0);

                                    //if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                    if (!eventParticipant.isPullout()) {

                                        //Version 1
                                        /*
                                        while (!calStartDate.equals(calEndDate)) {
                                            jsonContactObj = new JsonObject();
                                            jsonContactObj.addProperty("title", tempEvent.getEventTitle());
                                            jsonContactObj.addProperty("start", date.format(calStartDate.getTime()));
                                            if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                                jsonContactObj.addProperty("state", ".viewIndivEvent");
                                            } else {
                                                jsonContactObj.addProperty("state", ".viewPastIndivEvent");
                                            }
                                            jsonContactObj.addProperty("eventId", tempEvent.getEventId());
                                            eventIdJoinedHM.put(tempEvent.getEventId(), tempEvent.getEventId());
                                            eventsJoinedArray.add(jsonContactObj);
                                            calStartDate.add(Calendar.DATE, 1);
                                        }

                                        jsonContactObj = new JsonObject();
                                        jsonContactObj.addProperty("title", tempEvent.getEventTitle());
                                        jsonContactObj.addProperty("start", date.format(tempEvent.getEventEndDate()));
                                        if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                            jsonContactObj.addProperty("state", ".viewIndivEvent");
                                        } else {
                                            jsonContactObj.addProperty("state", ".viewPastIndivEvent");
                                        }
                                        jsonContactObj.addProperty("eventId", tempEvent.getEventId());
                                        
                                        
                                        // version 2
                                        jsonContactObj = new JsonObject();
                                        jsonContactObj.addProperty("title", tempEvent.getEventTitle());
                                        jsonContactObj.addProperty("start", date.format(tempEvent.getEventStartDate()));
                                        jsonContactObj.addProperty("end", date.format(tempEvent.getEventEndDate()));
                                        if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                            jsonContactObj.addProperty("state", ".viewIndivEvent");
                                        } else {
                                            jsonContactObj.addProperty("state", ".viewPastIndivEvent");
                                        }
                                        jsonContactObj.addProperty("eventId", tempEvent.getEventId());
                                        */
                                        
                                        //version 3
                                        TimeZone tz = TimeZone.getTimeZone("UTC");
                                        DateFormat dftz = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
                                        dftz.setTimeZone(tz);
                                        String startDateTZ = dftz.format(tempEvent.getEventStartDate());
                                        String endDateTZ = dftz.format(tempEvent.getEventEndDate());
                                        jsonContactObj = new JsonObject();
                                        jsonContactObj.addProperty("title", tempEvent.getEventTitle());
                                        jsonContactObj.addProperty("start", startDateTZ);
                                        jsonContactObj.addProperty("end", endDateTZ);
                                        if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                            jsonContactObj.addProperty("state", ".viewIndivEvent");
                                        } else {
                                            jsonContactObj.addProperty("state", ".viewPastIndivEvent");
                                        }
                                        jsonContactObj.addProperty("eventId", tempEvent.getEventId());
                                        
                                        
                                        //end of version
                                        eventIdJoinedHM.put(tempEvent.getEventId(), tempEvent.getEventId());
                                        eventsJoinedArray.add(jsonContactObj);
                                    }

                                    //}
                                } catch (ParseException ex) {
                                    Logger.getLogger(RetrieveEventsHomePage.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        }

                        if (eventList != null) {
                            for (int i = 0; i < eventList.size(); i++) {
                                Event event = eventList.get(i);
                                if (event.getContactId() == contact.getContactId() && !eventIdJoinedHM.containsKey(event.getEventId())) {

                                    Date currentDateTime = new Date();
                                    try {
                                        Date currentDate = date.parse(date.format(currentDateTime));
                                        Date currentTime = time.parse(time.format(currentDateTime));
                                        Date eventEndDate = date.parse(date.format(event.getEventEndDate()));
                                        Date eventEndTime = time.parse(time.format(event.getEventEndTime()));
                                        //if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                        jsonContactObj = new JsonObject();
                                        
                                        //version 1
                                        /*
                                        jsonContactObj.addProperty("title", event.getEventTitle());
                                        jsonContactObj.addProperty("start", date.format(event.getEventStartDate()));
                                        if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                            jsonContactObj.addProperty("state", ".viewIndivEvent");
                                        } else {
                                            jsonContactObj.addProperty("state", ".viewPastIndivEvent");
                                        }
                                        jsonContactObj.addProperty("eventId", event.getEventId());
                                        */
                                        
                                        //version 2
                                        jsonContactObj.addProperty("title", event.getEventTitle());
                                        jsonContactObj.addProperty("start", date.format(event.getEventStartDate()));
                                        jsonContactObj.addProperty("end", date.format(event.getEventEndDate()));
                                        if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                            jsonContactObj.addProperty("state", ".viewIndivEvent");
                                        } else {
                                            jsonContactObj.addProperty("state", ".viewPastIndivEvent");
                                        }
                                        jsonContactObj.addProperty("eventId", event.getEventId());
                                        
                                        
                                        //version 3
                                        
                                        TimeZone tz = TimeZone.getTimeZone("UTC");
                                        DateFormat dftz = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
                                        dftz.setTimeZone(tz);
                                        String startDateTZ = dftz.format(event.getEventStartDate());
                                        String endDateTZ = dftz.format(event.getEventEndDate());
                                        jsonContactObj.addProperty("title", event.getEventTitle());
                                        jsonContactObj.addProperty("start", startDateTZ);
                                        jsonContactObj.addProperty("end", endDateTZ);
                                        if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {
                                            jsonContactObj.addProperty("state", ".viewIndivEvent");
                                        } else {
                                            jsonContactObj.addProperty("state", ".viewPastIndivEvent");
                                        }
                                        jsonContactObj.addProperty("eventId", event.getEventId());
                                        
                                        
                                        //end of version
                                        eventsCreatedArray.add(jsonContactObj);
                                        //}
                                    } catch (ParseException ex) {
                                        Logger.getLogger(RetrieveEventsHomePage.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }

                        json.addProperty("message", "success");
                        json.add("eventsJoined", eventsJoinedArray);
                        json.add("eventsCreated", eventsCreatedArray);
                        out.println(gson.toJson(json));
                        /*
                         if (eventList != null) {
                         json.addProperty("message", "success");

                         for (int i = 0; i < eventList.size(); i++) {
                         Event event = eventList.get(i);
                         if (event.getContactId() == contact.getContactId()) {
                         eventIdOwerIdHM.put(event.getEventId(), event.getEventId());
                         }
                         Date currentDateTime = new Date();
                         try {
                         Date currentDate = date.parse(date.format(currentDateTime));
                         Date currentTime = time.parse(time.format(currentDateTime));
                         Date eventEndDate = date.parse(date.format(event.getEventEndDate()));
                         Date eventEndTime = time.parse(time.format(event.getEventEndTime()));
                         if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {

                         jsonContactObj = new JsonObject();
                         jsonContactObj.addProperty("title", event.getEventTitle());
                         jsonContactObj.addProperty("start", date.format(event.getEventStartDate()));
                         jsonContactObj.addProperty("state", ".viewIndivEvent");
                         jsonContactObj.addProperty("eventId", event.getEventId());

                         eventsJoinedArray.add(jsonContactObj);
                         }
                         } catch (ParseException ex) {
                         Logger.getLogger(RetrieveEventsHomePage.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         }

                         json.add("eventsJoined", eventsJoinedArray);
                         json.add("eventsCreated", eventsCreatedArray);
                         out.println(gson.toJson(json));

                         } else {
                         json.addProperty("message", "Fail retrieve all upcoming events");
                         out.println(gson.toJson(json));
                         }
                         */
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
