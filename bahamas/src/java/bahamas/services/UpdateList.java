/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.ContactDAO;
import bahamas.dao.list.UpdateListDAO;
import bahamas.entity.Contact;
import bahamas.util.Authenticator;
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import is203.JWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "UpdateList", urlPatterns = {"/updatelist"})
public class UpdateList extends HttpServlet {

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
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));

            } else {
                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();

                String token = Validator.containsBlankField(jobject.get("token"));
                String selectedList = Validator.containsBlankField(jobject.get("selectedList"));
                JsonArray jsonlist = jobject.getAsJsonArray("list");
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "fail");
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
                        if(contact.isIsAdmin() && !jsonlist.isJsonNull()){
                            //update db
                            if(UpdateList.dbTableMapping(selectedList, jsonlist)){
                                json.addProperty("message", "success");
                                out.println(gson.toJson(json));
                                return;
                            }
                            json.addProperty("message", "fail");
                            out.println(gson.toJson(json));
                            return;
                        }
                    }
                }
            }
            
        }    }
    
    private static boolean dbTableMapping(String tableID, JsonArray jsonlist){
        boolean status = false;
        switch (tableID) {
            case "1":
                //tableName = "TYPE_OF_CONTACT_LIST";
                UpdateListDAO.updateTypeOfContactList(jsonlist);
                status = true;
                break;
            case "2":
                UpdateListDAO.updateEventClassList(jsonlist);
                status = true;
                break;
            case "3":
                UpdateListDAO.updateEventLocationList(jsonlist);
                status = true;
                break;
            case "4":
                UpdateListDAO.updateEventStatusList(jsonlist);
                status = true;
                break;
            case "5":
                UpdateListDAO.updateLSAClassList(jsonlist);
                status = true;
                break;
            case "6":
                UpdateListDAO.updateLanguageList(jsonlist);
                status = true;
                break;
            case "7":
                UpdateListDAO.updateMembershipClassList(jsonlist);
                status = true;
                break;
            case "8":
                UpdateListDAO.updateModeOfSendingReceiptList(jsonlist);
                status = true;
                break;
            case "9":
                UpdateListDAO.updateOfficeList(jsonlist);
                status = true;
                break;
            case "10":
                UpdateListDAO.updatePaymentModeList(jsonlist);
                status = true;
                break;
            case "11":
                UpdateListDAO.updatePermissionLevelList(jsonlist);
                status = true;
                break;
            case "12":
                UpdateListDAO.updateTeamAffiliationList(jsonlist);
                status = true;
                break;
            default:
                return false;
        }
        return status;
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
