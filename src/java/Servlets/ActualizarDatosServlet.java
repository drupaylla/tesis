/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DAO.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author DSRS
 */
@WebServlet(name = "ActualizarDatosServlet", urlPatterns = {"/ActualizarDatosServlet"})
public class ActualizarDatosServlet extends HttpServlet {

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

        //int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        String username = request.getParameter("username");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        Usuario usu = new Usuario();
        //PARA TRAER PARAMETRO DE SESION
        Cookie[] cookies = null;
        cookies = request.getCookies();
        String idUsuario = "";
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("idUsuario")) {
                idUsuario = cookies[i].getValue();
            }
        }
        int id = Integer.parseInt(idUsuario);
        String rpta = usu.modificarDatosUsuario(id, nombre, apellido, username);
        PrintWriter out = response.getWriter();
        out.print(rpta);
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
