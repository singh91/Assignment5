/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Connection.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


/**
 *
 * @author c0655613
 */
@Path("/products")
public class newServlet {

    @GET
    @Produces("application/json")
    public Response getAll() {

        return Response.ok(getResult("SELECT * FROM product")).build();
        // return Response.entity(getResult("SELECT * FROM product")).build();

    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getById(@PathParam("id") String id) {

        return Response.ok(getResult("SELECT * FROM product WHERE productID=?", String.valueOf(id))).build();

        // return Response.entity(getResult("SELECT * FROM product")).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response add(JsonObject json) {

        String name = json.getString("name");
        String description = json.getString("description");
        String quantity = String.valueOf(json.getInt("quantity"));

        System.out.println(name + '\t' + description + '\t' + quantity);

        int result = doUpdate("INSERT INTO product (name,description,quantity) VALUES (?,?,?)", name, description, quantity);
        if (result <= 0) {
            return Response.status(500).build();
        } else {
            return Response.ok(json).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateData(@PathParam("id") String id, JsonObject json) {

        String name = json.getString("name");
        String description = json.getString("description");
        String quantity = String.valueOf(json.getInt("quantity"));

        System.out.println(name + '\t' + description + '\t' + quantity +'\t'+ id);

        int result = doUpdate("UPDATE product SET name=?,description=?,quantity=? where productID=?", name, description, quantity, String.valueOf(id));
        if (result <= 0) {
            return Response.status(500).build();
        } else {
            return Response.ok(json).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces("application/json")

    public Response deleteById(@PathParam("id") String id) {

        int result = doUpdate("DELETE FROM product where productID=? ", String.valueOf(id));

        if (result <= 0) {
            return Response.status(500).build();
        } else {
            return Response.ok("ProductID: "+id+ " record is deleted successfully from product").build();
        }
        // return Response.entity(getResult("SELECT * FROM product")).build();

    }
//    
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
//        Set<String> key = request.getParameterMap().keySet();
//        try (PrintWriter out = response.getWriter()) {
//            if (key.contains("name") && key.contains("description") && key.contains("quantity")) {
//                String name = request.getParameter("name");
//                String desc = request.getParameter("description");
//                String quant = request.getParameter("quantity");
//
//                doUpdate("INSERT INTO product (name,description,quantity) VALUES (?,?,?)", name, desc, quant);
//                out.println("http://localhost:8080/NutsAndBolts/products?id="+doUpdate("SELECT LAST_INSERT_ID()"));
//            } else {
//                response.setStatus(500);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(newServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
//        Set<String> key = request.getParameterMap().keySet();
//        try (PrintWriter out = response.getWriter()) {
//            if (key.contains("productID") && key.contains("name") && key.contains("description") && key.contains("quantity")) {
//                String id = request.getParameter("productID");
//                String name = request.getParameter("name");
//                String desc = request.getParameter("description");
//                String quant = request.getParameter("quantity");
//
//                doUpdate("UPDATE product SET name=?,description=?,quantity=? where productID=? ", name, desc, quant, id);
//                out.println("http://localhost:8080/NutsAndBolts/products?id=" + id);
//            } else {
//                response.setStatus(500);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(newServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
//        Set<String> key = request.getParameterMap().keySet();
//        try (PrintWriter out = response.getWriter()) {
//            if (key.contains("productID")) {
//                String id = request.getParameter("productID");
//
//                doUpdate("DELETE FROM product where productID=? ", id);
//
//            } else {
//                response.setStatus(500);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(newServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public String getResult(String query, String... parameter) {
     StringBuilder sb = new StringBuilder();
     JsonObject obj = null;

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(query);
            for (int i = 1; i <= parameter.length; i++) {
                pst.setString(i, parameter[i - 1]);
            }
            System.out.println(parameter.length);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                obj =  Json.createObjectBuilder()
                .add("productID", rs.getInt("productID"))
                .add("name", rs.getString("name"))
                .add("description", rs.getString("description"))
                .add("quantity", rs.getInt("quantity")).build();
               sb.append(obj.toString());
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return sb.toString();
    }

    private int doUpdate(String query, String... parameter) {
        int change = 0;
        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= parameter.length; i++) {
                pstmt.setString(i, parameter[i - 1]);
            }
            change = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(newServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return change;
    }

}
