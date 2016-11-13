/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DAO.Libro;
import DAO.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author DSRS
 */
public class ObtenerRespuesta extends HttpServlet {

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

        request.setCharacterEncoding("UTF-8");
        
        String rptaU1 = request.getParameter("rpta1");
        String rptaU2 = request.getParameter("rpta2");
        String rptaU3 = request.getParameter("rpta3");
        String rptaU4 = request.getParameter("rpta4");
      
        String rpta1=rptaU1.replace("\n", "").trim();
        String rpta2=rptaU2.replace("\n", "").trim();
        String rpta3=rptaU3.replace("\n", "").trim();
        String rpta4=rptaU4.replace("\n", "").trim();

        Libro l = new Libro();
        int idPregunta1 = Integer.parseInt(request.getParameter("preg1"));
        int idPregunta2 = Integer.parseInt(request.getParameter("preg2"));
        int idPregunta3 = Integer.parseInt(request.getParameter("preg3"));
        int idPregunta4 = Integer.parseInt(request.getParameter("preg4"));
        
        
        PrintWriter out = response.getWriter();
        String msg="";
        int nota = 0;       
//        System.out.println("ev1: "+l.evaluar(idPregunta1, rpta1));
//        System.out.println("ev2: "+l.evaluar(idPregunta2, rpta2));
//        System.out.println("ev3: "+l.evaluar(idPregunta3, rpta3));
//        System.out.println("ev4: "+l.evaluar(idPregunta4, rpta4));
        
        boolean sw1 = l.evaluar(idPregunta1, rpta1);
        if (sw1 == true){
            nota++;
        }
        if (l.evaluar(idPregunta2, rpta2) == true){
             nota++;
        }
        if (l.evaluar(idPregunta3, rpta3) == true){
            nota++;
        }
        if (l.evaluar(idPregunta4, rpta4) == true){
             nota++;
        }        
        //System.out.println("Cantidad de aciertos: " + nota);
        if (nota == 0) {
            msg = "No acertaste ninguna ): \n Nota: 0";            
        }else if(nota==1){
            msg = "Solo acertaste 1 pregunta \n Nota: 1";                   
        }else if (nota==2){
            msg = "Acertaste 2 preguntas \n Nota: 2";                      
        }else if(nota==3){
            msg = "Acertaste 3 preguntas \n Nota: 3";          
        }else if(nota==4){
            msg = "¡¡ Acertaste todas !! \n Nota: 4";                   
        }
        out.write(msg);
        
        String idLibro = request.getParameter("idlibro");
        Cookie[] cookies = null;
        cookies = request.getCookies();
        int idU;
        int idL;
        String idUsuario = "";
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("idusuario")) {
                idUsuario = cookies[i].getValue();
            }
        }
        idU = Integer.parseInt(idUsuario);
        idL = Integer.parseInt(idLibro);
        Usuario u = new Usuario();
        int rpta= u.insertarNota(idU, idL, nota);
        //System.out.println("Se insertó la nota: "+rpta);
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
