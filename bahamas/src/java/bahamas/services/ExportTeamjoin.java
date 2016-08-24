/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.TeamJoinDAO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Darryl Mok
 */
@WebServlet(name = "ExportTeamjoin", urlPatterns = {"/export.teamjoin"})
public class ExportTeamjoin extends HttpServlet {

    
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
                json.addProperty("message", "json empty");
                out.println(gson.toJson(json));
                
            } else {
                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();
                String token = Validator.containsBlankField(jobject.get("token"));
                //String cidString = jobject.get("cid").getAsString();
                //Optional for admin and novice
                //String teamName = jobject.get("teamname").getAsString();
                //String permission = jobject.get("permission").getAsString();

                if ((token == null || token.isEmpty())) {
                    json.addProperty("message", "no token");
                    out.println(gson.toJson(json));
                    return;
                }

                String username = Authenticator.verifyToken(token);
                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));
                    return;
                }
                
                TeamJoinDAO teamjoinDAO = new TeamJoinDAO();
                ArrayList<TeamJoin> aList = teamjoinDAO.retrieveAllTeamJoin();

                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat sdft = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                JsonArray teamjoinArray = new JsonArray();

                //start
                if (aList != null && !aList.isEmpty()) {

                    for (int i = 0; i < aList.size(); i++) {
                        JsonObject jsonTeamJoinObj = new JsonObject();
                        TeamJoin teamjoin = aList.get(i);

                        if (teamjoin.getContactId() > 0) {
                            jsonTeamJoinObj.addProperty("cid", teamjoin.getContactId());
                        } else {
                            jsonTeamJoinObj.addProperty("cid", "");
                        }

                        jsonTeamJoinObj.addProperty("teamjoin", teamjoin.getTeamName());

                        if (teamjoin.getExplainIfOthers() != null) {
                            jsonTeamJoinObj.addProperty("explain_if_other", teamjoin.getExplainIfOthers());
                        } else {
                            jsonTeamJoinObj.addProperty("explain_if_other", "");
                        }
                        if (teamjoin.getPermission() != null) {
                            jsonTeamJoinObj.addProperty("permission", teamjoin.getPermission());
                        } else {
                            jsonTeamJoinObj.addProperty("permission", "");
                        }
                        if (teamjoin.getRemarks() != null) {
                            jsonTeamJoinObj.addProperty("remarks", teamjoin.getRemarks());
                        } else {
                            jsonTeamJoinObj.addProperty("remarks", "");
                        }
                        if (teamjoin.getSubTeam() != null) {
                            jsonTeamJoinObj.addProperty("subteam", teamjoin.getSubTeam());
                        } else {
                            jsonTeamJoinObj.addProperty("subteam", "");
                        }
                        if (teamjoin.getDateObsolete() != null) {
                            jsonTeamJoinObj.addProperty("date_obsolete", sdf.format(teamjoin.getDateObsolete()));
                        } else {
                            jsonTeamJoinObj.addProperty("date_obsolete", "");
                        }
                        jsonTeamJoinObj.addProperty("created_by", teamjoin.getCreatedBy());
                        jsonTeamJoinObj.addProperty("date_created", sdft.format(teamjoin.getDateCreated()));
                        teamjoinArray.add(jsonTeamJoinObj);

                    }
                    json.addProperty("message", "success");
                    json.add("teamjoinlist", teamjoinArray);
                } else {

                    json.addProperty("message", "list empty");
                    out.println(gson.toJson(json));
                    return;
                }

            }

            out.println(gson.toJson(json));
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
